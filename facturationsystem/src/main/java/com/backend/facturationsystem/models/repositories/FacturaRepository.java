package com.backend.facturationsystem.models.repositories;

import com.backend.facturationsystem.models.entities.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends CrudRepository<Factura, Long> {
}
