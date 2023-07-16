package com.Integrador.controller;

import com.Integrador.entities.Odontologo;
import com.Integrador.entities.Paciente;
import com.Integrador.entities.Turno;
import com.Integrador.exception.BadRequestException;
import com.Integrador.exception.ResourceNotFoundException;
import com.Integrador.service.OdontologoService;
import com.Integrador.service.PacienteService;
import com.Integrador.service.TurnoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turnos")
public class TurnoController {

    private static final Logger LOGGER = Logger.getLogger(TurnoController.class);

    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;

@PostMapping("/crearturno")
    public ResponseEntity<Turno> guardarTurno(@RequestBody Turno turno) throws BadRequestException {
    LOGGER.info("Iniciando método para guardar un turno");
    Optional<Paciente> pacienteBuscado = pacienteService.buscarPacientePorId(turno.getPaciente().getId());
    Optional<Odontologo> odontologoBuscado = odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId());
    if(pacienteService.buscarPacientePorId(turno.getPaciente().getId()).isPresent() && odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId()).isPresent()){
        Turno turnoGuardado = turnoService.registrarTurno(turno);
        LOGGER.info("Se registró correctamente el turno");
        return ResponseEntity.ok(turnoGuardado);
    }else{
        LOGGER.info("No se pudo registrar el turno");
        throw new BadRequestException("El paciente o el odontologo son nulos");
    }
}

@GetMapping("/listarturnos")
    public ResponseEntity<List<Turno>> listarTurnos(){
    List<Turno> listadoTurnos = turnoService.listarTurnos();
    LOGGER.info("Se listo correctamente la lista de turnos");
    return ResponseEntity.ok(listadoTurnos);
}

@GetMapping("/buscarturno/{id}")
    public ResponseEntity<Turno> buscarTurnoPorId(@PathVariable Long id) throws ResourceNotFoundException {
    LOGGER.info("Inicia medoto de busqueda de turno por id");
    // primero se busca el turno para luego corroborar que existe
    Optional<Turno> turnoBuscado= turnoService.buscarTurno(id);
    if(turnoBuscado.isPresent()){
        LOGGER.info("El turno con id " + id + " fue encontrado");
        return ResponseEntity.ok(turnoBuscado.get());
    }else{
        LOGGER.info("No se encontro el turno con el id "+ id);
        throw new ResourceNotFoundException("No se encontro el turno con el id "+ id);
    }
}

@DeleteMapping("/eliminarturno/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id) throws ResourceNotFoundException{
    // se busca el turno para luego verificar que existe
    Optional<Turno> turnoBuscado= turnoService.buscarTurno(id);
    if(turnoBuscado.isPresent()){
        LOGGER.info("Existe el turno con id " + id);
        turnoService.eliminarTurno(id);
        LOGGER.info("Se elimino correctamente el turno con id " + id);
        return ResponseEntity.ok("El turno con id " + id + " fue eliminado correctamente");
    }else{
        // El turno no existe, por lo tanto se avisa con un Logger y se lanza la excepcion correspondiente
        LOGGER.error("No se encontro el turno con el id "+ id);
        throw new ResourceNotFoundException("No se encontro el turno con el id + id");
    }
}

@PutMapping("/actualizarturno")
    public ResponseEntity<String> actualizarTurno(@RequestBody Turno turno) throws BadRequestException{
    if(pacienteService.buscarPacientePorId(turno.getPaciente().getId()).isPresent() && odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId()).isPresent()){
        LOGGER.info("Existe el paciente y odontologo enviados");
        Optional<Turno> turnoBuscado = turnoService.buscarTurno(turno.getId());
        if(turnoBuscado.isPresent()){
            LOGGER.info("Existe el turno con id "+ turno.getId());
            turnoService.actualizarTurno(turno);
            LOGGER.info("Se actualizo correctamente el turno con id " + turno.getId());
            return ResponseEntity.ok("Se actualizo el turno con el id " + turno.getId());
        }else{
            LOGGER.error("No existe el turno con id "+ turno.getId() + ", revise nuevamente el id");
            throw new BadRequestException("El turno con el id "+ turno.getId()+ " no fue encontrado, revise nuevamente el id");
        }
    }else{
        LOGGER.error("El paciente o el odontologo no fueron encontrados, revise nuevamente el id");
        throw new BadRequestException("El paciente o el odontologo no fueron encontrados, revise nuevamente el id");
    }
}


}
