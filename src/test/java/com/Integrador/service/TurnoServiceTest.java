package com.Integrador.service;


import com.Integrador.entities.Domicilio;
import com.Integrador.entities.Odontologo;
import com.Integrador.entities.Paciente;
import com.Integrador.entities.Turno;
import com.Integrador.repository.OdontologoRepository;
import com.Integrador.repository.PacienteRepository;
import com.Integrador.repository.TurnoRepository;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    private static final Logger log = Logger.getLogger(TurnoServiceTest.class);

    @Mock
    TurnoRepository turnoRepository;

    @InjectMocks
    TurnoService turnoService;



    private Turno turno;

    private Paciente paciente;

    private Domicilio domicilio;

    private Odontologo odontologo;
    private Long ID;

    @BeforeEach
    void setup(){

        log.debug("Se iniciliazan las variables necesarias para realizar los test");

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

        odontologo = new Odontologo();
        odontologo.setNombre("Pedro");
        odontologo.setApellido("Vargas");
        odontologo.setMatricula("45");

        turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.of(2023,07,15));

        log.debug("Se guarda correctamente un paciente y un odontologo(pruebas) dentro de la bdd");

        log.debug("Se iniciliazaron correctamente las variables necesarias para realizar los test");

    }

    @DisplayName("Service: Guardar turno")
    @Test
    void guardarTurno(){
        log.debug("Service: Inicia test para guardar un turno");

        turno.setId(ID);

        given(turnoRepository.save(turno)).willReturn(turno);

        Turno turnoGuardado = turnoService.registrarTurno(turno);

        assertThat(turnoGuardado).isNotNull();

        log.debug("Service: Finaliza correctamente el test para guardar un turno");
    }

    @DisplayName("Service: Listar turnos")
    @Test
    void listarTurnos(){
        log.debug("Service: Inicia test para listar turnos");



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

        turnos.add(turno2);
        turnos.add(turno);

        given(turnoRepository.findAll()).willReturn(turnos);

        List<Turno> turnosListados = turnoService.listarTurnos();

        assertThat(turnosListados).isNotNull();
        assertEquals(turnosListados.size(), 2 );

        log.debug("Service: Finaliza correctamente el test para listar turnos");
    }

    @DisplayName("Service: Buscar turno por id")
    @Test
    void buscarTurnoPorId(){
        log.debug("Service: Inicia test para buscar un turno por id");

        turno.setId(ID);

        given(turnoRepository.findById(ID)).willReturn(Optional.of(turno));

        Optional<Turno> turnoBuscado = turnoService.buscarTurno(ID);

        assertThat(turnoBuscado).isNotEmpty();

        log.debug("Service: Finaliza correctamente el test para buscar un turno por id");
    }

    @DisplayName("Service: Actualizar turno")
    @Test
    void actualizarTurno(){
        log.debug("Service: Inicia test para actualizar un turno");

        //se modifican las variables del turno guardado para corroborar que se hayan efectuado los cambios
        domicilio.setId(ID);
        domicilio.setCalle("Calle modificada");
        domicilio.setNumero(10000);
        domicilio.setProvincia("Cordoba modificada");
        domicilio.setLocalidad("Modificadal");

        paciente.setId(ID);
        paciente.setApellido("Rivero modificado");
        paciente.setNombre("Kevin modificado");
        paciente.setDni("8448484 modificado");
        paciente.setFechaIngreso(LocalDate.of(2024, 02, 24));
        paciente.setDomicilio(domicilio);

        odontologo.setId(ID);
        odontologo.setNombre("Pedro modificado");
        odontologo.setApellido("Vargas modificado");
        odontologo.setMatricula("989");

        turno.setId(ID);
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.of(2023,04,12));

        given(turnoRepository.save(turno)).willReturn(turno);

        turnoService.actualizarTurno(turno);

        assertThat(turno).isNotNull();
        //se corroboran primero los datos de domicililo
        assertEquals(turno.getPaciente().getDomicilio().getCalle(), domicilio.getCalle());
        assertEquals(turno.getPaciente().getDomicilio().getNumero(), domicilio.getNumero());
        assertEquals(turno.getPaciente().getDomicilio().getProvincia(), domicilio.getProvincia());
        assertEquals(turno.getPaciente().getDomicilio().getLocalidad(), domicilio.getLocalidad());
        assertEquals(turno.getPaciente().getDomicilio().getId(), domicilio.getId());
        //luego los de paciente
        assertEquals(turno.getPaciente().getId(), paciente.getId());
        assertEquals(turno.getPaciente().getApellido(), paciente.getApellido());
        assertEquals(turno.getPaciente().getNombre(), paciente.getNombre());
        assertEquals(turno.getPaciente().getDni(), paciente.getDni());
        assertEquals(turno.getPaciente().getFechaIngreso(), paciente.getFechaIngreso());
        //luego los de turno
        assertEquals(turno.getId(), turno.getId());
        assertEquals(turno.getFecha(), turno.getFecha());

        log.debug("Service: Finaliza correctamente el test para actualizar un turno");
    }

    @DisplayName("Service: Eliminar turno por id")
    @Test
    void eliminarTurno(){
        log.debug("Service: Inicia test para eliminar un turno por su id");

        willDoNothing().given(turnoRepository).deleteById(turno.getId());

        turnoService.eliminarTurno(turno.getId());

        verify(turnoRepository,times(1)).deleteById(turno.getId());

        log.debug("Service: Finaliza correctamente el test para eliminar un turno por su id");
    }
}
