package progAvan.test;

/**
 * Test de integración para la clase OrdenTrabajo.
 * Este test de integración prueba la funcionalidad completa de OrdenTrabajo
 * interactuando con todos los componentes reales (servicios, repositorios y base de datos).
 * Se usa una base de datos H2 en memoria para las pruebas.
 */
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import jakarta.transaction.Transactional;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import progAvan.AutosApplication;
import progAvan.Model.Auto;
import progAvan.Model.Cliente;
import progAvan.Model.DetalleOrdenTrabajo;
import progAvan.Model.Estado;
import progAvan.Model.Marca;
import progAvan.Model.Modelo;
import progAvan.Model.OrdenTrabajo;
import progAvan.Model.Servicio;
import progAvan.Model.Tecnico;
import progAvan.Repository.AutoRepository;
import progAvan.Repository.ClienteRepository;
import progAvan.Repository.DetalleOrdenRepository;
import progAvan.Repository.EstadoRepository;
import progAvan.Repository.MarcaRepository;
import progAvan.Repository.ModeloRepository;
import progAvan.Repository.OrdenTrabajoRepository;
import progAvan.Repository.ServicioRepository;
import progAvan.Repository.TecnicoRepository;
import progAvan.test.config.TestConfig;

@SpringBootTest(classes = AutosApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
// Este es un test de integración que usa una base de datos H2 en memoria
// Los métodos que ejecutan SQL nativo específico de PostgreSQL han sido
// adaptados
public class OrdenTrabajoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    private OrdenTrabajo ordenTrabajo;
    private Auto auto;
    private Cliente cliente;
    private Marca marca;
    private Modelo modelo;
    private Tecnico tecnico;
    private Estado estado;
    private DetalleOrdenTrabajo detalle;
    private Servicio servicio;

    private Integer ordenId;
    private Integer clienteId;

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
        clienteId = cliente.getId();

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
        auto.setPatente("AA123BB");
        auto.setAnio("2022");
        auto.setCliente(cliente);
        auto.setModelo(modelo);
        auto.setEstado(true);
        autoRepository.save(auto);

        // Crear y guardar técnico
        tecnico = new Tecnico();
        tecnico.setNombre("Juan Técnico");
        tecnico.setDni(87654321);
        tecnico.setEstado(true);
        tecnicoRepository.save(tecnico);

        // Crear y guardar estado
        estado = new Estado();
        estado.setNombre("Activo");
        estado.setDescripcion("Estado Activo");
        estadoRepository.save(estado);

        // Crear y guardar servicio
        servicio = new Servicio();
        servicio.setNombre("Cambio de aceite");
        servicio.setPrecio(5000.0f);
        servicio.setMinutosestimados(30);
        servicio.setEstado(true);
        servicioRepository.save(servicio);

        // Crear orden de trabajo
        ordenTrabajo = new OrdenTrabajo();
        ordenTrabajo.setDescripcion("Servicio completo");
        ordenTrabajo.setFechaInicio(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        ordenTrabajo.setFechaFin(calendar.getTime());
        ordenTrabajo.setTotal(5000.0f);
        ordenTrabajo.setTecnico(tecnico);
        ordenTrabajo.setAuto(auto);
        ordenTrabajo.setEstado(estado);
        ordenTrabajo.setHabilitado(true);
        ordenTrabajoRepository.save(ordenTrabajo);
        ordenId = ordenTrabajo.getId();

        // Crear detalle de orden
        detalle = new DetalleOrdenTrabajo();
        detalle.setDescripcion("Cambio de aceite sintético");
        detalle.setCantidad(1);
        detalle.setServicio(servicio);
        detalle.setSubtotal(5000.0);
        detalle.setEstado(true);
        detalle.setMinutosRealizados(25);
        detalle.setOrden(ordenTrabajo);
        detalleOrdenRepository.save(detalle);

        // Refrescar la orden para tener los detalles actualizados
        ordenTrabajo = ordenTrabajoRepository.findById(ordenId).orElse(null);
    }

    // TC-ORDEN-INTEGRACION-001
    @Test
    void testMostrarOrdenesTrabajo() throws Exception {
        // Probar el endpoint GET /ordenTrabajo/mostrar
        mockMvc.perform(MockMvcRequestBuilders.get("/ordenTrabajo/mostrar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());
    }

    // TC-ORDEN-INTEGRACION-002
    @Test
    void testMostrarOrdenesTrabajoHabilitadas() throws Exception {
        // Probar el endpoint GET /ordenTrabajo/mostrarHabilitados
        mockMvc.perform(MockMvcRequestBuilders.get("/ordenTrabajo/mostrarHabilitados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    // TC-ORDEN-INTEGRACION-003
    @Test
    void testGuardarOrdenTrabajo() throws Exception {
        // Crear una nueva orden para guardar
        OrdenTrabajo nuevaOrden = new OrdenTrabajo();
        nuevaOrden.setDescripcion("Orden desde test MockMvc");
        nuevaOrden.setFechaInicio(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 4);
        nuevaOrden.setFechaFin(calendar.getTime());
        nuevaOrden.setTotal(9000.0f);
        nuevaOrden.setTecnico(tecnico);
        nuevaOrden.setAuto(auto);
        nuevaOrden.setEstado(estado);
        nuevaOrden.setHabilitado(true);

        // Convertir a JSON
        String ordenJson = objectMapper.writeValueAsString(nuevaOrden);

        // Probar el endpoint POST /ordenTrabajo/guardar
        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ordenJson)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    } // TC-ORDEN-INTEGRACION-004

    @Test
    void testGuardarOrdenTrabajoSinServicios() throws Exception {
        // Crear una orden con datos faltantes (sin servicios en los detalles)
        OrdenTrabajo ordenIncompleta = new OrdenTrabajo();
        ordenIncompleta.setDescripcion("Orden con detalles sin servicios");
        ordenIncompleta.setFechaInicio(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 4);
        ordenIncompleta.setFechaFin(calendar.getTime());
        ordenIncompleta.setTotal(9000.0f);
        ordenIncompleta.setTecnico(tecnico); // Asignamos técnico ya que no es el problema a probar
        ordenIncompleta.setAuto(auto);
        ordenIncompleta.setEstado(estado);
        ordenIncompleta.setHabilitado(true);

        // Agregar detalles sin servicio para forzar NullPointerException cuando se
        // procese
        List<DetalleOrdenTrabajo> detalles = new ArrayList<>();
        DetalleOrdenTrabajo detalleVacio = new DetalleOrdenTrabajo();
        detalleVacio.setDescripcion("Detalle sin servicio");
        detalleVacio.setCantidad(1);
        detalleVacio.setSubtotal(5000.0);
        detalleVacio.setEstado(true);
        detalleVacio.setMinutosRealizados(25);
        // No asignamos servicio: detalleVacio.setServicio(null);
        detalles.add(detalleVacio);
        ordenIncompleta.setDetalle(detalles);

        // Convertir a JSON
        String ordenJson = objectMapper.writeValueAsString(ordenIncompleta);

        // El servidor debería devolver un error 500 con mensaje "error interno"
        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ordenJson)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error interno"));
    }

    // TC-ORDEN-INTEGRACION-005
    @Test
    void testGuardarOrdenTrabajoFechasInvalidas() throws Exception {
        // Crear una orden con fechas inválidas (fecha fin anterior a fecha inicio)
        OrdenTrabajo ordenFechasInvalidas = new OrdenTrabajo();
        ordenFechasInvalidas.setDescripcion("Orden con fechas inválidas");
        ordenFechasInvalidas.setFechaInicio(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -4); // Fecha fin anterior a fecha inicio
        ordenFechasInvalidas.setFechaFin(calendar.getTime());
        ordenFechasInvalidas.setTotal(9000.0f);
        ordenFechasInvalidas.setTecnico(tecnico);
        ordenFechasInvalidas.setAuto(auto);
        ordenFechasInvalidas.setEstado(estado);
        ordenFechasInvalidas.setHabilitado(true);

        // Convertir a JSON
        String ordenJson = objectMapper.writeValueAsString(ordenFechasInvalidas);

        // El servidor debería devolver un error por rango inválido de fechas
        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/editar/" + ordenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ordenJson)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Rango invalido fecha"));
    }

    // TC-ORDEN-INTEGRACION-006
    @Test
    void testEliminarOrdenTrabajo() throws Exception {
        // Probar el endpoint POST /ordenTrabajo/eliminar/{id}
        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/eliminar/" + ordenId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));

        // Verificar que el estado cambió en la base de datos
        OrdenTrabajo ordenActualizada = ordenTrabajoRepository.findById(ordenId).orElse(null);
        assertNotNull(ordenActualizada);
        assertFalse(ordenActualizada.getHabilitado());
    }

    // TC-ORDEN-INTEGRACION-007
    @Test
    void testEliminarOrdenTrabajoInexistente() throws Exception {
        // Intentar eliminar una orden que no existe (ID inexistente)
        int idInexistente = 99999;

        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/eliminar/" + idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error"));
    }

    // TC-ORDEN-INTEGRACION-008
    @Test
    void testEditarOrdenTrabajo() throws Exception {
        // Modificar la orden existente
        ordenTrabajo.setDescripcion("Descripción modificada para test");
        ordenTrabajo.setTotal(6000.0f);

        // Convertir a JSON
        String ordenJson = objectMapper.writeValueAsString(ordenTrabajo);

        // Probar el endpoint POST /ordenTrabajo/editar/{id}
        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/editar/" + ordenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ordenJson)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));

        // Verificar que los cambios se guardaron en la base de datos
        OrdenTrabajo ordenActualizada = ordenTrabajoRepository.findById(ordenId).orElse(null);
        assertNotNull(ordenActualizada);
        assertEquals("Descripción modificada para test", ordenActualizada.getDescripcion());
        assertEquals(6000.0f, ordenActualizada.getTotal());
    }

    // TC-ORDEN-INTEGRACION-009
    @Test
    void testEditarOrdenTrabajoInexistente() throws Exception {
        // Intentar editar una orden inexistente
        int idInexistente = 99999;

        // Crear una copia de la orden para no modificar la orden original del test
        OrdenTrabajo ordenCopia = new OrdenTrabajo();
        ordenCopia.setDescripcion("Descripción modificada para test");
        ordenCopia.setFechaInicio(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        ordenCopia.setFechaFin(calendar.getTime());
        ordenCopia.setTotal(6000.0f);
        ordenCopia.setTecnico(tecnico);
        ordenCopia.setAuto(auto);
        ordenCopia.setEstado(estado);
        ordenCopia.setHabilitado(true);
        ordenCopia.setId(idInexistente); // Id que no existe

        // Convertir a JSON
        String ordenJson = objectMapper.writeValueAsString(ordenCopia);

        // Debería fallar al no encontrar la orden con mensaje "error interno"
        mockMvc.perform(MockMvcRequestBuilders.post("/ordenTrabajo/editar/" + idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ordenJson)
                .header("Origin", "http://localhost:4200"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    // TC-ORDEN-INTEGRACION-010
    @Test
    void testBuscarUltimaOrdenCliente() throws Exception {
        // Probar el endpoint GET para obtener la última orden de un cliente
        mockMvc.perform(MockMvcRequestBuilders.get("/ordenTrabajo/mostrar/ultima/" + clienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }
}
