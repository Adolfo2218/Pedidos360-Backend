package backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ObrasController {

    @GetMapping("/obras")
    public List<Map<String, Object>> getObras() {

        return List.of(
            Map.of(
                "id", 1,
                "nombre", "Obra Uno",
                "artista", "Artista Uno"
            ),
            Map.of(
                "id", 2,
                "nombre", "Obra Dos",
                "artista", "Artista Dos"
            )
        );
    }
}