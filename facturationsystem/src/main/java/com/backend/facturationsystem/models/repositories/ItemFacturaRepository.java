package com.backend.facturationsystem.models.repositories;

import com.backend.facturationsystem.models.entities.ItemFactura;
import org.springframework.data.repository.CrudRepository;

public interface ItemFacturaRepository extends CrudRepository<ItemFactura, Long> {
}
