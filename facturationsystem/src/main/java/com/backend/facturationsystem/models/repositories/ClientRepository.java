package com.backend.facturationsystem.models.repositories;

import com.backend.facturationsystem.models.entities.Client;
import com.backend.facturationsystem.models.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("from Region")
    //Buscar todas las regiones en la tabla clientes
    List<Region> findAllRegions();
}
