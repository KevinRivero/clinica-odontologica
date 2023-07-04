package com.Integrador.controller;

import com.Integrador.entities.Odontologo;
import com.Integrador.exception.BadRequestException;
import com.Integrador.exception.ResourceNotFoundException;
import com.Integrador.service.OdontologoService;
import org.apache.coyote.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

    private static final Logger LOGGER = Logger.getLogger(OdontologoController.class);

    @Autowired
    private OdontologoService odontologoService;

    @GetMapping("/listarodontologos")
    public ResponseEntity<List<Odontologo>> listarOdontologos(){
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        List<Odontologo> listadoOdontologos = odontologoService.listarOdontologos();
        LOGGER.info("Se listaron correctamente los odontologos");
        return ResponseEntity.ok(listadoOdontologos);
    }

    @GetMapping("/buscarodontologo/{id}")
    public ResponseEntity<Odontologo> buscarOdontologoPorId(@PathVariable Long id) throws ResourceNotFoundException{
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        // se busca un odontologo para luego verificar que existe
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarOdontologoPorId(id);
        if(odontologoBuscado.isPresent()){
            //existe, por lo tanto se ejecutan los metodos correspondientes
            LOGGER.info("El odontologo con id " + id + " fue encontrado");
            return ResponseEntity.ok(odontologoBuscado.get());
        }else{
            LOGGER.error("No se encontro el odontologo con id " + id);
            throw new ResourceNotFoundException("No se encontro el odontologo con id "+ id);
        }
    }

    @PostMapping("/crearodontologo")
    public ResponseEntity<Odontologo> altaOdontologo(@RequestBody Odontologo odontologo){
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        Odontologo nuevoOdontologo = odontologoService.altaOdontologo(odontologo);
        LOGGER.info("Se creó correctamente el odontólogo");
        return ResponseEntity.ok(nuevoOdontologo);
    }

    @DeleteMapping("/eliminarodontologo/{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        // Se crea una variable para guardar lo que arroja la funcion del service, para poder enviar un Log avisando que se ejecuto correctamente
        //se busca el odontologo para luego verificar si existe
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarOdontologoPorId(id);
        if(odontologoBuscado.isPresent()) {
            // existe, por lo tanto se ejecutan los metodos correspondientes
            LOGGER.info("Se encontró el odontólogo con id " + id);
            odontologoService.eliminarOdontologo(id);
            LOGGER.info("Se eliminó correctamente el odontólogo");
            return ResponseEntity.ok("El odontologo con id " + id + " fue eliminado correctamente");
        }else{
            // el odontologo no existe entonces se avisa con un Logger a la consola y se lanza una excepcion
            LOGGER.error("No se encontró el odontólogo con id " + id);
            throw new ResourceNotFoundException("No se encontro el odontologo con id "+ id);
        }
    }

    @PutMapping("/actualizarodontologo")
    public ResponseEntity<String> actualizarOdontologo(@RequestBody Odontologo odontologo) throws BadRequestException {
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarOdontologoPorId(odontologo.getId());
        if(odontologoBuscado.isPresent()){
            LOGGER.info("Se encontró el odontólogo con id "+ odontologo.getId());
            odontologoService.actualizarOdontologo(odontologo);
            LOGGER.info("Se actualizó correctamente el odontólogo");
            return ResponseEntity.ok("El odontologo con id " + odontologo.getId() + " fue actualizado correctamente");
        }else{
            LOGGER.error("El odontólogo no fue encontrado, revise nuevamente el id");
            throw new BadRequestException("El odontólogo no fue encontrado, revise nuevamente el id");
        }
    }
}
