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

import progAvan.Model.Auto;
import progAvan.Model.Cliente;
import progAvan.Model.Modelo;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.ModeloRepository;
import progAvan.Service.AutoService;

@ExtendWith(MockitoExtension.class)
public class AutoTestUnitarios {

    @Mock
    private AutoRepository autoRepository;

    @Mock
    private ModeloRepository modeloRepository;

    @InjectMocks
    private AutoService autoService;

    private Auto auto;
    private Modelo modelo;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        modelo = new Modelo();
        modelo.setId(1);
        modelo.setNombre("Corolla");
        modelo.setEstado(true);

        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Juan Pérez");
        cliente.setEstado(true);

        auto = new Auto();
        auto.setId(1);
        auto.setPatente("AA123BB");
        auto.setAnio("2022");
        auto.setEstado(true);
        auto.setModelo(modelo);
        auto.setCliente(cliente);
    }

    // TC-AUTO-001
    @Test
    void testSave() {
        when(autoRepository.save(any(Auto.class))).thenReturn(auto);
        autoService.save(auto);
        verify(autoRepository, times(1)).save(auto);
    }

    // TC-AUTO-002
    @Test
    void testFindById() {
        when(autoRepository.findById(1)).thenReturn(Optional.of(auto));
        Optional<Auto> resultado = autoService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals(auto, resultado.get());
        verify(autoRepository, times(1)).findById(1);
    }

    // TC-AUTO-003
    @Test
    void testFindByIdNotFound() {
        when(autoRepository.findById(99)).thenReturn(Optional.empty());
        Optional<Auto> resultado = autoService.findById(99);
        assertFalse(resultado.isPresent());
        verify(autoRepository, times(1)).findById(99);
    }

    // TC-AUTO-004
    @Test
    void testFindAll() {
        List<Auto> autos = Arrays.asList(auto);
        when(autoRepository.findAll()).thenReturn(autos);
        List<Auto> resultado = autoService.findAll();
        assertEquals(1, resultado.size());
        assertEquals(auto, resultado.get(0));
        verify(autoRepository, times(1)).findAll();
    }

    // TC-AUTO-005
    @Test
    void testFindHabilitados() {
        List<Auto> autosHabilitados = Arrays.asList(auto);
        when(autoRepository.findByEstadoIsTrue()).thenReturn(autosHabilitados);
        List<Auto> resultado = autoService.findHabilitados();
        assertEquals(1, resultado.size());
        assertEquals(auto, resultado.get(0));
        verify(autoRepository, times(1)).findByEstadoIsTrue();
    }

    // TC-AUTO-006
    @Test
    void testDeshabilitarAuto() {
        doNothing().when(autoRepository).deshabilitarAuto(1);
        autoService.deshabilitarAuto(1);
        verify(autoRepository, times(1)).deshabilitarAuto(1);
    }

    // TC-AUTO-007
    @Test
    void testFindByPatente() {
        List<Auto> autosPorPatente = Arrays.asList(auto);
        when(autoRepository.findByPatente("AA123BB")).thenReturn(autosPorPatente);
        List<Auto> resultado = autoService.findByPatente("AA123BB");
        assertEquals(1, resultado.size());
        assertEquals(auto, resultado.get(0));
        verify(autoRepository, times(1)).findByPatente("AA123BB");
    }

    // TC-AUTO-008
    @Test
    void testFindByPatenteNoResultados() {
        when(autoRepository.findByPatente("ZZ999ZZ")).thenReturn(Collections.emptyList());
        List<Auto> resultado = autoService.findByPatente("ZZ999ZZ");
        assertTrue(resultado.isEmpty());
        verify(autoRepository, times(1)).findByPatente("ZZ999ZZ");
    }

    // TC-AUTO-009
    @Test
    public void testPatenteValida() {
        // TC-PATENTE-001
        assertTrue(auto.validarPatente("AB123CD"));
        // TC-PATENTE-002
        assertTrue(auto.validarPatente("XY987ZW"));
        // TC-PATENTE-003
        assertTrue(auto.validarPatente("GSS456"));
    }

    // TC-AUTO-010
    @Test
    public void testPatenteInvalida() {
        // TC-PATENTE-004
        assertFalse(auto.validarPatente("123ABC")); // Números en lugar de letras
        // TC-PATENTE-005
        assertFalse(auto.validarPatente("AB12CD")); // Longitud incorrecta
        // TC-PATENTE-006
        assertFalse(auto.validarPatente("ABC-123")); // Caracteres especiales
    }
}
