package progAvan.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import progAvan.Model.Cliente;
import progAvan.Service.ClienteService;
import progAvan.shared.Result;

@RestController
@RequestMapping(path = "/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Value("${path_general}")
    String path;

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @PostMapping(value = "/guardar")
    public ResponseEntity<Result<Cliente>> guardar(@RequestBody Cliente model) {
        try {
            Result<Cliente> result = clienteService.save(model);
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                // Determinar el código de estado HTTP según el tipo de error
                HttpStatus status = HttpStatus.BAD_REQUEST;

                if (result.getError() instanceof progAvan.shared.error.ArchivedEntityError) {
                    // Para entidades archivadas, seguimos usando BAD_REQUEST pero podríamos usar
                    // otro código si lo deseamos
                    status = HttpStatus.CONFLICT; // 409 Conflict puede ser más apropiado para este caso
                } else if (result.getError() instanceof progAvan.shared.error.ValidationError) {
                    status = HttpStatus.BAD_REQUEST; // 400 Bad Request
                } else if (result.getError() instanceof progAvan.shared.error.NotFoundError) {
                    status = HttpStatus.NOT_FOUND; // 404 Not Found
                }

                return ResponseEntity
                        .status(status)
                        .body(result);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.failure(new progAvan.shared.error.ServerInternalError(e)));
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrar")
    public ResponseEntity<?> mostrar() {
        Result<List<Cliente>> result = clienteService.findAll();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getData());
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result.getMessage());
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrarHabilitados")
    public ResponseEntity<?> mostrarHabilitados() {
        Result<List<Cliente>> result = clienteService.findHabilitados();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getData());
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result.getMessage());
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @PostMapping(value = "/editar/{id}")
    public ResponseEntity<Result<Cliente>> actualizar(@PathVariable int id, @RequestBody Cliente model) {
        try {
            // Verificar que el cliente exista
            Result<Cliente> clienteResult = clienteService.findById(id);
            if (!clienteResult.isSuccess()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Result.failure(new progAvan.shared.error.NotFoundError("Cliente", String.valueOf(id))));
            }

            // Verificar que el ID coincide con el modelo
            if (model.getId() != id) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Result.failure(new progAvan.shared.error.ValidationError("ID",
                                "El ID de la URL no coincide con el ID del cliente")));
            }
            Result<Cliente> result = clienteService.save(model);
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(result);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.failure(new progAvan.shared.error.ServerInternalError(e)));
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @PostMapping(value = "/eliminar/{id}")
    public ResponseEntity<Result<Cliente>> eliminar(@PathVariable int id) {
        try {
            Result<Cliente> result = clienteService.toggleEstado(id);

            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(result);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.failure(new progAvan.shared.error.ServerInternalError(e)));
        }
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrarpaginado")
    public List<Cliente> mostrarPaginado(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return clienteService.findPaginado(page, size);
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/longitud")
    public long longitud() {
        return clienteService.longitud();
    }

    @CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 3600)
    @GetMapping(value = "/mostrar/{nombre}")
    public List<Cliente> buscarPorAtributo(@PathVariable String nombre) {
        return clienteService.buscarPorAtributo(nombre);
    }
}
