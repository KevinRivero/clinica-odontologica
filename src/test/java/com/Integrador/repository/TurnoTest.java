package com.Integrador.repository;


import com.Integrador.entities.Domicilio;
import com.Integrador.entities.Odontologo;
import com.Integrador.entities.Paciente;
import com.Integrador.entities.Turno;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TurnoTest {

    private static final Logger log = Logger.getLogger(TurnoTest.class);

    @Autowired
    TurnoRepository turnoRepository;
    @Autowired
    PacienteRepository pacienteRepository;
    @Autowired
    OdontologoRepository odontologoRepository;

    private Turno turno;
    private Paciente paciente;
    private Domicilio domicilio;
    private Odontologo odontologo;
    private Long ID;

    @BeforeEach
    void setup(){
        log.debug("Se iniciliazan las variables necesarias para realizar los test");

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

        odontologo = new Odontologo();
        odontologo.setNombre("Pedro");
        odontologo.setApellido("Vargas");
        odontologo.setMatricula("45");

        turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.of(2023,07,15));

        ID = 1L;

        pacienteRepository.save(paciente);
        odontologoRepository.save(odontologo);
        log.debug("Se guarda correctamente un paciente y un odontologo(pruebas) dentro de la bdd");

        log.debug("Se iniciliazaron correctamente las variables necesarias para realizar los test");
    }

    @DisplayName("Repository: Guardar turno")
    @Test
    void guardarTurno(){
        log.debug("Repository: Inicia test para guardar turnos");

        Turno turnoGuardado = turnoRepository.save(turno);

        //se corrobora que el turno no sea nulo y que su id sea mayor a 0
        assertThat(turnoGuardado).isNotNull();
        assertThat(turnoGuardado.getId()).isGreaterThan(0);

        log.debug("Repository: Finaliza correctamente el test para guardar turnos");
    }

    @DisplayName("Repository: Listar turnos")
    @Test
    void listarTurnos(){
        log.debug("Repository: Inicia test para listar turnos");

        List<Turno> turnos = new ArrayList<>();

        //se crea otro odontologo, paciente y domicilio para que la lista de turnos tenga mas de un elemento
        Domicilio domicilio2 = new Domicilio();
        domicilio2.setCalle("Calle2");
        domicilio2.setNumero(748);
        domicilio2.setProvincia("Buenos Aires");
        domicilio2.setLocalidad("La Plata");

        Paciente paciente2 = new Paciente();
        paciente2.setApellido("Canete");
        paciente2.setNombre("Tiziana");
        paciente2.setDni("54545");
        paciente2.setFechaIngreso(LocalDate.of(2022, 06, 30));
        paciente2.setDomicilio(domicilio2);

        Odontologo odontologo2 = new Odontologo();
        odontologo2.setNombre("Jorge");
        odontologo2.setApellido("Rodriguez");
        odontologo2.setMatricula("65");

        Turno turno2 = new Turno();
        turno2.setPaciente(paciente2);
        turno2.setOdontologo(odontologo2);
        turno2.setFecha(LocalDate.of(2023,06,15));

        //se guardan los pacientes y los odontologos en la bdd
        pacienteRepository.save(paciente2);
        odontologoRepository.save(odontologo2);

        //se guardan los turnos en la bdd
        turnoRepository.save(turno);
        turnoRepository.save(turno2);

        turnos = turnoRepository.findAll();

        //se verifica que el turno no sea nulo y que el tamanio de la lista sea igual a la cantidad de turnos guardados
        assertThat(turnos).isNotNull();
        assertEquals(turnos.size(), 2);

        log.debug("Repository: Finaliza correctamente el test para listar turnos");

    }

    @DisplayName("Repository: Buscar turno por id")
    @Test
    void buscarTurnoPorId(){
        log.debug("Repository: Inicia test para buscar turnos por id");

        Turno turnoGuardado = turnoRepository.save(turno);
        assertEquals(turnoGuardado.getId(), ID);

        Optional<Turno> turnoBuscado = turnoRepository.findById(ID);

        assertThat(turnoBuscado).isNotEmpty();
        assertEquals(turnoBuscado.get().getPaciente().getId(), paciente.getId());
        assertEquals(turnoBuscado.get().getOdontologo().getId(), odontologo.getId());
        assertEquals(turnoBuscado.get().getFecha(), turno.getFecha());

        log.debug("Repository: Finaliza correctamente el test para buscar turnos por id");

    }

    @DisplayName("Repository: Actualizar Turno")
    @Test
    void actualizarTurno(){
        log.debug("Repository: Inicia test para actualizar un turno");

        Turno turnoGuardado = turnoRepository.save(turno);
        assertEquals(turnoGuardado.getId(),ID);

        //se modifican las variables del turno guardado para corroborar que se hayan efectuado los cambios
        domicilio.setId(turnoGuardado.getPaciente().getDomicilio().getId());
        domicilio.setCalle("Calle modificada");
        domicilio.setNumero(10000);
        domicilio.setProvincia("Cordoba modificada");
        domicilio.setLocalidad("Modificadal");

        paciente.setId(turnoGuardado.getPaciente().getId());
        paciente.setApellido("Rivero modificado");
        paciente.setNombre("Kevin modificado");
        paciente.setDni("8448484 modificado");
        paciente.setFechaIngreso(LocalDate.of(2024, 02, 24));
        paciente.setDomicilio(domicilio);

        odontologo.setId(turnoGuardado.getOdontologo().getId());
        odontologo.setNombre("Pedro modificado");
        odontologo.setApellido("Vargas modificado");
        odontologo.setMatricula("989");

        turno.setId(turnoGuardado.getId());
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.of(2023,04,12));

        //se utiliza el metodo a testear y se le pasa de vuelta el turno pero con los datos modificados
        Turno turnoActualizado = turnoRepository.save(turno);

        assertThat(turnoActualizado).isNotNull();
        //se corroboran primero los datos de domicililo
        assertEquals(turnoActualizado.getPaciente().getDomicilio().getCalle(), domicilio.getCalle());
        assertEquals(turnoActualizado.getPaciente().getDomicilio().getNumero(), domicilio.getNumero());
        assertEquals(turnoActualizado.getPaciente().getDomicilio().getProvincia(), domicilio.getProvincia());
        assertEquals(turnoActualizado.getPaciente().getDomicilio().getLocalidad(), domicilio.getLocalidad());
        assertEquals(turnoActualizado.getPaciente().getDomicilio().getId(), domicilio.getId());
        //luego los de paciente
        assertEquals(turnoActualizado.getPaciente().getId(), paciente.getId());
        assertEquals(turnoActualizado.getPaciente().getApellido(), paciente.getApellido());
        assertEquals(turnoActualizado.getPaciente().getNombre(), paciente.getNombre());
        assertEquals(turnoActualizado.getPaciente().getDni(), paciente.getDni());
        assertEquals(turnoActualizado.getPaciente().getFechaIngreso(), paciente.getFechaIngreso());
        //luego los de turno
        assertEquals(turnoActualizado.getId(), turno.getId());
        assertEquals(turnoActualizado.getFecha(), turno.getFecha());

        log.debug("Repository: Finaliza correctamente el test para actualizar un turno");

    }

    @DisplayName("Repository: Eliminar turno")
    @Test
    void eliminarTurno(){
        log.debug("Repository: Inicia test para eliminar un turno");

        Turno turnoGuardado = turnoRepository.save(turno);
        assertEquals(turnoGuardado.getId(), ID) ;

        //metodo a testear,se borra el turno
        turnoRepository.deleteById(ID);

        //se busca el turno para corroborar que los datos fueron eliminados
        Optional<Turno> turnoEliminado = turnoRepository.findById(ID);

        assertThat(turnoEliminado).isEmpty();

        log.debug("Repository: Finaliza correctamente el test para eliminar un turno");
    }
}
