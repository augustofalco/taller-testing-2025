package progAvan.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import progAvan.Model.DetalleOrdenTrabajo;
import progAvan.Model.OrdenTrabajo;
import progAvan.Model.Servicio;
import progAvan.Repository.DetalleOrdenRepository;
import progAvan.Service.DetalleOrdenService;

@ExtendWith(MockitoExtension.class)
public class DetalleOrdenTrabajoTestUnitarios {

    @Mock
    private DetalleOrdenRepository detalleOrdenRepository;

    @InjectMocks
    private DetalleOrdenService detalleOrdenService;

    private DetalleOrdenTrabajo detalleOrden;
    private OrdenTrabajo orden;
    private Servicio servicio;

    @BeforeEach
    void setUp() {
        orden = new OrdenTrabajo();
        orden.setId(1);

        servicio = new Servicio();
        servicio.setId(1);
        servicio.setNombre("Cambio de aceite");
        servicio.setPrecio(5000.0f);

        detalleOrden = new DetalleOrdenTrabajo();
        detalleOrden.setId(1);
        detalleOrden.setDescripcion("Cambio de aceite sintético");
        detalleOrden.setCantidad(1);
        detalleOrden.setServicio(servicio);
        detalleOrden.setSubtotal(5000.0);
        detalleOrden.setEstado(true);
        detalleOrden.setMinutosRealizados(25);
        detalleOrden.setOrden(orden);
    }

    // TC-DETALLE-ORDEN-001
    @Test
    void testBuscarPorOrden() {
        List<DetalleOrdenTrabajo> detalles = Arrays.asList(detalleOrden);
        when(detalleOrdenRepository.findByOrden(orden)).thenReturn(detalles);

        List<DetalleOrdenTrabajo> resultado = detalleOrdenService.buscarPorOrden(orden);
        assertEquals(1, resultado.size());
        assertEquals(detalleOrden, resultado.get(0));
        verify(detalleOrdenRepository, times(1)).findByOrden(orden);
    }

    // TC-DETALLE-ORDEN-002
    @ParameterizedTest(name = "#{index} - La cantidad {0} debería ser válida")
    @ValueSource(ints = { 0, 1, 5, 10, 100, 1000 })
    public void testCantidadesValidas(int cantidad) {
        assertTrue(detalleOrden.cantidadValida(cantidad));
    }

    // TC-DETALLE-ORDEN-003
    @ParameterizedTest(name = "#{index} - La cantidad {0} no debería ser válida")
    @ValueSource(ints = { -1, -5, -10, -100, -1000 })
    public void testCantidadesInvalidas(int cantidad) {
        assertFalse(detalleOrden.cantidadValida(cantidad));
    }

    // TC-DETALLE-ORDEN-004
    @Test
    void testGetEstado() {
        assertTrue(detalleOrden.getEstado());

        detalleOrden.setEstado(false);
        assertFalse(detalleOrden.getEstado());
    }

    // TC-DETALLE-ORDEN-005
    @Test
    void testSetEstado() {
        detalleOrden.setEstado(false);
        assertFalse(detalleOrden.getEstado());

        detalleOrden.setEstado(true);
        assertTrue(detalleOrden.getEstado());
    }

    // TC-DETALLE-ORDEN-006
    @Test
    void testGetterAndSetters() {
        DetalleOrdenTrabajo detalle = new DetalleOrdenTrabajo();

        detalle.setId(2);
        assertEquals(2, detalle.getId());

        detalle.setDescripcion("Descripción de prueba");
        assertEquals("Descripción de prueba", detalle.getDescripcion());

        detalle.setCantidad(3);
        assertEquals(3, detalle.getCantidad());

        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setId(2);
        detalle.setServicio(nuevoServicio);
        assertEquals(nuevoServicio, detalle.getServicio());

        detalle.setSubtotal(7500.0);
        assertEquals(7500.0, detalle.getSubtotal());

        detalle.setEstado(true);
        assertTrue(detalle.getEstado());

        detalle.setMinutosRealizados(45);
        assertEquals(45, detalle.getMinutosRealizados());

        OrdenTrabajo nuevaOrden = new OrdenTrabajo();
        nuevaOrden.setId(2);
        detalle.setOrden(nuevaOrden);
        assertEquals(nuevaOrden, detalle.getOrden());
    }
}
