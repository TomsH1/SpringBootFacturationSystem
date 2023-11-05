package com.backend.facturationsystem.controller;

import com.backend.facturationsystem.models.entities.Factura;
import com.backend.facturationsystem.models.services.FacturaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/Api")
public class FacturaRestController {
  @Autowired
  FacturaServices services;

  @GetMapping("/Invoices")
  ResponseEntity<?> getAllInvoices(){
	List<Factura> facturas;
	Map<String, Object> response = new HashMap<>();
	try{
	  facturas = services.findAllInvoices();
	}catch(DataAccessException error){
	  response.put("mensaje", "Ha ocurrido un error inesperado en el servidor, por favor intente más tarde");
	  response.put("error: ", Objects.requireNonNull(error.getMessage())
			  .concat(error.getMostSpecificCause().getMessage()));
	  return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return ResponseEntity.status(HttpStatus.OK).body(facturas);
  }

  @GetMapping("/Invoices/{id}")
  ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
	Factura factura = null;
	HashMap<String, Object> response = new HashMap<>();
	try {
	  factura = services.findFacturaById(id);
	} catch (DataAccessException error) {
	  response.put("mensaje", "Ha ocurrido un error de conexión con el servidor");
	  response.put("error: ", Objects.requireNonNull(error.getMessage())
			  .concat(error.getMostSpecificCause().getMessage()));
	  return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	if (factura == null) {
	  response.put("mensaje", "La factura con el ID: ".concat(id.toString())
			  .concat(" No existe"));
	  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	return new ResponseEntity<>(factura, HttpStatus.OK);
  }

  @PostMapping("/Invoices")
  ResponseEntity<?> createInvoice(@Valid @RequestBody Factura factura, BindingResult requestForm){
	HashMap<String, Object> response = new HashMap<>();
	if(requestForm.hasErrors()){
	  List<String> validationResult = requestForm.getFieldErrors().stream()
		  	.map(error -> "Field: '"+error.getField()+"' "+error.getDefaultMessage()).toList();
	  response.put("mensaje", "La información de la factura es invalida: "+validationResult);
	  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	try {
	  services.saveFactura(factura);
	}catch (DataAccessException error){
	  response.put("mensaje", "Ha ocurrido un error interno del servidor al intentar de guardar la factura: "
			  .concat("por favor verifique sus datos e intente nuevamente"));
	  response.put("error: ", Objects.requireNonNull(error.getMessage())
			  .concat(error.getMostSpecificCause().getMessage()));
	  return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	response.put("mensaje", "La factura se ha guardado con éxito");
	return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/Invoices/{id}")
  ResponseEntity<?> deleteInvoice(@PathVariable Long id){
	HashMap<String, Object> response = new HashMap<>();
	try {
	  services.deleteFacturaById(id);
	}catch(DataAccessException error){
	  response.put("mensaje", "Ha ocurrido un error interno del servidor al intentar eliminar la factura, "
			  .concat("por favor verifique sus datos e intente nuevamente"));
	  response.put("error", Objects.requireNonNull(error.getMessage())
			  .concat(error.getMostSpecificCause().toString()));
	  return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	response.put("mensaje", "La factura con el id: "
			.concat(id.toString()).concat(" ha sido eliminado correctamente"));
	return new ResponseEntity<>(response, HttpStatus.OK);
  }
}

