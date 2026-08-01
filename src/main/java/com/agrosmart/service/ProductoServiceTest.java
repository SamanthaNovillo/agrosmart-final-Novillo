package com.agrosmart.service;

import com.agrosmart.domain.exception.ProductoNoEncontradoException;
import com.agrosmart.domain.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void obtenerProductosComercializables_shouldReturnOnlyValidProducts() {
        when(repository.findAll()).thenReturn(List.of());

        StepVerifier.create(productoService.obtenerProductosComercializables())
                .expectNextCount(1)  // El producto genérico
                .verifyComplete();
    }

    @Test
    void buscarPorId_whenProductNotFound_shouldThrowException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        StepVerifier.create(productoService.buscarPorId(999L))
                .expectError(ProductoNoEncontradoException.class)
                .verify();
    }
}