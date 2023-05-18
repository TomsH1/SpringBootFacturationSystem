package com.backend.facturationsystem.models.repositories;

import com.backend.facturationsystem.models.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {


}
