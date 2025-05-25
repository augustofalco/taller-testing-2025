package progAvan.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import progAvan.Model.Marca;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.MarcaRepository;
import progAvan.Repository.ModeloRepository;

@Service
public class MarcaService {

    @Autowired
    MarcaRepository marcaRepository;

    @Autowired
    ModeloRepository modeloRepository;

    @Autowired
    AutoRepository autoRepository;

    public void save(Marca model) {
        if (model.getNombre() == null || model.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca no puede ser nulo o vacío");
        }

        Optional<Marca> marcaExistente = marcaRepository.findByNombre(model.getNombre());
        if (marcaExistente.isPresent() && !marcaExistente.get().getId().equals(model.getId())) {
            Marca marcaEncontrada = marcaExistente.get();
            if (marcaEncontrada.getEstado()) {
                throw new IllegalStateException("La marca ya existe");
            } else {
                throw new IllegalStateException("La marca ya existe pero esta archivada");
            }
        }

        marcaRepository.save(model);
    }

    public Optional<Marca> findById(long id) {
        return marcaRepository.findById(id);
    }

    public List<Marca> findAll() {
        return marcaRepository.findAll();
    }

    public List<Marca> findHabiliitados() {
        return marcaRepository.findByEstadoIsTrue();
    }

    public List<Marca> findPaginado(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<Marca> pagedResult = marcaRepository.findByEstadoIsTrue(paging);
        return pagedResult;
    }

    @Transactional
    public void deshabilitarMarcaYRelacionados(Integer marcaId) {
        marcaRepository.deshabilitarMarca(marcaId);
        modeloRepository.deshabilitarModelosPorMarcaId(marcaId);
        autoRepository.deshabilitarAutosPorMarcaId(marcaId);
    }

    public long longitud() {
        return this.marcaRepository.count();
    }

}
