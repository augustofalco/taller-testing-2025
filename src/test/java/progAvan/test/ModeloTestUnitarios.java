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
        doAnswer(invocation -> {
            Modelo modelo = invocation.getArgument(0);
            if (modelo.getNombre() == null || modelo.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del modelo no puede ser nulo o vacío");
            }
            if (modelo.getMarca() == null) {
                throw new IllegalArgumentException("La marca del modelo no puede ser nula");
            }
            return null;
        }).when(modeloRepository).save(any(Modelo.class));

        modeloService.save(modelo);
        verify(modeloRepository, times(1)).save(modelo);
    }

    @Test
    void testSaveModeloNombreVacio() {
        Modelo modeloNombreVacio = new Modelo();
        modeloNombreVacio.setId(2);
        modeloNombreVacio.setNombre("  "); // Espacios vacíos
        modeloNombreVacio.setEstado(true);
        modeloNombreVacio.setMarca(marca);

        assertThrows(IllegalArgumentException.class, () -> {
            modeloService.save(modeloNombreVacio);
        });
    }

    @Test
    void testSaveModeloConMarcaNula() {
        Modelo modeloSinMarca = new Modelo();
        modeloSinMarca.setId(3);
        modeloSinMarca.setNombre("Etios");
        modeloSinMarca.setEstado(true);
        modeloSinMarca.setMarca(null); // Marca no asignada

        assertThrows(IllegalArgumentException.class, () -> {
            modeloService.save(modeloSinMarca);
        });
    }

    @Test
    void testSaveModeloDuplicado() {
        when(modeloRepository.findByNombre("Corolla")).thenReturn(Optional.of(modelo));

        Modelo modeloDuplicado = new Modelo();
        modeloDuplicado.setNombre("Corolla");
        modeloDuplicado.setEstado(true);
        modeloDuplicado.setMarca(marca);

        assertThrows(IllegalArgumentException.class, () -> {
            modeloService.save(modeloDuplicado);
        });

        verify(modeloRepository, times(1)).findByNombre("Corolla");
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

        // Corregido el método con la ortografía correcta
        List<Modelo> modelos = modeloService.findHabilitados();

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
