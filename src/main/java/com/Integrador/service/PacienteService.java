package com.Integrador.service;

import com.Integrador.entities.Paciente;
import com.Integrador.repository.PacienteRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private static final Logger LOGGER = Logger.getLogger(PacienteService.class);

    @Autowired
    private PacienteRepository pacienteRepository;


    public Paciente altaPaciente(Paciente paciente){
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarPacientes(){
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPacientePorId (Long id){
        return pacienteRepository.findById(id);
    }

    public void actualizarPaciente (Paciente paciente){
        pacienteRepository.save(paciente); // En este caso, Hibernate actualiza el paciente. La funcion en sí, permite actualizar el paciente o guardarlo en caso que no exista
    }

    public void eliminarPaciente(Long id){
        pacienteRepository.deleteById(id);
    }

}
