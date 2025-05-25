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

import progAvan.Model.Tecnico;
import progAvan.Repository.TecnicoRepository;
import progAvan.Service.TecnicoService;

@ExtendWith(MockitoExtension.class)
public class TecnicoTestUnitarios {

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private TecnicoService tecnicoService;

    private Tecnico tecnico;

    @BeforeEach
    void setUp() {
        tecnico = new Tecnico();
        tecnico.setId(1);
        tecnico.setNombre("Juan Técnico");
        tecnico.setDni(12345678);
        tecnico.setEstado(true);
    }

    // TC-TECNICO-001
    @Test
    void testSave() {
        when(tecnicoRepository.save(any(Tecnico.class))).thenReturn(tecnico);
        tecnicoService.save(tecnico);
        verify(tecnicoRepository, times(1)).save(tecnico);
    }

    // TC-TECNICO-002
    @Test
    void testFindById() {
        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnico));
        Optional<Tecnico> resultado = tecnicoService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals(tecnico, resultado.get());
        verify(tecnicoRepository, times(1)).findById(1L);
    }

    // TC-TECNICO-003
    @Test
    void testFindByIdNotFound() {
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Tecnico> resultado = tecnicoService.findById(99);
        assertFalse(resultado.isPresent());
        verify(tecnicoRepository, times(1)).findById(99L);
    }

    // TC-TECNICO-004
    @Test
    void testFindAll() {
        List<Tecnico> tecnicos = Arrays.asList(tecnico);
        when(tecnicoRepository.findAll()).thenReturn(tecnicos);
        List<Tecnico> resultado = tecnicoService.findAll();
        assertEquals(1, resultado.size());
        assertEquals(tecnico, resultado.get(0));
        verify(tecnicoRepository, times(1)).findAll();
    }

    // TC-TECNICO-005
    @Test
    void testFindHabilitados() {
        List<Tecnico> tecnicosHabilitados = Arrays.asList(tecnico);
        when(tecnicoRepository.findByEstadoIsTrue()).thenReturn(tecnicosHabilitados);
        List<Tecnico> resultado = tecnicoService.findHabiliitados();
        assertEquals(1, resultado.size());
        assertEquals(tecnico, resultado.get(0));
        verify(tecnicoRepository, times(1)).findByEstadoIsTrue();
    }

    // TC-TECNICO-006
    @Test
    void testGetEstado() {
        assertTrue(tecnico.getEstado());

        tecnico.setEstado(false);
        assertFalse(tecnico.getEstado());
    }

    // TC-TECNICO-007
    @Test
    void testSetEstado() {
        tecnico.setEstado(false);
        assertFalse(tecnico.getEstado());

        tecnico.setEstado(true);
        assertTrue(tecnico.getEstado());
    }
}
