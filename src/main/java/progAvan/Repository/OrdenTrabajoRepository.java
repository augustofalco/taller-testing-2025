package progAvan.Repository;

import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import progAvan.Model.OrdenTrabajo;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Integer> {

    List<OrdenTrabajo> findByHabilitadoIsTrue();

    List<OrdenTrabajo> findByHabilitadoIsTrue(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE OrdenTrabajo SET habilitado = false WHERE id = :ordenTrabajoId", nativeQuery = true)
    void deshabilitarOrdenTrabajo(Integer ordenTrabajoId);

    @Transactional
    @Modifying
    @Query(value = "SELECT ot.fecha_inicio, a.patente FROM orden_trabajo ot left join auto a on ot.vehiculo_id = a.id left join cliente c on a.cliente_id = :idCliente order by ot.fecha_inicio desc FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    List<Object> ultimaOrdenCliente(int idCliente);

    OrdenTrabajo findFirstByOrderByIdDesc();

    @Transactional
    @Modifying
    @Query(value = "UPDATE detalle_orden_trabajo SET orden_id=:idOrden WHERE id=:idDetalle", nativeQuery = true)
    void setOdenId(int idOrden, int idDetalle);

    @Transactional
    @Modifying
    @Query(value = "SELECT ot.*, a.patente, t.nombre FROM orden_trabajo ot left JOIN auto a ON ot.vehiculo_id=a.id left JOIN tecnico t ON ot.tecnico_id=t.id WHERE ( LOWER(ot.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) OR LOWER(a.patente) LIKE LOWER(CONCAT('%', :descripcion, '%')) OR LOWER(t.nombre) LIKE LOWER(CONCAT('%', :descripcion, '%')) ) and ot.fecha_inicio >= PARSEDATETIME(:fechaInferior, 'yyyyMMdd') and ot.fecha_inicio <= PARSEDATETIME(:fechaSuperior, 'yyyyMMdd')", nativeQuery = true)
    List<OrdenTrabajo> buscarPorAtributo(String descripcion, String fechaInferior, String fechaSuperior);
}