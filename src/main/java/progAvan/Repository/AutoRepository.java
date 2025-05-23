package progAvan.Repository;

import progAvan.Model.Auto;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Integer> {

    List<Auto> findByEstadoIsTrue();

    List<Auto> findByEstadoIsTrue(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Auto SET estado = false WHERE id = :autoId", nativeQuery = true)
    void deshabilitarAuto(Integer autoId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Auto SET estado = false WHERE modelo_id IN (SELECT id FROM Modelo WHERE marca_id = :marcaId)", nativeQuery = true)
    void deshabilitarAutosPorMarcaId(Integer marcaId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Auto SET estado = false WHERE modelo_id = :modeloId", nativeQuery = true)
    void deshabilitarAutosPorModeloId(Integer modeloId);

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM Auto WHERE LOWER(patente) LIKE LOWER(CONCAT('%', :patente, '%'))", nativeQuery = true)
    List<Auto> findByPatente(String patente);
}