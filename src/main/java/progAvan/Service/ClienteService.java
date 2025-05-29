package progAvan.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progAvan.Model.Cliente;
import progAvan.Repository.ClienteRepository;
import progAvan.shared.Result;
import progAvan.shared.error.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    public Result<Cliente> save(Cliente cliente) {
        try {
            // Validar datos del cliente
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                return Result.failure(new ValidationError("nombre", "El nombre no puede estar vacío"));
            }

            if (cliente.getDni() <= 0) {
                return Result.failure(new ValidationError("dni", "El DNI debe ser un número positivo"));
            } // Validar DNI duplicado (excepto en actualizaciones del mismo cliente)
            Optional<Cliente> clienteExistente = clienteRepository.findByDni(cliente.getDni());
            if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(cliente.getId())) {
                // Verificar si el cliente existente está archivado (deshabilitado)
                if (!clienteExistente.get().getEstado()) {
                    // Pasamos el cliente archivado como dato para que el frontend pueda manejarlo
                    return Result.failure(
                            new ArchivedEntityError("Cliente", "DNI", String.valueOf(cliente.getDni())),
                            clienteExistente.get());
                } else {
                    return Result.failure(
                            new DuplicateError("Cliente", "DNI", String.valueOf(cliente.getDni())));
                }
            } // Si todas las validaciones pasan, guardar el cliente
            Cliente clienteGuardado = clienteRepository.save(cliente);
            return Result.success(clienteGuardado, "Cliente guardado correctamente");

        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<Cliente> findById(long id) {
        try {
            Optional<Cliente> cliente = clienteRepository.findById(id);
            if (cliente.isPresent()) {
                return Result.success(cliente.get(), "Cliente encontrado");
            } else {
                return Result.failure(new NotFoundError("Cliente", String.valueOf(id)));
            }
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<List<Cliente>> findHabilitados() {
        try {
            List<Cliente> clientes = clienteRepository.findByEstadoIsTrue();
            return Result.success(clientes, "Clientes habilitados recuperados con éxito");
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<Cliente> toggleEstado(long id) {
        try {
            Optional<Cliente> optionalCliente = clienteRepository.findById(id);

            if (!optionalCliente.isPresent()) {
                return Result.failure(new NotFoundError("Cliente", String.valueOf(id)));
            }

            Cliente cliente = optionalCliente.get();
            boolean nuevoEstado = !cliente.getEstado();
            cliente.setEstado(nuevoEstado);

            Cliente clienteActualizado = clienteRepository.save(cliente);

            String mensaje = nuevoEstado ? "Cliente habilitado correctamente" : "Cliente deshabilitado correctamente";
            return Result.success(clienteActualizado, mensaje);

        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    // paginación
    public List<Cliente> findPaginado(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<Cliente> pagedResult = clienteRepository.findByEstadoIsTrue(paging);
        return pagedResult;
    }

    public long longitud() {
        return this.clienteRepository.count();
    }

    public List<Cliente> buscarPorAtributo(String nombre) {
        return clienteRepository.buscarPorAtributo(nombre);
    }

    public Result<List<Cliente>> findAll() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            return Result.success(clientes, "Clientes recuperados con éxito");
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public void actualizarUltimaFechaVisita(long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);

        if (cliente != null) {
            // Actualiza la fecha_ultima_visita con la fecha y hora actual
            cliente.setFecha_ultima_actualizacion(LocalDateTime.now());

            // Guarda los cambios en el repositorio
            cliente = clienteRepository.save(cliente);
        }
    }
}
