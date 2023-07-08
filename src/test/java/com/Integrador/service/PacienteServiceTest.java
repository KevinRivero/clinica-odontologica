package com.Integrador.service;

import com.Integrador.entities.Domicilio;
import com.Integrador.entities.Paciente;
import com.Integrador.exception.BadRequestException;
import com.Integrador.repository.PacienteRepository;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    //creamos un logger para dar informacion durante el corrido de los tests
    private static final Logger log = Logger.getLogger(PacienteServiceTest.class);

    // Usamos la anotacion @Mock para hacer una simulacion de la clase repository
    @Mock
    private PacienteRepository pacienteRepository;


    //Se crea el objeto service con la anotacion @InjectMocks que inyecta el mock dentro de los metodos del service
    @InjectMocks
    private PacienteService pacienteService;

    // variables a usar durante el desarrollo de los tests
    Paciente paciente;
    Domicilio domicilio;
    Long ID;


    //------------------METODOS--------------------

    // Se crea un paciente para poder ejecturas las pruebas
    //este metodo se ejecuta antes de cada prueba
    //setup() Inicializa y setea valores a domicilio y paciente
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

        // se le asigna un valor de tipo Long
        ID = 1L;

        log.debug("Finaliza correctamente el metodo setup para crear pacientes");
    }

    @DisplayName("Service: Guardar paciente")
    @Test
    public void guardarPaciente(){
        //se logea informacion al principio y final del metodo
        log.debug("Service: Inicia test para guardar un paciente ");

        // si se ejecuta el metodo retornara paciente
        given(pacienteRepository.save(paciente)).willReturn(paciente);

        //se crea una variable para guardar lo que retorne del metodo del service
        Paciente pacienteGurdado = pacienteService.altaPaciente(paciente);

        //se verifica que paciente guardado no sea nula
        assertThat(pacienteGurdado).isNotNull();

        log.debug("Finaliza correctamente el test para guardar un paciente");

    }

    @DisplayName("Service: Listar pacientes")
    @Test
    public void listarPacientes(){
        //se logea informacion al principio y al final del medotdo
        log.debug("Service: Inicia test para listar pacientes");

        //se crea una lista para sumarle pacientes y devolverlo en el objeto simulado
        List<Paciente> listaPacientes = new ArrayList<>();

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

        //se guardan los pacientes dentro de la lista
        listaPacientes.add(paciente);
        listaPacientes.add(paciente2);

        // cuando se ejecute el metodo de pacienteRepository, debe devolver el listado de pacientes
        when(pacienteRepository.findAll()).thenReturn(listaPacientes);

        //se utiliza el metodo del service para listar los pacientes. Se guarda en otra lista
        List<Paciente> listaPacientesDelService = pacienteService.listarPacientes();

        // se verifica que la lista no sea nula y que el tamanio de la lista del service coincida con la del objeto simulado
        assertThat(listaPacientesDelService).isNotNull();
        Assertions.assertEquals(listaPacientesDelService.size(), 2);

        log.debug("Finaliza correctamente el test para listar pacientes");
    }

    @DisplayName("Service: Listar pacientes - Retorna lista vacia")
    @Test
    public void listarPacientesRetornaListaVacia(){
        //se logea informacion al principio y final del metodo
        log.debug("Service: Inicia test para listar pacientes y que arroje una lista vacia");


        //se simula el metodo del repository para que devuelva una lista vacia
        given(pacienteRepository.findAll()).willReturn(Collections.emptyList());

        //se utiliza el metodo del service para listar los pacientes
        List<Paciente> listaVacia = pacienteService.listarPacientes();

        //se corrobora que la lista este vacia y que la cantidad de elementos sea igual a 0
        assertTrue(listaVacia.isEmpty());
        assertEquals(listaVacia.size(), 0);

    }
    @DisplayName("Service: Buscar paciente por id")
    @Test
    public void buscarPacientePorId(){
        //se logea informacion al principio y final del metodo
        log.debug("Service: Inicia test para buscar paciente por id");

        //se le asigna al paciente un id de tipo Long
        paciente.setId(ID);

        //se simula el repository para que devuelva el paciente cuando se busque por id
        given(pacienteRepository.findById(paciente.getId())).willReturn(Optional.ofNullable(paciente));

        //se usa el service para buscar un paciente y se lo guarda en una variable
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPacientePorId(1L);

        // se  verifica que el paciente no sea nulo y que el id sea igual al paciente de prueba
        assertThat(pacienteBuscado).isNotNull();
        assertEquals(pacienteBuscado.get().getId(), paciente.getId());
    }

    @DisplayName("Service: Actualizar paciente")
    @Test
    void actualizarPaciente(){
        //se logea informacion al principio y final del test
        log.debug("Service: Inicia test para actualizar un paciente");

        // modifico algun valor de paciente para actualizar
        paciente.setId(ID);
        paciente.setNombre("Jorge");
        paciente.setApellido("Ramirez");

        // se simula el repository para que devuelva el paciente actualizado
        given(pacienteRepository.save(paciente)).willReturn(paciente);

        //se utiliza el metodo del service
        pacienteService.actualizarPaciente(paciente);

        //se corrobora que el paciente no sea nulo y que los valores de nombre y apellido coincidan con los indicados
        assertThat(paciente).isNotNull();
        assertEquals(paciente.getNombre(), "Jorge");
        assertEquals(paciente.getApellido(), "Ramirez");

        log.debug("Finaliza correctamente el test para actualizar pacientes");

    }
    @DisplayName("Service: Eliminar paciente")
    @Test
    void eliminarPaciente(){

        log.debug("Service: Inicia test para eliminar pacientes");

        // para simular que, cuando se ejecute el metodo especificado, no retorne nada
        willDoNothing().given(pacienteRepository).deleteById(ID);

        //para verificar que el metodo se haya ejecutado solo una vez
        verify(pacienteRepository,times(1)).deleteById(ID);


        log.debug("Finaliza correctamente el test para eliminar pacientes");

    }










}








