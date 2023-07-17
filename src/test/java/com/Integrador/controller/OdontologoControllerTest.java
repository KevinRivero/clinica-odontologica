package com.Integrador.controller;

import com.Integrador.entities.Odontologo;
import com.Integrador.service.OdontologoService;
import com.Integrador.service.PacienteService;
import com.Integrador.service.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class OdontologoControllerTest {

    private static final Logger log = Logger.getLogger(OdontologoControllerTest.class);

    @Autowired
    private MockMvc mock;

    @MockBean
    private OdontologoService odontologoService;

    //los siguientes services no se usan pero la aplicacion los necesita para correr correctamente
    @MockBean
    private PacienteService pacienteService;
    @MockBean
    private TurnoService turnoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long ID;
    private Odontologo odontologo;

    @BeforeEach
    void setup(){
        log.debug("Se inicializa correctamente un odontologo para la ejecucion de los test");

        odontologo = new Odontologo();
        odontologo.setNombre("Kevin");
        odontologo.setApellido("Rivero");
        odontologo.setMatricula("45");

        ID= 1L;

        log.debug("Finaliza correctamente el metodo setup");
    }

    @DisplayName("Controller: Guardar odontologo")
    @Test
    void guardarOdontologo() throws Exception {
        log.debug("Controller: Inicia test para guardar un odontologo");

        given(odontologoService.altaOdontologo(any(Odontologo.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mock.perform(post("/odontologos/crearodontologo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(odontologo)));

        //lo que se hace en las lineas anteriores es lo siguiente:
        // - en la linea 66 se envia la informacion a traves de http, y se envia odontologo en forma de JSON
        // - esta infomacion que se envia es la que entraria como parametro en la linea 63, entraria dentro del any(Odontologo.class)

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(odontologo.getNombre())))
                .andExpect(jsonPath("$.apellido", is(odontologo.getApellido())))
                .andExpect(jsonPath("$.matricula", is(odontologo.getMatricula())))
                .andExpect(jsonPath("$.id", is(not(equals(0L)))));

        log.debug("Controller: Finaliza correctamente el test para guardar un odontologo");
    }

    @DisplayName("Controller: Listar odontologos")
    @Test
    void listarOdontologos() throws Exception {
        log.debug("Controller: Inicia test para listar odontologos");

        List<Odontologo> odontologos = new ArrayList<>();

        //se crea otro odontologo para que la lista tenga mas de un item
        Odontologo odontologo2 = new Odontologo();
        odontologo2.setId(2L);
        odontologo2.setNombre("Tiziana");
        odontologo2.setApellido("Canete");
        odontologo2.setMatricula("46");
        odontologo.setId(ID);
        //se guardan en la lista
        odontologos.add(odontologo);
        odontologos.add(odontologo2);

        given(odontologoService.listarOdontologos()).willReturn(odontologos);

        ResultActions response = mock.perform(get("/odontologos/listarodontologos"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(odontologos.size())));

        log.debug("Controller: Finaliza correctamente el test para listar odontologos");
    }

    @DisplayName("Controller: Buscar odontologo por id")
    @Test
    void buscarOdontologoPorId() throws  Exception{
        log.debug("Controller: Inicia test para buscar un odontologo por su id");

        given(odontologoService.buscarOdontologoPorId(ID)).willReturn(Optional.of(odontologo));

        ResultActions response = mock.perform(get("/odontologos/buscarodontologo/{id}",ID));

        response.andDo(print())
                .andExpect(status().isOk());

        log.debug("Controller: Finaliza correctamente el test para buscar un odontologo por su id");
    }

    @DisplayName("Controller: Actualizar odontologo")
    @Test
    void actualizarOdontologo() throws Exception{
        log.debug("Controller: Inicia test para actualizar un odontologo");

        //se modifican los datos de odontologo para corroborar los cambios
        odontologo.setId(ID);
        odontologo.setNombre("Tiziana");
        odontologo.setApellido("Canete");
        odontologo.setMatricula("46");

        //en este caso se incluye el metodo buscarOdontologoPorId porque en el Controller esta ese metodo
        given(odontologoService.buscarOdontologoPorId(ID)).willReturn(Optional.of(odontologo));
        doNothing().when(odontologoService).actualizarOdontologo(any(Odontologo.class));

        ResultActions response = mock.perform(put("/odontologos/actualizarodontologo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(odontologo)));

        response.andDo(print())
                .andExpect(status().isOk());

        verify(odontologoService,times(1)).actualizarOdontologo(any(Odontologo.class));

        log.debug("Controller: Finaliza correctamente el test para actualizar un odontologo");
    }

    @DisplayName("Controller: Eliminar Odontologo")
    @Test
    void eliminarOdontologo() throws Exception{
        log.debug("Controller: Inicia test para eliminar un odontologo por su id");

        given(odontologoService.buscarOdontologoPorId(ID)).willReturn(Optional.of(odontologo));
        doNothing().when(odontologoService).eliminarOdontologo(ID);

        ResultActions response = mock.perform(delete("/odontologos/eliminarodontologo/{id}",ID));

        response.andDo(print())
                .andExpect(status().isOk());

        verify(odontologoService,times(1)).eliminarOdontologo(ID);

        log.debug("Controller: Finaliza correctamente el test para eliminar un odontologo por su id");
    }

}
