package com.backend.facturationsystem.controller;

import com.backend.facturationsystem.models.entities.Product;
import com.backend.facturationsystem.models.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/Api")
public class ProductRestController {

  @Autowired
  ProductServices services;
  @GetMapping("/Products")
  public ResponseEntity<?> getProducts(){
	HashMap<String, Object> response = new HashMap<>();
	List<Product> products = null;
	try {
	  products = services.getProducts();
	}catch(DataAccessException exception){
	  response.put("mesnaje", "Ha ocurrido un error interno del servidor: ");
	  response.put("error", Objects.requireNonNull(exception.getMessage())
			  .concat(exception.getMostSpecificCause().toString()));
	  return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return ResponseEntity.status(HttpStatus.OK).body(products);
  }

  @GetMapping("/Products/{productName}")
  public ResponseEntity<?> getProductByProductName(@PathVariable String productName){
	HashMap<String, Object> response = new HashMap<>();
	List<Product> product = null;
	try {
	  product = this.services.getProductByName(productName);
	}catch (DataAccessException exception){
	  response.put("mensaje", "Ha ocurrido un error interno del servidor, no se ha podido obtener el producto: ");
	  response.put("error", Objects.requireNonNull(exception.getMessage())
			  .concat(exception.getMostSpecificCause().toString()));
	  return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	if(productName == null){
	  response.put("mensaje", "El producto solicitado no existe");
	  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	return ResponseEntity.status(HttpStatus.OK).body(product);
  }
}
