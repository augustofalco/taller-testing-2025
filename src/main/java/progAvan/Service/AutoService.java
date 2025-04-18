package progAvan.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import progAvan.Model.Auto;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.ModeloRepository;

@Service
public class AutoService {

    @Autowired
    AutoRepository autoRepository;

    @Autowired
    ModeloRepository modeloRepository;

    public Page<Auto> findPaginado(Pageable pageable) {
        return autoRepository.findAll(pageable);
    }

    public void save(Auto auto) {
        autoRepository.save(auto);
    }

    public Optional<Auto> findById(int id) {
        return autoRepository.findById(id);
    }

    public List<Auto> findAll() {
        return autoRepository.findAll();
    }

    public List<Auto> findHabilitados() {
        return autoRepository.findByEstadoIsTrue();
    }

    public List<Auto> findPaginado(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<Auto> pagedResult = autoRepository.findByEstadoIsTrue(paging);
        return pagedResult;
    }

    public long longitud() {
        return this.autoRepository.count();
    }

    @Transactional
    public void deshabilitarAuto(Integer autoId) {
        autoRepository.deshabilitarAuto(autoId);
    }

    @Transactional
    public List<Auto> findByPatente(String patente) {
        return autoRepository.findByPatente(patente);
    }

}