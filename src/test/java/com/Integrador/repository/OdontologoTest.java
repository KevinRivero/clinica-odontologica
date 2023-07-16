package com.Integrador.repository;


import com.Integrador.entities.Odontologo;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OdontologoTest {

    private static final Logger log = Logger.getLogger(OdontologoTest.class);

    @Autowired
    private OdontologoRepository odontologoRepository;

    private Odontologo odontologo;
    private Long ID;
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

    @DisplayName("Repository: Guardar odontologo")
    @Test
    void guardarOdontologo(){
        log.debug("Repository: Inicia test para guardar odontologo");

        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);

        //se corrobora que odontologo guardado no sea nulo, que los valores que devuelve al guardar sean iguales a los enviados, y que el id sea mayor a cero
        assertThat(odontologoGuardado).isNotNull();
        assertEquals(odontologoGuardado.getNombre(), odontologo.getNombre(),"El nombre es correcto");
        assertEquals(odontologoGuardado.getApellido(), odontologo.getApellido(),"El apellido es correcto");
        assertEquals(odontologoGuardado.getMatricula(),odontologo.getMatricula(),"La matricula es correcta");
        assertThat(odontologoGuardado.getId()).isGreaterThan(0);

        log.debug("Repository: Finaliza correctamente el test para guardar odontologo");
    }

    @DisplayName("Repository: Listar odontologos")
    @Test
    void listarOdontologos(){
        log.debug("Repository: Inicia test para listar odontologos");

        List<Odontologo> odontologos = new ArrayList<>();

        //se crea otro odontologo para sumar mas de uno al listado
        Odontologo odontologo2 = new Odontologo();
        odontologo2.setId(2L);
        odontologo2.setNombre("Tiziana");
        odontologo2.setApellido("Canete");
        odontologo2.setMatricula("46");
        odontologo.setId(ID);

        //se guardan los odontologos en la bdd
        odontologoRepository.save(odontologo);
        odontologoRepository.save(odontologo2);

        //se ejectua el metodo a testear
        odontologos = odontologoRepository.findAll();

        assertThat(odontologos).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(odontologos.size(),2);

        log.debug("Finaliza correctamente el test para listar odontologos");
    }

    @DisplayName("Repository: Buscar odontologo por id")
    @Test
    void buscarOdontologoPorId(){
        log.debug("Repository: Inicia test para buscar un odontologo por su id");

        //se guarda primero un odontologo
        odontologoRepository.save(odontologo);

        Optional<Odontologo> odontologoBuscado = odontologoRepository.findById(odontologo.getId());

        assertThat(odontologoBuscado).isNotEmpty();
        assertEquals(odontologoBuscado.get().getNombre(), odontologo.getNombre(),"El nombre es correcto");
        assertEquals(odontologoBuscado.get().getApellido(), odontologo.getApellido(),"El apellido es correcto");
        assertEquals(odontologoBuscado.get().getMatricula(),odontologo.getMatricula(),"La matricula es correcta");


        log.debug("Repository: Finaliza correctamente el test para buscar un odontologo por su id");

    }

    @DisplayName("Repository: Actualizar odontologo")
    @Test
    void actualizarOdontologo(){
        log.debug("Repository: Inicia test para actualizar un odontologo");

        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);
        //se corrobora que el id del odontologo sea mayor a 0
        assertThat(odontologoGuardado.getId()).isGreaterThan(0);

        Odontologo odontologo2 = new Odontologo();
        odontologo2.setId(ID); // se le asigna el mismo id que el odontologo guardado porque justamente se quiere actualizar y deben tener el mismo identificador
        odontologo2.setNombre("Tiziana");
        odontologo2.setApellido("Canete");
        odontologo2.setMatricula("46");

        //este es el metodo a testear
        Odontologo odontologoActualizado = odontologoRepository.save(odontologo2);

        assertThat(odontologoActualizado).isNotNull();;
        assertEquals(odontologoActualizado.getNombre(), odontologo2.getNombre());
        assertEquals(odontologoActualizado.getApellido(), odontologo2.getApellido());
        assertEquals(odontologoActualizado.getMatricula(), odontologo2.getMatricula());

        log.debug("Repository: Finaliza correctamente test para actualizar un odontologo");

    }

    @DisplayName("Repository: Eliminar odontologo")
    @Test
    void eliminarOdontologo(){
        log.debug("Repository: Inicia test para eliminar un odontologo por su id");

        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);
        assertEquals(odontologoGuardado.getId(), ID);

        //el metodo a testear
        odontologoRepository.deleteById(ID);

        //se busca el odontologo por el id para luego hacer las verificaciones correspondientes
        Optional<Odontologo> odontologoEliminado = odontologoRepository.findById(ID);

        //se verifica que el odontologoEliminado este vacio, es decir, que el metodo buscar por id no encontro ningun odontologo con ese id
        assertThat(odontologoEliminado).isEmpty();

        log.debug("Repository: Finaliza correctamente el test para eliminar un odontologo por su id");

    }


}
