package progAvan.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import progAvan.Model.Estado;
import progAvan.Service.EstadoService;

@RestController
@RequestMapping(path = "/estado")
public class EstadoController {
    @Autowired
    private EstadoService estadoService;
    Map<String, String> response = new HashMap<>();

    @GetMapping(value = "/mostrar")
    public List<Estado> mostrar() {
        return estadoService.findAll();
    }

}
