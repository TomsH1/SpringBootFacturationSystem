package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.Product;

import java.util.List;

public interface ProductServices {
  public List<Product> getProducts();

  public List<Product> getProductByName(String productName);
}
