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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OdontologoServiceTest {

    private static final Logger log = Logger.getLogger(OdontologoServiceTest.class);

    @Mock
    private OdontologoRepository odontologoRepository;

    @InjectMocks
    private OdontologoService odontologoService;

    @Autowired
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

    @DisplayName("Service: Test para buscar un odontologo por su id")
    @Test
    void buscarOdontologoPorId(){
        log.debug("Service: Inicia test para buscar un odontologo por su id");

        odontologo.setId(ID);

        given(odontologoRepository.findById(odontologo.getId())).willReturn(Optional.of(odontologo));

        Optional<Odontologo> odontologoBuscado = odontologoService.buscarOdontologoPorId(ID);

        assertThat(odontologoBuscado).isNotEmpty();
        assertEquals(odontologoBuscado.get().getId(), odontologo.getId());

        log.debug("Service: Finaliza correctamente el test para buscar un odontologo por su id");
    }

    @DisplayName("Service: Test para actualizar odontologos")
    @Test
    void actualizarOdontologo(){
        log.debug("Service: Inicia test para actualizar un odontologo");

        //se modifican los datos de odontologo para corroborar los cambios
        odontologo.setId(ID);
        odontologo.setNombre("Tiziana");
        odontologo.setApellido("Canete");
        odontologo.setMatricula("46");

        given(odontologoRepository.save(odontologo)).willReturn(odontologo);

        Odontologo odontologoActualizado = odontologoService.altaOdontologo(odontologo);

        assertThat(odontologoActualizado).isNotNull();;
        assertEquals(odontologoActualizado.getNombre(), odontologo.getNombre());
        assertEquals(odontologoActualizado.getApellido(), odontologo.getApellido());
        assertEquals(odontologoActualizado.getMatricula(), odontologo.getMatricula());

        log.debug("Service: Finaliza correctamente el test para buscar un odontologo por su id");
    }

    @DisplayName("Service: Eliminar odontologo")
    @Test
    void eliminarOdontologo(){
        log.debug("Service: Inicia test para eliminar odontologos");

        odontologo.setId(ID);

        willDoNothing().given(odontologoRepository).deleteById(odontologo.getId());

        odontologoService.eliminarOdontologo(odontologo.getId());

        //se corrobora que solamente se haya ejecutado el metodo una vez
        verify(odontologoRepository,times(1)).deleteById(ID);

        log.debug("Service: Finaliza correctamente el test para eliminar odontologos");

    }
}
