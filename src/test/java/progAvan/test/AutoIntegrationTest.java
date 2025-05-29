package progAvan.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.transaction.Transactional;
import progAvan.AutosApplication;
import progAvan.Model.Auto;
import progAvan.Model.Cliente;
import progAvan.Model.Marca;
import progAvan.Model.Modelo;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.ClienteRepository;
import progAvan.Repository.MarcaRepository;
import progAvan.Repository.ModeloRepository;
import progAvan.test.config.TestConfig;

/**
 * Test de integración para la clase Auto.
 * Este test de integración prueba la funcionalidad completa de Auto
 * interactuando con todos los componentes reales (servicios, repositorios y
 * base de datos).
 * Se usa una base de datos H2 en memoria para las pruebas.
 */
@SpringBootTest(classes = AutosApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AutoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;

    private Auto auto;
    private Cliente cliente;
    private Marca marca;
    private Modelo modelo;
    private Integer autoId;

    @BeforeEach
    void setUp() {
        // Crear y guardar cliente
        cliente = new Cliente();
        cliente.setNombre("Juan Perez");
        cliente.setDni(12345678);
        cliente.setTelefono("12345678");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setEstado(true);
        clienteRepository.save(cliente);

        // Crear y guardar marca
        marca = new Marca();
        marca.setNombre("Toyota");
        marca.setEstado(true);
        marcaRepository.save(marca);

        // Crear y guardar modelo
        modelo = new Modelo();
        modelo.setNombre("Corolla");
        modelo.setMarca(marca);
        modelo.setEstado(true);
        modeloRepository.save(modelo);

        // Crear y guardar auto
        auto = new Auto();
        auto.setPatente("ABC123");
        auto.setAnio("2022");
        auto.setCliente(cliente);
        auto.setModelo(modelo);
        auto.setEstado(true);
        autoRepository.save(auto);
        autoId = auto.getId();
    }

    // TC-AUTO-INTEGRACION-001
    @Test
    void testMostrarAutos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/auto/mostrar")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].patente").exists());
    }

    // TC-AUTO-INTEGRACION-002
    @Test
    void testMostrarAutosHabilitados() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/auto/mostrarHabilitados")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Auto> autos = objectMapper.readValue(content, new TypeReference<List<Auto>>() {
        });

        assertFalse(autos.isEmpty());
        for (Auto a : autos) {
            assertTrue(a.getEstado());
        }
    }

    // TC-AUTO-INTEGRACION-003
    @Test
    void testGuardarAuto() throws Exception {
        Auto nuevoAuto = new Auto();
        nuevoAuto.setPatente("XYZ789");
        nuevoAuto.setAnio("2023");
        nuevoAuto.setCliente(cliente);
        nuevoAuto.setModelo(modelo);
        nuevoAuto.setEstado(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auto/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoAuto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));

        // Verificar que se guardó en la base de datos mediante consulta directa
        // ya que findByPatente usa LIKE y puede retornar resultados no esperados
        List<Auto> autos = autoRepository.findAll();
        boolean autoEncontrado = false;
        for (Auto a : autos) {
            if (a.getPatente().equals("XYZ789")) {
                autoEncontrado = true;
                break;
            }
        }
        assertTrue(autoEncontrado, "No se encontró el auto guardado en la base de datos");
    }

    // TC-AUTO-INTEGRACION-004
    @Test
    void testEditarAuto() throws Exception {
        auto.setAnio("2024");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auto/editar/{id}", autoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));

        // Verificar que se actualizó en la base de datos
        Auto autoActualizado = autoRepository.findById(autoId).orElse(null);
        assertNotNull(autoActualizado);
        assertEquals("2024", autoActualizado.getAnio());
    }

    // TC-AUTO-INTEGRACION-005
    @Test
    void testEliminarAuto() throws Exception {
        // Probar el endpoint POST /auto/eliminar/{id}
        mockMvc.perform(MockMvcRequestBuilders.post("/auto/eliminar/" + autoId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));

        // Verificar que el estado cambió en la base de datos
        Auto autoActualizado = autoRepository.findById(autoId).orElse(null);
        assertNotNull(autoActualizado);
        assertFalse(autoActualizado.getEstado());
    }

    // TC-AUTO-INTEGRACION-006
    @Test
    void testEliminarAutoInexistente() throws Exception {
        int idInexistente = 99999;

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auto/eliminar/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error"));
    }

    // TC-AUTO-INTEGRACION-007
    @Test
    void testGuardarAutoConPatenteInvalida() throws Exception {
        Auto autoInvalido = new Auto();
        autoInvalido.setPatente("PATENTE-INVALIDA"); // Formato inválido de patente
        autoInvalido.setAnio("2023");
        autoInvalido.setCliente(cliente);
        autoInvalido.setModelo(modelo);
        autoInvalido.setEstado(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auto/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autoInvalido))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // TC-AUTO-INTEGRACION-008
    @Test
    void testGuardarAutoDuplicado() throws Exception {
        Auto autoDuplicado = new Auto();
        autoDuplicado.setPatente("ABC123"); // Misma patente que el auto en setUp
        autoDuplicado.setAnio("2023");
        autoDuplicado.setCliente(cliente);
        autoDuplicado.setModelo(modelo);
        autoDuplicado.setEstado(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auto/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autoDuplicado))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error interno"));
    }
}
