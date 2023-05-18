package com.backend.facturationsystem.controller;

import com.backend.facturationsystem.models.entities.Client;
import com.backend.facturationsystem.models.services.ClientServices;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/Api")
public class ClientController {
    @Autowired
    private ClientServices clientServices;

    @GetMapping("/Clients/page/{page}")
    public Page<Client> getAllClients(@PathVariable int page){
        return clientServices.clientPager(PageRequest.of(page, 5));
    }
    @GetMapping("/Clients")
    public ResponseEntity<?> getAllClients(){
        List<Client> clients;
        Map<String, Object> response = new HashMap<>();
        try{
            clients = clientServices.findAllClients();
        }catch(DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error inesperado en el servidor, por favor intente más tarde");
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            System.out.println("response = " + response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }
    @GetMapping("/Clients/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){
        Client client = null;
        Map<String, Object> response = new HashMap<>();

        try {
            client = clientServices.findClientById(id);
        }catch (DataAccessException error){
            System.out.println(error);
            response.put("mensaje", "Ha ocurrido un error inesperado en el servidor");
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(client == null){
            response.put("mensaje", "El cliente con el ID: ".concat(id.toString()).concat(" No existe"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @PostMapping("/Clients")
    public ResponseEntity<?> createNewClient(@Valid @RequestBody Client client, BindingResult requestForm){
        Client request;
        Map<String, Object> response = new HashMap<>();
        if(requestForm.hasErrors()){
            List<String> validationErrorList = new ArrayList<>();
            for(FieldError error: requestForm.getFieldErrors()){
                validationErrorList.add("Field: '"+error.getField()+"' "+error.getDefaultMessage());
            }
            response.put("mensaje", "La información del cliente es invalida: "+validationErrorList);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            request = clientServices.saveClient(client);
        }catch (DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error interno del servidor al momento de guardar el usuario, "
                    .concat("por favor verifique sus datos e intente nuevamente"));
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido creado con éxito!");
        response.put("cliente", request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/Clients/{id}")
    public ResponseEntity<?> updateClientById(
            @Valid
            @RequestBody
            Client requestBody,
            BindingResult requestForm,
            @PathVariable
            Long id
    ){

        Client client;
        Map<String, Object> response = new HashMap<>();
        if(requestForm.hasErrors()){
            List<String> validationErrorList = requestForm.getFieldErrors().stream().map(
                    error -> "Field '"+error.getField()+"' "+error.getDefaultMessage()).toList();
            response.put("mensaje", "La información del cliente es invalida: "+validationErrorList);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            client = clientServices.findClientById(id);
        }catch (DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error interno del servidor al momento de guardar el usuario, "
                    .concat("por favor intente nuevamente"));
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(client == null){
            response.put("mensaje", "el cliente solicitado no existe");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        client.setNombre(requestBody.getNombre());
        client.setApellido(requestBody.getApellido());
        client.setEmail(requestBody.getEmail());

        return new ResponseEntity<Client>(clientServices.saveClient(client), HttpStatus.CREATED);
    }

    @DeleteMapping("/Clients/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            clientServices.deleteClientById(id);
        }catch(DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error inesperado en el servidor al intentar eliminar el usuario, "
                    .concat("por favor intente nuevamente"));
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El usuario con el id ".concat(id.toString()).concat(" Ha sido eliminado con éxito"));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
