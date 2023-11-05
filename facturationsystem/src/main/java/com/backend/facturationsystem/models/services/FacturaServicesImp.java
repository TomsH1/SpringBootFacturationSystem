package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.Factura;
import com.backend.facturationsystem.models.repositories.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FacturaServicesImp implements FacturaServices {
  @Autowired
  private FacturaRepository repository;

  @Override
  public List<Factura> findAllInvoices() {
    return (List<Factura>) repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Factura findFacturaById(Long id) {
	return this.repository.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public void saveFactura(Factura factura) {
    this.repository.save(factura);
  }

  @Override
  @Transactional
  public void deleteFacturaById(Long id) {
	this.repository.deleteById(id);
  }
}
