package com.agrosmart.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProductoDominio {
    private final Long id;
    private final String nombre;
    private final String categoria;
    private final BigDecimal precioUsd;
    private final List<String> correosNotificacion;

    public ProductoDominio(Long id, String nombre, String categoria,
                           BigDecimal precioUsd, List<String> correosNotificacion) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioUsd = precioUsd;
        this.correosNotificacion = new ArrayList<>(correosNotificacion);
    }

    public Long getId() {return id; }
    public String getNombre(){return nombre; }
    public String getCategoria() {return categoria; }
    public BigDecimal getPrecioUsd() { return precioUsd; }

    public List<String> getCorreosNotificacion() {
        return Collections.unmodifiableList(new ArrayList<>(correosNotificacion));
    }
    @Override
    public String toString() {
        return "ProductoDominio{" +
                "id" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria='" +categoria + '\'' +
                ", precioUsd=" + precioUsd +
                ", correos=" + correosNotificacion +
                '}';
    }
}
