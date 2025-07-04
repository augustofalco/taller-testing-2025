package progAvan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import progAvan.Service.EstadisticaService;

@RestController
@RequestMapping("/estadistica")
@RequiredArgsConstructor

public class EstadisticaController {

    @Autowired
    private EstadisticaService estadisticaService;
    
    Map<String, String> response = new HashMap<>();

    @Value("${path_general}")
    String path;

    @GetMapping(value = "/comparacionMinutos/{fechaInferior}/{fechaSuperior}")
    public List<Object> comparacionMinutosServicios(@PathVariable String fechaInferior, @PathVariable String fechaSuperior) {

        return estadisticaService.comparacionMinutosServicios(fechaInferior,fechaSuperior);
    }
    
    @GetMapping(value = "/estadisticaOrden/{fechaInferior}/{fechaSuperior}")
    public List<Object> estadisticaOrden(@PathVariable String fechaInferior, @PathVariable String fechaSuperior) {
        return estadisticaService.estadisticaOrden(fechaInferior,fechaSuperior);
    }

    @GetMapping(value = "/cantidadServiciosDetalle/{fechaInferior}/{fechaSuperior}")
    public List<Object> cantidadServiciosEnDetalleOrden(@PathVariable String fechaInferior, @PathVariable String fechaSuperior) {
        return estadisticaService.cantidadServiciosEnDetalleOrden(fechaInferior,fechaSuperior);
    }
}
