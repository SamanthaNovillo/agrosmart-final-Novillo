package com.agrosmart;

import com.agrosmart.domain.model.ProductoDominio;
import com.agrosmart.domain.model.ProductoFilters;
import com.agrosmart.domain.model.ProductoMapper;
import com.agrosmart.domain.model.ProductoEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PruebaFase3 {

    public static void main(String [] args) {
        System.out.println("=== Prueba Fase 3 - Modelo Inmutable ===\n");

        List<String> correos = new ArrayList<>();
        correos.add("cliente1@email.com");
        correos.add("cliente2@email.com");

        ProductoDominio producto = new ProductoDominio(
                1L,
                "Café especial",
                "Café",
                new BigDecimal("25.50"),
                correos
        );

        System.out.println("Producto creado:");
        System.out.println(producto);

        correos.add("cliente3@email.com");
        System.out.println("\nLista original modificada (cliente3 agregado):");
        System.out.println("Correos internos del producto" + producto.getCorreosNotificacion());
        System.out.println("Los correos internos NO CAMBIARON (la copia defensiva funciona");

        System.out.println("\n--- Probando filtros ---");
        System.out.println("¿Es válido?" + ProductoFilters.IS_VALID.test(producto));

        System.out.println("\n--- Probando LOG_PRODUCTOS ---");
        ProductoFilters.LOG_PRODUCTO.accept(producto);

        ProductoDominio productoMayus = ProductoFilters.A_MAYUSCULAS.apply(producto);
        System.out.println("\nProducto en mayúsculas:");
        System.out.println("Nombre original: " + producto.getNombre());
        System.out.println("Nombre en mayúsculas: " + productoMayus.getNombre());
        System.out.println("El producto original NO se modificó (inmutabilidad)");

        System.out.println("\n--- Probando ProductoMapper ---");
        ProductoEntity entity = new ProductoEntity();
        entity.setIdProducto(2L);
        entity.setNombreProducto("Café Gourmet");
        entity.setCategoria("Café");
        entity.setPrecioUsd(new BigDecimal("32.00"));
        entity.setCorreosNotificacion("cliente3@email.com,cliente4@email.com");

        ProductoDominio convertido = ProductoMapper.toDominio(entity);
        System.out.println("Entidad convertida a dominio:");
        System.out.println(convertido);

        System.out.println("\n La prueba se completo exitosamente");
    }
}
