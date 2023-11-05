package com.backend.facturationsystem.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Clientes")
public class Client implements Serializable {

    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long Id;

    @Getter
    @Setter
    @NotNull
    @Length(min = 3)
    private String nombre;

    @Getter
    @Setter
    @NotNull
    @Length(min = 3)
    private String apellido;

    @Getter
    @Setter
    @NotNull
    @Length(min = 10)
    @Email
    @Column(unique = true)
    private String email;

    @Column(name="create_at")
    @Getter
    @Setter
    @NotNull(message = "No puede estar vació")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Getter
    @Setter
    private String image;

    //Excluir propiedades del proxy de la clase Región del JSON
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region", nullable = true)
    @Getter
    @Setter
    private Region region;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "client"})
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private List<Factura> facturas;
    Client(){
        this.facturas = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Client{" +
                "Id=" + Id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}

