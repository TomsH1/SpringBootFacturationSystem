package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.Factura;

import java.util.List;

public interface FacturaServices {
  public List<Factura> findAllInvoices();
  public Factura findFacturaById(Long id);
  public void saveFactura(Factura factura);
  public void deleteFacturaById(Long id);

}
