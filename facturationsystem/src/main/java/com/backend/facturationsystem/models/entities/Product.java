package com.backend.facturationsystem.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "Productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @NotEmpty(message = "Tiene que darle un nombre al producto")
    @Column(name ="nombre_producto")
    @Getter @Setter
    private String producto;

    @Min(value = 1, message = "Tiene que darle un precio al producto")
    @Getter @Setter
    private Double precio;

    @NotEmpty(message = "Tiene que darle una descripción al producto")
    @Column(length = 2000)
    @Getter @Setter
    private String descripcion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    @Getter @Setter
    private Date createAt;

    @PrePersist
    private void createAtPrePersist(){
        this.createAt = new Date();
    }

}
