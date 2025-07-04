package progAvan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import progAvan.Model.Modelo;
import progAvan.Service.ModeloService;

@RestController
@RequestMapping(path = "/modelo")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    Map<String, String> response = new HashMap<>();

    @PostMapping(value = "/guardar")
    public ResponseEntity<Map<String, String>> guardar(@RequestBody Modelo model) {
        try {
            modeloService.save(model);
            this.response.put("message", "success");
            return new ResponseEntity<>(this.response, HttpStatus.OK);
        } catch (Exception e) {
            this.response.put("message", "error interno");
            return new ResponseEntity<>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/mostrarpaginado")
    public List<Modelo> mostrarPaginado(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return modeloService.findPaginado(page, size);
    }

    @GetMapping(value = "/longitud")
    public long longitud() {
        return modeloService.longitud();
    }

    @GetMapping(value = "/mostrarHabilitados")
    public List<Modelo> mostrarHabilitados() {
        return modeloService.findHabilitados();
    }

    @GetMapping(value = "/mostrarInhabilitados")
    public List<Modelo> mostrarInhabilitados() {
        return modeloService.findInhabilitados();
    }

    @GetMapping(value = "/mostrarXMarca/{id}")
    public List<Modelo> mostrarXMarca(@PathVariable int id) {
        System.out.println(id);
        return modeloService.findModelosXMarca(id);
    }

    @PostMapping(value = "/editar/{id}")
    public ResponseEntity<Map<String, String>> actualizar(@PathVariable int id, @RequestBody Modelo model) {
        // Modelo modelo = modeloService.findById(id).orElse(null);
        try {
            modeloService.save(model);
            this.response.put("message", "success");
            return new ResponseEntity<>(this.response, HttpStatus.OK);
        } catch (Exception e) {
            this.response.put("message", "error interno");
            return new ResponseEntity<>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable int id) {
        try {
            Optional<Modelo> optionalModelo = modeloService.findById(id);

            if (optionalModelo.isPresent()) {
                Modelo modelo = optionalModelo.get();
                // modelo.setEstado(!modelo.getEstado());
                // modeloService.save(modelo);
                modeloService.deshabilitarModeloYRelacionados(modelo.getId());

                this.response.put("message", "success");
                return new ResponseEntity<>(this.response, HttpStatus.OK);
            } else {
                this.response.put("message", "error");
                return new ResponseEntity<>(this.response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            this.response.put("message", "error interno");
            return new ResponseEntity<>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
