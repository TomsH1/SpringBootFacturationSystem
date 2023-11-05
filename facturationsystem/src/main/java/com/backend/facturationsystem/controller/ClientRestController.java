package com.backend.facturationsystem.controller;

import com.backend.facturationsystem.models.entities.Client;
import com.backend.facturationsystem.models.entities.Region;
import com.backend.facturationsystem.models.services.ClientServices;
import com.backend.facturationsystem.models.services.FileServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@CrossOrigin(originPatterns = {"http://localhost:4200"})
@RestController
@RequestMapping("/Api")
public class ClientRestController {

    private final Logger logger = LoggerFactory.getLogger(ClientRestController.class);
    @Autowired
    //? Servicio de persistencia de la tabla clientes
    private ClientServices clientServices;
    @Autowired
    //? Servicio para la administración de archivos
    private FileServices fileServices;
    private MultipartFile image;

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

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
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

    @Secured("ROLE_ADMIN")
    @PostMapping("/Clients")
    public ResponseEntity<?> createNewClient(@Valid @RequestBody Client client, BindingResult requestForm){
        Client request;
        Map<String, Object> response = new HashMap<>();
        if(requestForm.hasErrors()){
            List<String> validationErrorList = new ArrayList<>();
            validationErrorList = requestForm.getFieldErrors().stream().map(
                    error -> "Field '"+error.getField()+"' "+error.getDefaultMessage()).toList();
            response.put("mensaje", "La información del cliente es invalida: "+validationErrorList);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            request = clientServices.saveClient(client);
        }catch (DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error interno del servidor al intentar de guardar el usuario, "
                    .concat("por favor verifique sus datos e intente nuevamente"));
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido creado con éxito!");
        response.put("cliente", request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/Clients/{id}")
    public ResponseEntity<?> updateClientById(@Valid @RequestBody Client requestBody, BindingResult requestForm,
    @PathVariable Long id
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
        client.setRegion(requestBody.getRegion());

        return new ResponseEntity<Client>(clientServices.saveClient(client), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/Clients/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable Long id) throws Exception {
        Map<String, Object> response = new HashMap<>();
        Client client = null;
        try{
            client = clientServices.findClientById(id);
            String clientImage = client.getImage();
            fileServices.deleteImageResource(clientImage);
            clientServices.deleteClientById(id);
        }catch(DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error inesperado en el servidor al intentar eliminar el usuario, "
                    .concat("por favor intente nuevamente"));
            response.put("error: ", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("El usuario con el id ".concat(id.toString()).concat(" Ha sido eliminado con éxito"));
        response.put("mensaje", "El usuario con el id ".concat(id.toString()).concat(" Ha sido eliminado con éxito"));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("Clients/upload/img")
    public ResponseEntity<?> uploadClientImage(@RequestParam("image") MultipartFile image, @RequestParam("id") Long id)
    throws IOException
    {
        Map<String, Object> response = new HashMap<>();
        Client client = null;
        String nameImage = null;
        try {
            // buscar por el id del request
            client = clientServices.findClientById(id);
        }catch (DataAccessException error){ //Capturar excepciones de la conexión a la base de datos
            response.put("mensaje", "Ha ocurrido un error interno del servidor por favor intente en otro momento");
            response.put("error", Objects.requireNonNull(error.getMessage())
                    .concat(error.getCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(image.isEmpty()){
            response.put("mensaje", "Debe seleccionar un imagen valida");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String clientImage = client.getImage();
        // existe una ruta de imagen anterior almacenada en el objeto
        if(clientImage != null && !clientImage.isEmpty()){
            //Eliminar el nombre de imagen actual
            fileServices.deleteImageResource(clientImage);
        }
        // Guardar el archivo y retornar el nombre
        nameImage = fileServices.copy(image);
        System.out.println("nameImage: ".concat(nameImage));
        //Guardar el nombre de la imagen en el objeto Cliente
        client.setImage(nameImage);

        try {
            clientServices.saveClient(client);
        }catch(DataAccessException error){
            response.put("mensaje", "No se ha podido guardar la imagen");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("client", client);
        response.put("mensaje", "la imagen ".concat(Objects.requireNonNull(image.getOriginalFilename()))
                .concat(" Se ha guardado correctamente"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("Clients/upload/img/{clientImageName:.+}")
    public ResponseEntity<?> getClientImage(@PathVariable String clientImageName) {
        Map<String, Object> response = new HashMap<>();
        Resource resource = null;

        try {
            //? Obtener el recurso de URL con la imagen del cliente
            resource = fileServices.getImageResource(clientImageName);
        } catch (MalformedURLException error) {
            throw new RuntimeException(error);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+clientImageName+"\"");
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("Clients/regions")
    ResponseEntity<?> getRegions(){
        Map<String, Object> response = new HashMap<>();
        List<Region> regions = null;
        try {
            regions = clientServices.findAllRegions();
        }catch(DataAccessException error){
            response.put("mensaje", "Ha ocurrido un error interno del servidor");
            response.put("error", Objects.requireNonNull(error.getMessage())
                    .concat(error.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK).body(regions);
    }

}
