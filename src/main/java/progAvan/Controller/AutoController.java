package progAvan.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import progAvan.Model.Auto;

import progAvan.Service.AutoService;

@RestController
@RequestMapping(path = "/auto")
@Api(tags = "AutoController", description = "ABM completo de auto")

public class AutoController {

    @Autowired
    AutoService autoService;

    Map<String, String> response = new HashMap<>();

    @GetMapping(value = "/mostrarpaginado")
    public List<Auto> mostrarPaginado(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return autoService.findPaginado(page, size);
    }

    @GetMapping(value = "/mostrar/{patente}")
    public List<Auto> buscarPorAtributo(@PathVariable String patente) {
        return autoService.findByPatente(patente);
    }

    @GetMapping(value = "/longitud")
    public long longitud() {
        return autoService.longitud();
    }

    @PostMapping(value = "/guardar")
    @ApiOperation(value = "Traer auto")
    public ResponseEntity<Map<String, String>> guardar(@Valid @RequestBody Auto model) {
        try {
            autoService.save(model);
            this.response.put("message", "success");
            return new ResponseEntity<>(this.response, HttpStatus.OK);
        } catch (Exception e) {
            this.response.put("message", "error interno");
            return new ResponseEntity<>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/mostrar")
    public List<Auto> mostrar() {
        return autoService.findAll();
    }

    @GetMapping(value = "/mostrarHabilitados")
    public List<Auto> mostrarHabilitados() {
        return autoService.findHabilitados();
    }

    @PostMapping(value = "/editar/{id}")
    public ResponseEntity<Map<String, String>> actualizar(@PathVariable int id, @Valid @RequestBody Auto model) {
        // Auto auto = autoService.findById(id).orElse(null);
        try {
            autoService.save(model);
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
            Optional<Auto> optionalAuto = autoService.findById(id);

            if (optionalAuto.isPresent()) {
                Auto auto = optionalAuto.get();
                auto.setEstado(false);  // Deshabilitar el auto
                autoService.save(auto);

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