package com.agrosmart.web.controller;

import com.agrosmart.domain.model.ProductoDominio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public Flux<ProductoDominio> obtenerProductos() {
        return productoService.obtenerProductosComercializables();
    }

    @GetMapping("/{id}")
    public Mono<ProductoDominio> obtenerProductoPorId(@PathVariable long id) {
        return productoService.buscarPorId(id);
    }
}
