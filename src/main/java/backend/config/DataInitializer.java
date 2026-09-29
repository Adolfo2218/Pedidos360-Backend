package backend.config;

import backend.model.Producto;
import backend.repository.ProductoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarProductos(
            ProductoRepository productoRepository
    ) {

        return args -> {

            if (productoRepository.count() == 0) {

                productoRepository.save(
                    new Producto(
                        "Producto Uno",
                        12000,
                        10
                    )
                );

                productoRepository.save(
                    new Producto(
                        "Producto Dos",
                        8500,
                        20
                    )
                );

                productoRepository.save(
                    new Producto(
                        "Producto Tres",
                        15000,
                        5
                    )
                );

                System.out.println(
                    "Productos iniciales creados"
                );
            }
        };
    }
}