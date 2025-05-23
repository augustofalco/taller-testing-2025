package progAvan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import progAvan.Model.Tecnico;
import progAvan.Service.TecnicoService;

@RestController
@RequestMapping(path = "/tecnico")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    Map<String, String> response = new HashMap<>();

    @Value("${path_general}")
    String path;

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @PostMapping(value = "/guardar")
    public ResponseEntity<Map<String, String>> guardar(@RequestBody Tecnico model) {
        try {
            tecnicoService.save(model);
            this.response.put("message", "success");
            return new ResponseEntity<>(this.response, HttpStatus.OK);
        } catch (Exception e) {
            this.response.put("message", "error interno");
            return new ResponseEntity<>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrar")
    public List<Tecnico> mostrar() {
        return tecnicoService.findAll();
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrarpaginado")
    public List<Tecnico> mostrarPaginado(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return tecnicoService.findPaginado(page, size);
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/longitud")
    public long longitud() {
        return tecnicoService.longitud();
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrarHabilitados")
    public List<Tecnico> mostrarHabilitados() {
        return tecnicoService.findHabiliitados();
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @PostMapping(value = "/editar/{id}")
    public ResponseEntity<Map<String, String>> actualizar(@PathVariable int id, @RequestBody Tecnico model) {
        // Tecnico tecnico = tecnicoService.findById(id).orElse(null);
        try {
            tecnicoService.save(model);
            this.response.put("message", "success");
            return new ResponseEntity<>(this.response, HttpStatus.OK);
        } catch (Exception e) {
            this.response.put("message", "error interno");
            return new ResponseEntity<>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @PostMapping(value = "/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable int id) {
        try {
            Optional<Tecnico> optionalTecnico = tecnicoService.findById(id);

            if (optionalTecnico.isPresent()) {
                Tecnico tecnico = optionalTecnico.get();
                tecnico.setEstado(!tecnico.getEstado());
                tecnicoService.save(tecnico);

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
