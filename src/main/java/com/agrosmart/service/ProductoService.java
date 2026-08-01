package com.agrosmart.service;

import com.agrosmart.domain.exception.ProductoNoEncontradoException;
import com.agrosmart.domain.model.ProductoDominio;
import com.agrosmart.domain.model.ProductoFilters;
import com.agrosmart.domain.model.ProductoMapper;
import com.agrosmart.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.Collections;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    private static final ProductoDominio PRODUCTO_GENERICO = new ProductoDominio(
            0L,
            "Producto por defecto",
            "Café",
            BigDecimal.ZERO,
            Collections.emptyList()
    );

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    /*
     * 4.1 Obtener los productos comercializables
     * Operadores usados y su propósito:
     * + fromCallable(): difiere la consulta bloqueante; nada se ejecuta hasta que alguien se suscriba
     * + subscribeOn(boundedElastic): JPA/Hibernate bloquea el hilo. Si esto corriera en el
     *   event loop de Netty, un solo hilo bloqueado degradaría TODAS las peticiones
     * + flatMapMany(): convierte la lista materializada en un flujo reactivo elemento a elemento
     * + map(ProductoMapper::toDominio): traduce la entidad JPA al modelo de dominio inmutable
     * + map(A_MAYUSCULAS): aplica la transformación a mayúsculas sin mutar el original
     * + filter(IS_VALID): descarta los productos no comercializables (precio <= 0)
     * + doOnNext(LOG_PRODUCTO): efecto de trazabilidad/logging, sin transformar el flujo
     * + defaultIfEmpty(): emite el producto genérico si el filtro dejó el flujo vacío
     */
    public Flux<ProductoDominio> obtenerProductosComercializables() {
        return Mono.fromCallable(repository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .map(ProductoMapper::toDominio)
                .map(ProductoFilters.A_MAYUSCULAS)
                .filter(ProductoFilters.IS_VALID)
                .doOnNext(ProductoFilters.LOG_PRODUCTO)
                .defaultIfEmpty(PRODUCTO_GENERICO);
    }

    /*
     * 4.2 Buscar producto por ID
     * Operadores usados y su propósito:
     * + fromCallable(): difiere la consulta bloqueante hasta la suscripción
     * + subscribeOn(boundedElastic): aísla el bloqueo de JPA fuera del event loop de Netty
     * + flatMap(Mono::justOrEmpty): traduce el Optional vacío del repositorio a un Mono vacío
     * + map(ProductoMapper::toDominio): traduce la entidad JPA al modelo de dominio
     * + switchIfEmpty(): el "no encontrado" se resuelve DENTRO del flujo reactivo,
     *   sin sacar el valor del contexto reactivo (prohibido usar block() o un if externo)
     */
    public Mono<ProductoDominio> buscarPorId(Long id) {
        return Mono.fromCallable(() -> repository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty)
                .map(ProductoMapper::toDominio)
                .switchIfEmpty(Mono.error(new ProductoNoEncontradoException(id)));
    }
}