package com.backend.facturationsystem.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long Id;
    @Getter
    @Setter
    @Length(max = 16)
    private String username;
    @Getter
    @Setter
    @Length(max = 16)
    private String nombre;
    @Getter
    @Setter
    @Length(max = 16)
    private String apellido;
    @Getter
    @Setter
    @Length(min = 10)
    @Email
    private String email;
    @Getter
    @Setter
    private Boolean enabled;
    @Getter
    @Setter
    //: para intellij IDEA
    /*@Size.List ({
            @Size(min=8, message="La contraseña no puede ser menor a {min} caracteres"),
            @Size(max=60, message="La contraseña no puede ser mayor a {max} caracteres")
    })*/
    //: para visual studio code
    @Size(min=8, message="La contraseña no puede ser menor a {min} caracteres")
    @Size(max=60, message="La contraseña no puede ser mayor a {max} caracteres")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "rol_id"})}
    )
    @Getter
    @Setter
    private List<Role> roles;

}
