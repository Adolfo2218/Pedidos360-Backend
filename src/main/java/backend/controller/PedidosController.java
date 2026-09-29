package backend.controller;

import backend.model.Pedido;
import backend.repository.PedidoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PedidosController {

    private final PedidoRepository pedidoRepository;


    public PedidosController(
            PedidoRepository pedidoRepository
    ) {
        this.pedidoRepository =
                pedidoRepository;
    }


    @GetMapping("/pedidos")
    public List<Pedido> getPedidos() {

        return pedidoRepository.findAll();
    }


    @PostMapping("/pedidos")
    public Pedido crearPedido(
            @RequestBody Pedido pedido
    ) {

        pedido.setEstado("CREADO");

        return pedidoRepository.save(pedido);
    }
}