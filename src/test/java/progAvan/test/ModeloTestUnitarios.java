package progAvan.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import progAvan.Model.Marca;
import progAvan.Model.Modelo;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.MarcaRepository;
import progAvan.Repository.ModeloRepository;
import progAvan.Service.ModeloService;

@ExtendWith(MockitoExtension.class)
public class ModeloTestUnitarios {

    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private AutoRepository autoRepository;

    @InjectMocks
    private ModeloService modeloService;

    private Modelo modelo;
    private Marca marca;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setId(1);
        marca.setNombre("Toyota");

        modelo = new Modelo();
        modelo.setId(1);
        modelo.setNombre("Corolla");
        modelo.setEstado(true);
        modelo.setMarca(marca);
    }

    @Test
    void testSave() {
        modeloService.save(modelo);
        verify(modeloRepository, times(1)).save(modelo);
    }

    @Test
    void testFindById() {
        when(modeloRepository.findById(1)).thenReturn(Optional.of(modelo));

        Optional<Modelo> result = modeloService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Corolla", result.get().getNombre());
        assertEquals("Toyota", result.get().getMarca().getNombre());
        verify(modeloRepository, times(1)).findById(1);
    }

    @Test
    void testFindAll() {
        when(modeloRepository.findAll()).thenReturn(List.of(modelo));

        List<Modelo> modelos = modeloService.findAll();

        assertEquals(1, modelos.size());
        verify(modeloRepository, times(1)).findAll();
    }

    @Test
    void testFindHabilitados() {
        when(modeloRepository.findByEstadoIsTrue()).thenReturn(List.of(modelo));

        List<Modelo> modelos = modeloService.findHabiliitados();

        assertEquals(1, modelos.size());
        assertTrue(modelos.get(0).getEstado());
        verify(modeloRepository, times(1)).findByEstadoIsTrue();
    }

    @Test
    void testFindModelosXMarca() {
        when(modeloRepository.findByMarca(1)).thenReturn(List.of(modelo));

        List<Modelo> modelos = modeloService.findModelosXMarca(1);

        assertEquals(1, modelos.size());
        assertEquals("Toyota", modelos.get(0).getMarca().getNombre());
        verify(modeloRepository, times(1)).findByMarca(1);
    }

    @Test
    void testMostrar() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Modelo> page = new PageImpl<>(List.of(modelo));
        when(modeloRepository.findAll(pageable)).thenReturn(page);

        Page<Modelo> result = modeloService.mostrar(pageable);

        assertEquals(1, result.getContent().size());
        verify(modeloRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDeshabilitarModeloYRelacionados() {
        doNothing().when(modeloRepository).deshabilitarModelo(1);
        doNothing().when(autoRepository).deshabilitarAutosPorModeloId(1);

        modeloService.deshabilitarModeloYRelacionados(1);

        verify(modeloRepository, times(1)).deshabilitarModelo(1);
        verify(autoRepository, times(1)).deshabilitarAutosPorModeloId(1);
    }
}
