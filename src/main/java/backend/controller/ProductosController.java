package backend.controller;

import backend.model.Producto;
import backend.repository.ProductoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductosController {

    private final ProductoRepository productoRepository;


    public ProductosController(
            ProductoRepository productoRepository
    ) {
        this.productoRepository =
                productoRepository;
    }


    @GetMapping("/productos")
    public List<Producto> getProductos() {

        return productoRepository.findAll();
    }


    @PostMapping("/productos")
    public Producto crearProducto(
            @RequestBody Producto producto
    ) {

        return productoRepository.save(producto);
    }
}