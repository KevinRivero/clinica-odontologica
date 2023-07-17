package com.Integrador.controller;


import com.Integrador.entities.Domicilio;
import com.Integrador.entities.Paciente;
import com.Integrador.service.OdontologoService;
import com.Integrador.service.PacienteService;
import com.Integrador.service.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class PacienteControllerTest {

    //log4j
    private static final Logger log = Logger.getLogger(PacienteControllerTest.class);

    // este mock sirve para poder probar peticiones http
    @Autowired
    private MockMvc mockMvc;

    @MockBean // para agregar objetos simulados al contexto de la aplicacion
    private PacienteService pacienteService;

    @MockBean
    private OdontologoService odontologoService;

    @MockBean
    private TurnoService turnoService;

    @Autowired
    private ObjectMapper objectMapper;

    // variables a usar durante el desarrollo de los tests
    Paciente paciente;
    Domicilio domicilio;
    Long ID;

    //------------------METODOS--------------------

    // Se crea un paciente para poder ejecturas las pruebas
    //este metodo se ejecuta antes de cada prueba
    //setup() Inicializa y setea valores a domicilio y paciente
    @BeforeEach
    public void setup(){
        log.debug("Se inicia metodo setup para crear pacientes");

        // se le asigna un valor de tipo Long
        ID = 1L;

        domicilio = new Domicilio();
        domicilio.setCalle("Calle1");
        domicilio.setNumero(747);
        domicilio.setProvincia("Cordoba");
        domicilio.setLocalidad("Capital");

        paciente = new Paciente();
        paciente.setApellido("Rivero");
        paciente.setNombre("Kevin");
        paciente.setDni("8448484");
        paciente.setFechaIngreso(LocalDate.of(2023, 06, 30));
        paciente.setDomicilio(domicilio);
        paciente.setId(ID);

        log.debug("Finaliza correctamente el metodo setup para crear pacientes");
    }

    @DisplayName("Controller: Guardar paciente")
    @Test
    void guardarPaciente() throws Exception {
        log.debug("Controller: Inicia metodo para guardar un paciente");

        // el metodo altaPaciente recibe cualquier instancia de tipo Paciente, luego en el willAnswer() se crea una funcion que retorna el primer argumento
        // que se le pasa al metodo, osea la instancia de tipo Paciente que le hayamos mandado
        given(pacienteService.altaPaciente(any(Paciente.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //
        ResultActions response = mockMvc.perform(post("/pacientes/crearpaciente") // se hara una solicitud de tipo post a este endpoint
                .contentType(MediaType.APPLICATION_JSON) // Esto indica que se enviará un objeto JSON en el cuerpo de la solicitud.
                .content(objectMapper.writeValueAsString(paciente))); // Aquí se establece el contenido de la solicitud, que es el objeto paciente convertido a su representación JSON utilizando el objeto objectMapper. El método writeValueAsString() convierte el objeto en una cadena JSON.

        //se verifica que el status sea Ok y que los valores que retorne la respuesta sean iguales a los del objeto enviado
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(paciente.getNombre())))
                .andExpect(jsonPath("$.apellido",is(paciente.getApellido())))
                .andExpect(jsonPath("$.dni",is(paciente.getDni())))
                .andExpect(jsonPath("$.fechaIngreso",is(paciente.getFechaIngreso().toString())))
                .andExpect(jsonPath("$.domicilio.numero", is(paciente.getDomicilio().getNumero())))
                .andExpect(jsonPath("$.domicilio.calle", is(paciente.getDomicilio().getCalle())))
                .andExpect(jsonPath("$.domicilio.localidad", is(paciente.getDomicilio().getLocalidad())))
                .andExpect(jsonPath("$.domicilio.provincia", is(paciente.getDomicilio().getProvincia())));

        log.debug("Finaliza correctamente el test para guardar un paciente");

    }

    @DisplayName("Controller: Listar pacientes")
    @Test
    void listarPacientes() throws Exception {

        log.debug("Controller: Inicia test para listar pacientes");

        List<Paciente> pacientes = new ArrayList<>();

        // Se crea otro objeto Paciente para que la lista tenga al menos dos elementos
        Domicilio domicilio2 = new Domicilio();
        domicilio2.setCalle("Balcarce");
        domicilio2.setNumero(451);
        domicilio2.setProvincia("Bs Aires");
        domicilio2.setLocalidad("La plata");
        Paciente paciente2 = new Paciente();
        paciente2.setApellido("Ibanez");
        paciente2.setNombre("Jorge");
        paciente2.setDni("4525");
        paciente2.setFechaIngreso(LocalDate.of(2021, 04, 12));
        paciente2.setDomicilio(domicilio2);

        //se agregan los pacientes a la lista
        pacientes.add(paciente);
        pacientes.add(paciente2);

        given(pacienteService.listarPacientes()).willReturn(pacientes);

        ResultActions response = mockMvc.perform(get("/pacientes/listarpacientes"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(pacientes.size())));

        log.debug("Finaliza correctamente el test para listar pacientes");

    }

    @DisplayName("Controller: Buscar paciente por id")
    @Test
    void buscarPacientePorId() throws Exception {

        log.debug("Controller: Inicia el test para buscar pacientes por id");

        given(pacienteService.buscarPacientePorId(ID)).willReturn(Optional.ofNullable(paciente));

        ResultActions response = mockMvc.perform(get("/pacientes/buscarpaciente/{id}",ID));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(paciente.getNombre())))
                .andExpect(jsonPath("$.apellido",is(paciente.getApellido())))
                .andExpect(jsonPath("$.dni",is(paciente.getDni())))
                .andExpect(jsonPath("$.fechaIngreso",is(paciente.getFechaIngreso().toString())))
                .andExpect(jsonPath("$.domicilio.numero", is(paciente.getDomicilio().getNumero())))
                .andExpect(jsonPath("$.domicilio.calle", is(paciente.getDomicilio().getCalle())))
                .andExpect(jsonPath("$.domicilio.localidad", is(paciente.getDomicilio().getLocalidad())))
                .andExpect(jsonPath("$.domicilio.provincia", is(paciente.getDomicilio().getProvincia())));

    }

    @DisplayName("Controller: Buscar paciente por id: No encontrado")
    @Test
    void buscarPacientePorIdNoEncontrado() throws Exception {

        log.debug("Controller: Inicia el test para buscar pacientes por id no encontrado");

        given(pacienteService.buscarPacientePorId(ID)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/pacientes/buscarpaciente/{id}",ID));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Controller: Actualizar paciente")
    @Test
    void actualizarPaciente() throws Exception {

        log.debug("Controller: Inicia test para actualizar pacientes");

        //se crea otro paciente para comparalo con el que se busca por el id
        Paciente pacienteActualizado = paciente;
        pacienteActualizado.setId(ID);
        pacienteActualizado.setNombre("Tiziana");
        pacienteActualizado.setApellido("Canete");
        pacienteActualizado.setDni("345533");

        given(pacienteService.buscarPacientePorId(ID)).willReturn(Optional.of(paciente));
        doNothing().when(pacienteService).actualizarPaciente(any(Paciente.class));


        ResultActions response = mockMvc.perform(put("/pacientes/actualizarpaciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteActualizado)));

        response.andDo(print())
                .andExpect(status().isOk());

        //se corrobora que el metodo haya sido ejecutado una sola vez
        verify(pacienteService,times(1)).actualizarPaciente(any(Paciente.class));


    log.debug("Finaliza correctamente el test para actualizar pacientes");

    }

    @DisplayName("Controller: Eliminar paciente")
    @Test
    void eliminarPaciente() throws Exception {
        log.debug("Controller: Inicia test para eliminar paciente");

        //se tiene que usar el metodo buscarPacientePorId() porque en el Controller esta dentro del metodo y hay que simularlo por completo
        given(pacienteService.buscarPacientePorId(ID)).willReturn(Optional.of(paciente));
        doNothing().when(pacienteService).eliminarPaciente(ID);

        ResultActions response = mockMvc.perform(delete("/pacientes/eliminarpaciente/{id}",ID));

        response.andDo(print())
                .andExpect(status().isOk());

        verify(pacienteService,times(1)).eliminarPaciente(ID);

        log.debug("Finaliza correctamente el test para eliminar paciente");
    }



}
