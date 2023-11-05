package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.Client;
import com.backend.facturationsystem.models.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientServices {

    public Page<Client> clientPager(Pageable pageable); // método para la paginación del sitio.
    public List<Client> findAllClients();
    public Client findClientById(Long id);
    public  Client saveClient(Client client);
    public void deleteClientById(Long id);
    List<Region> findAllRegions();

}
