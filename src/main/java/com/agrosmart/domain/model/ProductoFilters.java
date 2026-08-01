package com.agrosmart.domain.model;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductoFilters {
    public static final Predicate<ProductoDominio> IS_VALID =
            producto -> producto.getPrecioUsd().compareTo(BigDecimal.ZERO) > 0
            && !producto.getCorreosNotificacion().isEmpty();

    public  static final Consumer<ProductoDominio> LOG_PRODUCTO =
            producto -> System.out.println("Producto procesado: ID=" + producto.getId()
                                                            + ", Nombre=" + producto.getNombre());

    public static final Function<ProductoDominio, ProductoDominio> A_MAYUSCULAS =
            producto -> new ProductoDominio(
                    producto.getId(),
                    producto.getNombre().toUpperCase(),
                    producto.getCategoria(),
                    producto.getPrecioUsd(),
                    producto.getCorreosNotificacion()
            );
}
