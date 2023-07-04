package com.Integrador.service;

import com.Integrador.entities.Odontologo;
import com.Integrador.entities.Turno;
import com.Integrador.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    public Turno registrarTurno(Turno turno){
        return turnoRepository.save(turno);
    }

    public Optional<Turno> buscarTurno(Long id){
        return turnoRepository.findById(id);
    }

    public void eliminarTurno(Long id){
        turnoRepository.deleteById(id);
    }

    public void actualizarTurno(Turno turno){
        turnoRepository.save(turno);
    }

    public List<Turno> listarTurnos(){
        return turnoRepository.findAll();
    }
}
