package com.backend.facturationsystem.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    //? Omitir la realación inversa
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "facturas"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @NotNull
    @Getter @Setter
    private Client client;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    @NotNull
    @Getter @Setter
    private List<ItemFactura> items;

    @Column(length = 400)
    @Nullable
    @NotEmpty
    @Getter @Setter
    private String descripcion;

    @Size(min = 1, message = "Debe añadir un título valido a la factura")
    @NotEmpty
    @Getter @Setter
    private String titulo;

    @Column(length = 400)
    @Getter @Setter
    private String observaciones;

    @Min(value = 1, message = "El total no puede ser cero")
    @NotNull
    @Getter @Setter
    private Double total;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date createAt;

    public Factura() {
        this.items = new ArrayList<>();
    }

    @PrePersist
    private void createAtPrePersist(){
        createAt = new Date();
    }

    public Double calcularTotal(){
        Double total = 0.00;
        for(ItemFactura item: items){
            total += item.calcularImporte();

        }
        return total;
    }
}
