package com.Integrador.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
@Table(name = "domicilios")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Getter
@Setter
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer numero;
    @Column
    private String calle;
    @Column
    private String localidad;
    @Column
    private String provincia;


}
