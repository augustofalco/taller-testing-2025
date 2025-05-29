package progAvan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import progAvan.Model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEstadoIsTrue();

    List<Cliente> findByEstadoIsTrue(Pageable pageable);

    // Método para buscar cliente por DNI
    Optional<Cliente> findByDni(int dni);

    // boolean existsByNombre();

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM Cliente where LOWER(nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))", nativeQuery = true)
    List<Cliente> buscarPorAtributo(String nombre);
}