package com.Integrador.service;

import com.Integrador.entities.Domicilio;
import com.Integrador.entities.Odontologo;
import com.Integrador.entities.Paciente;
import com.Integrador.repository.OdontologoRepository;
import com.Integrador.repository.PacienteRepository;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OdontologoServiceTest {

    private static final Logger log = Logger.getLogger(OdontologoServiceTest.class);

    @Mock
    private OdontologoRepository odontologoRepository;

    @InjectMocks
    private OdontologoService odontologoService;

    private Odontologo odontologo;
    Long ID;


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

    @DisplayName("Service: Test para guardar un odontologo")
    @Test
    void guardarOdontologo(){

        log.debug("Service: Inicia test para guardar un odontologo");

        //en este caso se le setea el ID al odontologo porque no se trabaja con la bdd entonces nunca se crea automaticamente el id, porque devolvemos un objeto de tipo paciente
        odontologo.setId(ID);

        given(odontologoRepository.save(odontologo)).willReturn(odontologo);

        Odontologo odontologoGuardado = odontologoService.altaOdontologo(odontologo);

        assertThat(odontologoGuardado).isNotNull();
        assertEquals(odontologoGuardado.getNombre(), odontologo.getNombre(),"El nombre es correcto");
        assertEquals(odontologoGuardado.getApellido(), odontologo.getApellido(),"El apellido es correcto");
        assertEquals(odontologoGuardado.getMatricula(),odontologo.getMatricula(),"La matricula es correcta");
        assertThat(odontologoGuardado.getId()).isGreaterThan(0);

        log.debug("Service: Finaliza correctamente el test para guardar un odontologo");
    }

    @DisplayName("Service: Test para listar odontologos")
    @Test
    void listarOdontologos(){
        log.debug("Service: Inicia test para listar odontologos");

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

        given(odontologoRepository.findAll()).willReturn(odontologos);

        List<Odontologo> odontologosListados = odontologoService.listarOdontologos();

        assertThat(odontologosListados).isNotNull();
        assertEquals(odontologosListados.size(), 2 );

        log.debug("Service: Finaliza correctamente el test para listar odontologos");

    }
}