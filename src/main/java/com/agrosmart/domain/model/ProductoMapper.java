package com.agrosmart.domain.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductoMapper {

    public static ProductoDominio toDominio(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }

        List<String> correos = parseCorreos(entity.getCorreosNotificacion());

        return new ProductoDominio(
                entity.getIdProducto(),
                entity.getNombreProducto(),
                entity.getCategoria(),
                entity.getPrecioUsd(),
                correos
        );
    }

    private static List<String> parseCorreos(String correosStr) {
        if (correosStr == null || correosStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(correosStr.split("\\s*,\\s*"));
    }
}
