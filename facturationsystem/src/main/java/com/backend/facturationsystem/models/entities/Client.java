package com.backend.facturationsystem.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clientes")
public class Client implements Serializable {

    @Id
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

//    @PrePersist
//    public void datePrePersist(){
//        this.createAt = new Date();
//    }

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

