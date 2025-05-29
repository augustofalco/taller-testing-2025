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
import progAvan.shared.Result;
import progAvan.shared.error.*;

@ExtendWith(MockitoExtension.class)
public class TecnicoTestUnitarios {

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private TecnicoService tecnicoService;

    private Tecnico tecnicoPrueba;
    Tecnico tecnicoDeshabilitado = new Tecnico();

    @BeforeEach
    void setUp() {
        tecnicoPrueba = new Tecnico();
        tecnicoPrueba.setId(1);
        tecnicoPrueba.setNombre("Juan Técnico");
        tecnicoPrueba.setDni(12345678);
        tecnicoPrueba.setEstado(true);

        // tecnico deshabilitado para pruebas
        tecnicoDeshabilitado.setId(3);
        tecnicoDeshabilitado.setNombre("Técnico Deshabilitado");
        tecnicoDeshabilitado.setDni(87654321);
        tecnicoDeshabilitado.setEstado(false);
    }

    // TC-TECNICO-001
    @Test
    void testSaveTecnicoExitoso() {
        when(tecnicoRepository.save(any(Tecnico.class))).thenReturn(tecnicoPrueba);
        when(tecnicoRepository.findByDni(tecnicoPrueba.getDni())).thenReturn(Optional.empty());

        Result<Tecnico> resultado = tecnicoService.save(tecnicoPrueba);

        assertTrue(resultado.isSuccess());
        assertEquals(tecnicoPrueba, resultado.getData());
        assertEquals(tecnicoPrueba.getId(), resultado.getData().getId());
        verify(tecnicoRepository, times(1)).save(any(Tecnico.class));
    }

    // TC-TECNICO-002
    @Test
    void testSaveTecnicoConDniDuplicado() {
        Tecnico tecnicoConDniDuplicado = new Tecnico();
        tecnicoConDniDuplicado.setId(null);
        tecnicoConDniDuplicado.setNombre("Técnico Duplicado");
        tecnicoConDniDuplicado.setDni(12345678); // Mismo DNI que el existente
        tecnicoConDniDuplicado.setEstado(true);

        when(tecnicoRepository.findByDni(tecnicoPrueba.getDni())).thenReturn(Optional.of(tecnicoPrueba));

        Result<Tecnico> resultado = tecnicoService.save(tecnicoConDniDuplicado);

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.isFailure());
        assertTrue(resultado.getError() instanceof DuplicateError);
        assertEquals("ERR-DUP", resultado.getError().getCode());
    }

    // TC-TECNICO-003
    @Test
    void testFindById() {
        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnicoPrueba));
        Result<Tecnico> resultado = tecnicoService.findById(1L);

        assertTrue(resultado.isSuccess());
        assertNotNull(resultado.getData());
        assertEquals(tecnicoPrueba.getId(), resultado.getData().getId());
    }

    // TC-TECNICO-004
    @Test
    void testFindByIdNotFound() {
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.empty());
        Result<Tecnico> resultado = tecnicoService.findById(99);
        assertFalse(resultado.isSuccess());
        assertTrue(resultado.isFailure());
        assertTrue(resultado.getError() instanceof NotFoundError);
    }

    // TC-TECNICO-005
    @Test
    void testFindAll() {
        List<Tecnico> tecnicos = new ArrayList<>();
        tecnicos.add(tecnicoPrueba);
        tecnicos.add(tecnicoDeshabilitado);

        when(tecnicoRepository.findAll()).thenReturn(tecnicos);

        Result<List<Tecnico>> resultado = tecnicoService.findAll();

        assertTrue(resultado.isSuccess());
        assertEquals(2, resultado.getData().size());

    }

    // TC-TECNICO-006
    @Test
    void testFindHabilitados() {
        List<Tecnico> tecnicosHabilitados = new ArrayList<>();
        tecnicosHabilitados.add(tecnicoPrueba);

        when(tecnicoRepository.findByEstadoIsTrue()).thenReturn(tecnicosHabilitados);

        Result<List<Tecnico>> resultado = tecnicoService.findHabilitados();

        assertTrue(resultado.isSuccess());
        assertEquals(1, resultado.getData().size());
        assertEquals(tecnicoPrueba.getId(), resultado.getData().get(0).getId());
    }

}
