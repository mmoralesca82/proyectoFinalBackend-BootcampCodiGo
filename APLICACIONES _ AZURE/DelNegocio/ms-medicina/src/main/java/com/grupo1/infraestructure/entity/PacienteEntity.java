package com.grupo1.infraestructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;


@Entity
@Table(name="paciente")
@Getter
@Setter
public class PacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;
    @Column(nullable = false, length = 40)
    private String nombre;
    @Column(nullable = false, length = 40)
    private String apellido;
    @Column(nullable = false, length = 15)
    private String numDocumento;
    @Column(nullable = false)
    private Date fechaNacimiento;
    @Column(nullable = false)
    private String genero;
    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false, length = 25)
    private String usuarioCreacion;
    @Column(nullable = false)
    private Timestamp fechaCreacion;
    @Column(length = 25)
    private String usuarioModificacion;
    private Timestamp fechaModificacion;
    @Column(length = 25)
    private String usuarioEliminacion;
    private Timestamp fechaEliminacion;
    @Column(nullable = false)
    private Boolean estado;


//    @JsonIgnore
//    @JsonIgnoreProperties("direccion")
//    @OneToOne(optional = false)
//    @JoinColumn(name="id_direccion_fk", nullable = false)
//    private DirecccionEntity direccion;

//    @JsonIgnore
//    @JsonIgnoreProperties("contactoEmergencia")
//    @ManyToMany
//    @JoinTable(name="paciente_contacto",
//            joinColumns = @JoinColumn(name="id_paciente_fk", nullable = false),
//            inverseJoinColumns = @JoinColumn(name="id_contacto_emerg_fk", nullable = false))
//    private Set<ContactoEmergenciaEntity> contactoEmergencia;

//    @JsonIgnoreProperties("consulta")
//    @OneToMany(mappedBy = "paciente")
//    private Set<ConsultaMedicaEntity> consulta;

}
