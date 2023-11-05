package com.backend.facturationsystem.models.entities;

import com.backend.facturationsystem.models.enums.RoleList;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    Long Id;

    @Enumerated(EnumType.STRING)
    @Column(name ="rol")
    @NotEmpty
    @Getter
    @Setter
    private RoleList role;

}
