package progAvan.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import progAvan.Model.Cliente;
import progAvan.Repository.ClienteRepository;
import progAvan.Service.ClienteService;
import progAvan.shared.Result;
import progAvan.shared.error.*;

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
        when(clienteRepository.findByDni(clientePrueba.getDni())).thenReturn(Optional.empty());

        Result<Cliente> resultado = clienteService.save(clientePrueba);

        assertTrue(resultado.isSuccess());
        assertNotNull(resultado.getData());
        assertEquals(clientePrueba.getId(), resultado.getData().getId());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // TC-CLIENTE-002
    @Test
    void testSaveClienteSinDatosObligatorios() {
        // Configuramos el cliente inválido sin nombre
        clienteInvalido.setNombre(null);

        // No necesitamos configurar el mock para el repositorio ya que la validación
        // ocurre antes

        Result<Cliente> resultado = clienteService.save(clienteInvalido);

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.isFailure());
        assertNotNull(resultado.getError());
        assertTrue(resultado.getError() instanceof ValidationError);
    }

    // TC-CLIENTE-003
    @Test
    void testFindById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));

        Result<Cliente> resultado = clienteService.findById(1L);

        assertTrue(resultado.isSuccess());
        assertNotNull(resultado.getData());
        assertEquals(clientePrueba.getId(), resultado.getData().getId());
    }

    // TC-CLIENTE-004
    @Test
    void testFindByIdInexistente() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        Result<Cliente> resultado = clienteService.findById(999L);

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.isFailure());
        assertTrue(resultado.getError() instanceof NotFoundError);
    }

    // TC-CLIENTE-005
    @Test
    void testFindHabilitados() {
        List<Cliente> clientesHabilitados = new ArrayList<>();
        clientesHabilitados.add(clientePrueba);

        when(clienteRepository.findByEstadoIsTrue()).thenReturn(clientesHabilitados);

        Result<List<Cliente>> resultado = clienteService.findHabilitados();

        assertTrue(resultado.isSuccess());
        assertEquals(1, resultado.getData().size());
        assertEquals(clientePrueba.getId(), resultado.getData().get(0).getId());
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

        Result<List<Cliente>> resultado = clienteService.findAll();

        assertTrue(resultado.isSuccess());
        assertEquals(2, resultado.getData().size());
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
    void testToggleEstado() {
        // Preparar un cliente activo para desactivar
        Cliente clienteActivo = new Cliente();
        clienteActivo.setId(7);
        clienteActivo.setNombre("Cliente Activo");
        clienteActivo.setDni(11223344);
        clienteActivo.setEstado(true);

        // El cliente después de desactivarlo
        Cliente clienteDesactivado = new Cliente();
        clienteDesactivado.setId(7);
        clienteDesactivado.setNombre("Cliente Activo");
        clienteDesactivado.setDni(11223344);
        clienteDesactivado.setEstado(false);

        when(clienteRepository.findById(7L)).thenReturn(Optional.of(clienteActivo));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteDesactivado);

        Result<Cliente> resultado = clienteService.toggleEstado(7L);

        assertTrue(resultado.isSuccess());
        assertFalse(resultado.getData().getEstado()); // Verifica que el estado cambió a false
        assertEquals("Cliente deshabilitado correctamente", resultado.getMessage());
    }

    // TC-CLIENTE-010
    @Test
    void testSaveClienteConDniDuplicado() { // Preparamos un cliente existente con el mismo DNI
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(5);
        clienteExistente.setDni(98765432);
        clienteExistente.setNombre("Cliente Existente");
        clienteExistente.setEstado(true);

        Cliente clienteConDniDuplicado = new Cliente();
        clienteConDniDuplicado.setId(null); // No tiene ID aún (nuevo cliente)
        clienteConDniDuplicado.setDni(98765432); // DNI duplicado
        clienteConDniDuplicado.setNombre("Cliente Nuevo");
        clienteConDniDuplicado.setDireccion("Otra Calle 456");
        clienteConDniDuplicado.setTelefono("11-4444-5555");
        clienteConDniDuplicado.setEmail("nuevo@test.com");
        clienteConDniDuplicado.setEstado(true);

        when(clienteRepository.findByDni(98765432)).thenReturn(Optional.of(clienteExistente));

        // Ejecutar
        Result<Cliente> resultado = clienteService.save(clienteConDniDuplicado);

        // Verificar
        assertFalse(resultado.isSuccess());
        assertTrue(resultado.isFailure());
        assertTrue(resultado.getError() instanceof DuplicateError);
        assertEquals("ERR-DUP", resultado.getError().getCode());
    }

    // TC-CLIENTE-011
    @Test
    void testSaveClienteConDniDeClienteArchivado() {
        // Preparamos un cliente existente archivado con el mismo DNI
        Cliente clienteArchivoExistente = new Cliente();
        clienteArchivoExistente.setId(6);
        clienteArchivoExistente.setDni(87654321);
        clienteArchivoExistente.setNombre("Cliente Archivado");
        clienteArchivoExistente.setEstado(false); // Cliente archivado (deshabilitado)

        Cliente clienteConDniArchivado = new Cliente();
        clienteConDniArchivado.setId(null); // No tiene ID aún (nuevo cliente)
        clienteConDniArchivado.setDni(87654321); // DNI duplicado con cliente archivado
        clienteConDniArchivado.setNombre("Cliente Nuevo");
        clienteConDniArchivado.setDireccion("Otra Calle 789");
        clienteConDniArchivado.setTelefono("11-5555-6666");
        clienteConDniArchivado.setEmail("nuevo2@test.com");
        clienteConDniArchivado.setEstado(true);

        when(clienteRepository.findByDni(87654321)).thenReturn(Optional.of(clienteArchivoExistente));

        // Ejecutar
        Result<Cliente> resultado = clienteService.save(clienteConDniArchivado);

        // Verificar
        assertFalse(resultado.isSuccess());
        assertTrue(resultado.isFailure());
        assertTrue(resultado.getError() instanceof ArchivedEntityError);
        assertEquals("ERR-ARCH", resultado.getError().getCode());
        assertNotNull(resultado.getData()); // Debe contener la entidad archivada
        assertEquals(clienteArchivoExistente.getId(), resultado.getData().getId());
    }
}
