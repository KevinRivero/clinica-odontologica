package com.Integrador.repository;

import com.Integrador.entities.Domicilio;
import com.Integrador.entities.Paciente;
import com.Integrador.service.PacienteService;
import org.apache.log4j.Logger;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class PacienteTests {

    private static final Logger log = Logger.getLogger(PacienteTests.class);
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private Paciente paciente;
    @Autowired
    private Domicilio domicilio;


    // Se crea un paciente para poder ejecturas las pruebas
    @BeforeEach
    public void setup(){

        log.debug("Se inicia metodo setup para crear pacientes");

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

        log.debug("Finaliza correctamente el metodo setup para crear pacientes");
    }

    @DisplayName("Guardar paciente")
    @Test
    public void guardarPaciente(){
        log.debug("Iniciando test para guardar un paciente");

        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // para corroborar que el paiente creado no sea nulo
        assertThat(pacienteGuardado).isNotNull();;
        //para corroborar que el id del paciente guardado sea mayor a 0, es decir, que se haya autogenerado
        assertThat(pacienteGuardado.getId()).isGreaterThan(0);
        log.debug("Finaliza correctamente el test para guardar un paciente");
    }

    @DisplayName(("Listar pacientes"))
    @Test
    public void listarPacientes(){

        log.debug("Iniciando test para listar pacientes");

        // Se crea un domicilio para luego meterlo en  paciente
        Domicilio domicilio2 = new Domicilio();
        domicilio2.setCalle("Balcarce");
        domicilio2.setNumero(451);
        domicilio2.setProvincia("Bs Aires");
        domicilio2.setLocalidad("La plata");

        //se crea un paciente
        Paciente paciente2 = new Paciente();
        paciente2.setApellido("Ibanez");
        paciente2.setNombre("Jorge");
        paciente2.setDni("4525");
        paciente2.setFechaIngreso(LocalDate.of(2021, 04, 12));
        // se setea domicilio dentro de paciente
        paciente.setDomicilio(domicilio2);


        //se guardan los dos pacientes usando el metodo del repository
        pacienteRepository.save(paciente);
        pacienteRepository.save(paciente2);

        // Se utilliza el metodo a testear y se guarda el return dentro de una lista
        List<Paciente> listadoPacients = pacienteRepository.findAll();

        //Corroboro que el listado de pacientes no sea nulo
        assertThat(listadoPacients).isNotNull();
        // corroboro que el tamanio de la lista sea de la cantidad de pacientes introducidos, en este caso dos
        Assertions.assertEquals(listadoPacients.size(), 2);

        log.debug("Finaliza correctamente el test para listar pacientes");
    }
    @DisplayName("Buscar paciente por id")
    @Test
        public void buscarPacientePorId(){

        //se logea informacion al principio del metodo y al final
        log.debug("Iniciando test para buscar un paciente por id");

        // guardo un paciente
        pacienteRepository.save(paciente);

        //Utilizo el metodo a testear y guardo el return en una variable
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findById(paciente.getId());

        //se corrobora que el paciente no sea nulo
        assertThat(pacienteEncontrado).isNotNull();
        //se corrobora que los datos que trae sean los correctos
        Assertions.assertEquals(pacienteEncontrado.get().getApellido(),paciente.getApellido());
        Assertions.assertEquals(pacienteEncontrado.get().getNombre(),paciente.getNombre());
        Assertions.assertEquals(pacienteEncontrado.get().getDni(),paciente.getDni());
        Assertions.assertEquals(pacienteEncontrado.get().getFechaIngreso(),paciente.getFechaIngreso());
        Assertions.assertEquals(pacienteEncontrado.get().getDomicilio().getCalle(), paciente.getDomicilio().getCalle());
        Assertions.assertEquals(pacienteEncontrado.get().getDomicilio().getNumero(), paciente.getDomicilio().getNumero());
        Assertions.assertEquals(pacienteEncontrado.get().getDomicilio().getLocalidad(), paciente.getDomicilio().getLocalidad());
        Assertions.assertEquals(pacienteEncontrado.get().getDomicilio().getProvincia(), paciente.getDomicilio().getProvincia());

        log.debug("Finaliza test para buscar un paciente por id");

    }


    // ESTE TEST TENDRIA QUE DIVIDIRLO EN VARIOS PARA CORROBORAR TAMBIEN QUE LOS DATOS MODIFICADOS HAYAN CAMBIADO
    @DisplayName("Actualizar paciente")
    @Test
        public void actualizarPaciente(){
        //se logea informacion al principio del metodo y al final

        log.debug("Inicia test para actualizar paciente");

        // guarda un paciente
        pacienteRepository.save(paciente);

        // crep un nuevo paciente para modificar el anterior
        //se crea primero un domicilio
        Domicilio domicilio2 = new Domicilio();
        //se setea el valor del id del domicilio guardado
        domicilio2.setId(paciente.getDomicilio().getId());
        domicilio2.setCalle("Balcarce");
        domicilio2.setNumero(451);
        domicilio2.setProvincia("Bs Aires");
        domicilio2.setLocalidad("La plata");

        //se crea un paciente
        Paciente paciente2 = new Paciente();
        //se setea el valor del id del paciente guardado
        paciente2.setId(paciente.getId());
        paciente2.setApellido("Ibanez");
        paciente2.setNombre("Jorge");
        paciente2.setDni("4525");
        paciente2.setFechaIngreso(LocalDate.of(2021, 04, 12));
        // se setea domicilio dentro de paciente
        paciente2.setDomicilio(domicilio2);

        //se ejectuta el metodo para actualizar paciente
        Paciente pacienteActualizado = pacienteRepository.save(paciente2); // el metodo save crea un paciente y lo actualiza en caso de que ya exista

        //Se comprueba primero que el paciente no sea nulo
        assertThat(pacienteActualizado).isNotNull();

        //se comprueba que el id del paciente actualizado sea igual al paciente anterior, osea que conserve el id
        Assertions.assertEquals(paciente.getId(), pacienteActualizado.getId());

        log.debug("Finaliza correctamente el test para actualizar paciente");
    }

    @DisplayName("Eliminar paciente")
    @Test
    public void eliminarPaciente(){

        //se logea informacion al principio y final del test
        log.debug("Inicia test para eliminar paciente");

        //guardo un paciente
        pacienteRepository.save(paciente);

        // elimino el paciente
        pacienteRepository.deleteById(paciente.getId());

        // busco para ver si existe el paciente
        Optional<Paciente> pacienteBuscado = pacienteRepository.findById(paciente.getId());

        //comprobaciones
        assertThat(pacienteBuscado).isEmpty(); // corrobora que pacienteBuscado esta vacio

        log.debug("Finaliza correctamente el test para eliminar paciente");
    }

}
