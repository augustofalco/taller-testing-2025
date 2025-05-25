package progAvan.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import progAvan.Model.Servicio;
import progAvan.Repository.ServicioRepository;
import progAvan.Service.ServicioService;

@ExtendWith(MockitoExtension.class)
public class ServicioTestUnitarios {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService servicioService;

    private Servicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new Servicio();
        servicio.setId(1);
        servicio.setNombre("Cambio de aceite");
        servicio.setPrecio(5000.0f);
        servicio.setEstado(true);
        servicio.setMinutosestimados(30);
    }

    // TC-SERVICIO-001
    @Test
    void testSave() {
        when(servicioRepository.save(any(Servicio.class))).thenReturn(servicio);
        servicioService.save(servicio);
        verify(servicioRepository, times(1)).save(servicio);
    }

    // TC-SERVICIO-002
    @Test
    void testFindById() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        Optional<Servicio> resultado = servicioService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals(servicio, resultado.get());
        verify(servicioRepository, times(1)).findById(1L);
    }

    // TC-SERVICIO-003
    @Test
    void testFindByIdNotFound() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Servicio> resultado = servicioService.findById(99);
        assertFalse(resultado.isPresent());
        verify(servicioRepository, times(1)).findById(99L);
    }

    // TC-SERVICIO-004
    @Test
    void testFindAll() {
        List<Servicio> servicios = Arrays.asList(servicio);
        when(servicioRepository.findAll()).thenReturn(servicios);
        List<Servicio> resultado = servicioService.findAll();
        assertEquals(1, resultado.size());
        assertEquals(servicio, resultado.get(0));
        verify(servicioRepository, times(1)).findAll();
    }

    // TC-SERVICIO-005
    @Test
    void testFindHabilitados() {
        List<Servicio> serviciosHabilitados = Arrays.asList(servicio);
        when(servicioRepository.findByEstadoIsTrue()).thenReturn(serviciosHabilitados);
        List<Servicio> resultado = servicioService.findHabiliitados();
        assertEquals(1, resultado.size());
        assertEquals(servicio, resultado.get(0));
        verify(servicioRepository, times(1)).findByEstadoIsTrue();
    }

    // TC-SERVICIO-006
    @Test
    void testGetEstado() {
        assertTrue(servicio.getEstado());

        servicio.setEstado(false);
        assertFalse(servicio.getEstado());
    }

    // TC-SERVICIO-007
    @Test
    void testSetEstado() {
        servicio.setEstado(false);
        assertFalse(servicio.getEstado());

        servicio.setEstado(true);
        assertTrue(servicio.getEstado());
    }
}
