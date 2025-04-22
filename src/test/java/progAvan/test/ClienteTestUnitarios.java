package progAvan.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import progAvan.Model.Cliente;
import progAvan.Repository.ClienteRepository;
import progAvan.Service.ClienteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteTestUnitarios {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clientePrueba;
    private Cliente clienteInvalido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Cliente válido para pruebas
        clientePrueba = new Cliente();
        clientePrueba.setId(1);
        clientePrueba.setDni(12345678);
        clientePrueba.setNombre("Cliente Prueba");
        clientePrueba.setDireccion("Calle Falsa 123");
        clientePrueba.setTelefono("11-2222-3333");
        clientePrueba.setEmail("cliente@test.com");
        clientePrueba.setEstado(true);
        clientePrueba.setObservaciones("Cliente de prueba");

        // Cliente inválido para pruebas negativas
        clienteInvalido = new Cliente();
        // No configuramos todos los campos requeridos
        clienteInvalido.setId(2);
        clienteInvalido.setEstado(true);
    }

    // TC-CLIENTE-001
    @Test
    void testSaveClienteExitoso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clientePrueba);

        clienteService.save(clientePrueba);

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // TC-CLIENTE-002
    @Test
    void testSaveClienteSinDatosObligatorios() {
        when(clienteRepository.save(clienteInvalido))
                .thenThrow(new RuntimeException("Error al guardar cliente sin datos obligatorios"));

        assertThrows(RuntimeException.class, () -> {
            clienteService.save(clienteInvalido);
        });
    }

    // TC-CLIENTE-003
    @Test
    void testFindById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));

        Optional<Cliente> resultado = clienteService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(clientePrueba.getId(), resultado.get().getId());
    }

    // TC-CLIENTE-004
    @Test
    void testFindByIdInexistente() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.findById(999L);

        assertFalse(resultado.isPresent());
    }

    // TC-CLIENTE-005
    @Test
    void testFindHabilitados() {
        List<Cliente> clientesHabilitados = new ArrayList<>();
        clientesHabilitados.add(clientePrueba);

        when(clienteRepository.findByEstadoIsTrue()).thenReturn(clientesHabilitados);

        List<Cliente> resultado = clienteService.findHabilitados();

        assertEquals(1, resultado.size());
        assertEquals(clientePrueba.getId(), resultado.get(0).getId());
    }

    // TC-CLIENTE-006
    @Test
    void testFindAll() {
        List<Cliente> todosLosClientes = new ArrayList<>();
        todosLosClientes.add(clientePrueba);

        Cliente clienteDeshabilitado = new Cliente();
        clienteDeshabilitado.setId(3);
        clienteDeshabilitado.setDni(87654321);
        clienteDeshabilitado.setNombre("Cliente Deshabilitado");
        clienteDeshabilitado.setEstado(false);
        todosLosClientes.add(clienteDeshabilitado);

        when(clienteRepository.findAll()).thenReturn(todosLosClientes);

        List<Cliente> resultado = clienteService.findAll();

        assertEquals(2, resultado.size());
    }

    // TC-CLIENTE-007
    @Test
    void testActualizarUltimaFechaVisita() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clientePrueba);

        clienteService.actualizarUltimaFechaVisita(1L);

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        assertNotNull(clientePrueba.getFecha_ultima_actualizacion());
    }

    // TC-CLIENTE-008
    @Test
    void testActualizarUltimaFechaVisitaClienteInexistente() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        clienteService.actualizarUltimaFechaVisita(999L);

        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, times(0)).save(any(Cliente.class));
    }

    // TC-CLIENTE-009
    @Test
    void testSaveClienteConEmailDuplicado() {
        when(clienteRepository.save(any(Cliente.class)))
                .thenThrow(new RuntimeException("Duplicate entry for key 'email'"));

        Cliente clienteConEmailDuplicado = new Cliente();
        clienteConEmailDuplicado.setDni(98765432);
        clienteConEmailDuplicado.setNombre("Cliente Nuevo");
        clienteConEmailDuplicado.setDireccion("Otra Calle 456");
        clienteConEmailDuplicado.setTelefono("11-4444-5555");
        clienteConEmailDuplicado.setEmail("cliente@test.com"); // Email duplicado
        clienteConEmailDuplicado.setEstado(true);

        assertThrows(RuntimeException.class, () -> {
            clienteService.save(clienteConEmailDuplicado);
        });
    }
}
