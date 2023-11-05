package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.Product;
import com.backend.facturationsystem.models.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServicesImp implements ProductServices{
  @Autowired
  ProductRepository repository;
  @Override
  public List<Product> getProducts() {
	return (List<Product>)this.repository.findAll();
  }

  @Override
  public List<Product> getProductByName(String productName) {
    return this.repository.findProductByProductoContainingIgnoreCase(productName);
  }
}
