package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.Client;
import com.backend.facturationsystem.models.entities.Region;
import com.backend.facturationsystem.models.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServicesImp implements ClientServices{
    @Autowired
    private ClientRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<Client> clientPager(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public List<Client> findAllClients() {
        return repository.findAll();
    }

    @Override
    public Client findClientById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Client saveClient(Client client) {
        return repository.save(client);
    }

    @Override
    public void deleteClientById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Region> findAllRegions() {
        return repository.findAllRegions();
    }
}
