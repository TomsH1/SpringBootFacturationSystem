package com.backend.facturationsystem.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items_factura")
public class ItemFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long Id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @Getter @Setter
    private Product product;

    @Min(value = 1, message = "La cantidad no puede ser cero")
    @NotNull
    @Getter @Setter
    private Integer cantidad;

    @Getter @Setter
    @Min(value = 1, message = "El importe no puede ser cero")
    private Double importe;

    public Double calcularImporte(){
	  return this.cantidad.doubleValue() * this.product.getPrecio();
    }
}
