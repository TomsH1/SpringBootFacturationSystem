package com.backend.facturationsystem.models.repositories;

import com.backend.facturationsystem.models.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

  List<Product> findProductByProductoContainingIgnoreCase(String producto);
}
