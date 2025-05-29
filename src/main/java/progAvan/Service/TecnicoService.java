package progAvan.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import progAvan.Model.Tecnico;
import progAvan.Repository.TecnicoRepository;
import progAvan.shared.Result;
import progAvan.shared.error.*;

@Service
public class TecnicoService {

    @Autowired
    TecnicoRepository tecnicoRepository;

    public Result<Tecnico> save(Tecnico model) {
        try {
            // Validar datos del técnico
            if (model.getNombre() == null || model.getNombre().trim().isEmpty()) {
                return Result.failure(new ValidationError("El nombre no puede estar vacío"));
            }

            if (model.getDni() <= 0) {
                return Result.failure(new ValidationError("El DNI debe ser un número positivo"));
            }
            // Validar DNI duplicado
            Optional<Tecnico> tecnicoExistente = tecnicoRepository.findByDni(model.getDni());
            if (tecnicoExistente.isPresent() && !tecnicoExistente.get().getId().equals(model.getId())) {
                // Verificar si el técnico existente está archivado (deshabilitado)
                if (!tecnicoExistente.get().getEstado()) {
                    // Pasamos el técnico archivado como dato para que el frontend pueda manejarlo
                    return Result.failure(
                            new ArchivedEntityError(
                                    "Técnico con DNI " + model.getDni() + " ya existe y está archivado."));
                } else {
                    return Result.failure(
                            new DuplicateError("Técnico con DNI " + model.getDni() + " ya existe."));
                }
            }
            // Guardar el técnico
            Tecnico tecnicoGuardado = tecnicoRepository.save(model);
            return Result.success(tecnicoGuardado, "Técnico guardado correctamente");

        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<List<Tecnico>> findAll() {
        try {
            List<Tecnico> tecnicos = tecnicoRepository.findAll();
            return Result.success(tecnicos, "Técnicos recuperados con éxito");
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<Tecnico> findById(long id) {
        try {
            Optional<Tecnico> tecnico = tecnicoRepository.findById(id);
            if (tecnico.isPresent()) {
                return Result.success(tecnico.get(), "Técnico encontrado");
            } else {
                return Result.failure(new NotFoundError("Técnico", String.valueOf(id)));
            }
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<List<Tecnico>> findHabilitados() {
        try {
            List<Tecnico> tecnicosHabilitados = tecnicoRepository.findByEstadoIsTrue();
            return Result.success(tecnicosHabilitados, "Técnicos habilitados encontrados");
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public Result<Tecnico> toggleEstado(long id) {
        try {
            Optional<Tecnico> optionalTecnico = tecnicoRepository.findById(id);
            if (!optionalTecnico.isPresent()) {
                return Result.failure(new NotFoundError("Técnico", String.valueOf(id)));
            }
            Tecnico tecnico = optionalTecnico.get();
            boolean nuevoEstado = !tecnico.getEstado();
            tecnico.setEstado(nuevoEstado);
            Tecnico tecnicoActualizado = tecnicoRepository.save(tecnico);
            String mensaje = nuevoEstado ? "Técnico habilitado correctamente" : "Técnico deshabilitado correctamente";
            return Result.success(tecnicoActualizado, mensaje);
        } catch (Exception e) {
            return Result.failure(new ServerInternalError(e));
        }
    }

    public List<Tecnico> findPaginado(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<Tecnico> pagedResult = tecnicoRepository.findByEstadoIsTrue(paging);
        return pagedResult;
    }

    public long longitud() {
        return this.tecnicoRepository.count();
    }

}
