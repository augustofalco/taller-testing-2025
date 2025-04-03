package progAvan.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import progAvan.Model.Marca;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.MarcaRepository;
import progAvan.Repository.ModeloRepository;
import progAvan.Service.MarcaService;

@ExtendWith(MockitoExtension.class)
public class MarcaTestUnitarios {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private AutoRepository autoRepository;

    @InjectMocks
    private MarcaService marcaService;

    private Marca marca;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setId(1);
        marca.setNombre("Toyota");
        marca.setEstado(true);
    }

    @Test
    void testFindById() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        Optional<Marca> result = marcaService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Toyota", result.get().getNombre());
        verify(marcaRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        marcaService.save(marca);
        verify(marcaRepository, times(1)).save(marca);
    }

    @Test
    void testFindAll() {
        when(marcaRepository.findAll()).thenReturn(Arrays.asList(marca));

        List<Marca> marcas = marcaService.findAll();

        assertFalse(marcas.isEmpty());
        assertEquals(1, marcas.size());
        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void testFindHabilitados() {
        when(marcaRepository.findByEstadoIsTrue()).thenReturn(Arrays.asList(marca));

        List<Marca> marcas = marcaService.findHabiliitados();

        assertFalse(marcas.isEmpty());
        assertEquals(1, marcas.size());
        assertTrue(marcas.get(0).getEstado());
        verify(marcaRepository, times(1)).findByEstadoIsTrue();
    }

    @Test
    void testDeshabilitarMarcaYRelacionados() {
        doNothing().when(marcaRepository).deshabilitarMarca(1);
        doNothing().when(modeloRepository).deshabilitarModelosPorMarcaId(1);
        doNothing().when(autoRepository).deshabilitarAutosPorMarcaId(1);

        marcaService.deshabilitarMarcaYRelacionados(1);

        verify(marcaRepository, times(1)).deshabilitarMarca(1);
        verify(modeloRepository, times(1)).deshabilitarModelosPorMarcaId(1);
        verify(autoRepository, times(1)).deshabilitarAutosPorMarcaId(1);
    }
}
