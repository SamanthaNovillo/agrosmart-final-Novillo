package com.agrosmart.config;

import com.agrosmart.domain.model.ProductoEntity;
import com.agrosmart.domain.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {
    private final ProductoRepository repository;

    public DataLoader(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new ProductoEntity(
                    "Cafe especial colombiano",
                    new BigDecimal("25.50"),
                    100,
                    "Café",
                    "cliente1@gmail.com,cliente2@hotmail.com"
            ));

            repository.save(new ProductoEntity(
                    "Cafe Gourmet Brasileño",
                    new BigDecimal("32.00"),
                    75,
                    "Café",
                    "cliente3@yahoo.com"
            ));

            repository.save(new ProductoEntity(
                    "Café Etiopía Yugarcheffe",
                    new BigDecimal("38.50"),
                    50,
                    "Café",
                    "cliente4@gmail,com,cliente5@hotmail.com"
            ));

            repository.save(new ProductoEntity(
                    "Café sin notificación",
                    new BigDecimal("15.00"),
                    10,
                    "Café",
                    ""
            ));

            System.out.println("Los 5 productos de café cargados (Los 3 son válidos y 2 inválidos)");

        }
    }
}
