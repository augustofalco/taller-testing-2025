package progAvan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import progAvan.Model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByEstadoIsTrue();

    List<Marca> findByEstadoIsTrue(Pageable pageable);

    Optional<Marca> findByNombre(String nombre);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Marca SET estado = false WHERE id = :marcaId", nativeQuery = true)
    void deshabilitarMarca(Integer marcaId);

}