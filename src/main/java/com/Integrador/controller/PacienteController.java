package com.Integrador.controller;
import com.Integrador.entities.Paciente;
import com.Integrador.exception.BadRequestException;
import com.Integrador.exception.ResourceNotFoundException;
import com.Integrador.service.PacienteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private static final Logger LOGGER = Logger.getLogger(PacienteController.class);

    @Autowired
    private PacienteService pacienteService;


    // --- POST ---   //

    @PostMapping("/crearpaciente")
    public ResponseEntity<Paciente> altaPaciente(@RequestBody Paciente paciente){
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        Paciente nuevoPaciente = pacienteService.altaPaciente(paciente);
        LOGGER.info("Paciente creado correctamente");
        return ResponseEntity.ok(nuevoPaciente);
    }


    // --- GET ---   //
    @GetMapping("/listarpacientes")
    public ResponseEntity<List<Paciente>> listarPacientes(){
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        List<Paciente> listadoPacientes = pacienteService.listarPacientes();
        LOGGER.info("Se listaron correctamente los pacientes");
        return ResponseEntity.ok(listadoPacientes);
    }

    @GetMapping("/buscarpaciente/{id}")
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable Long id) throws ResourceNotFoundException{
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        // se busca el paciente para luego verificar que existe
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPacientePorId(id);
        if(pacienteBuscado.isPresent()){
            LOGGER.info("Paciente con id " + id + " encontrado");
            return ResponseEntity.ok(pacienteBuscado.get());
        }else{
            //si no se encuentra el paciente se arroja un logger para visualizarlo en la consola y una excepcion
            LOGGER.error("No se encontro al paciente con id " + id);
            throw new ResourceNotFoundException("No se encontro el paciente con id "+ id);
        }
    }

    // --- DELETE ---   //
    @DeleteMapping("/eliminarpaciente/{id}")
    public ResponseEntity<String>eliminarPaciente(@PathVariable Long id) throws ResourceNotFoundException {
        //se busca el paciente para luego verificar que existe
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPacientePorId(id);
        if(pacienteBuscado.isPresent()){
            // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
            pacienteService.eliminarPaciente(id);
            LOGGER.info("El paciente fue eliminado correctamente");
            return ResponseEntity.ok("El paciente con id " + id + " fue eliminado con exito");
        }else{
            // el paciente no existe por lo tanto no se puede elilminar y tengo que lanzar una excepcion
            LOGGER.error("No existe el paciente con id "+ id);
            throw new ResourceNotFoundException("No existe el paciente con id " + id);
        }

    }


    // --- PUT ---   //
    @PutMapping("/actualizarpaciente")
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente) throws BadRequestException {
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPacientePorId(paciente.getId());
        LOGGER.info("Buscando paciente por el id " + paciente.getId() + " ...");
        if(pacienteBuscado.isPresent()){
            LOGGER.info("El paciente con id " + paciente.getId() + " fue encontrado");
            pacienteService.actualizarPaciente(paciente);
            LOGGER.info("El paciente con id " + paciente.getId() + " fue actualizado correctamente");
            return ResponseEntity.ok("El paciente con id " + paciente.getId() + " fue actualizado con exito");
        }else{
            LOGGER.error("El paciente buscado no fue encontrado, revise nuevamente el id");
            throw new BadRequestException("El paciente buscado no fue encontrado, revise nuevamente el id");
        }
    }



}
