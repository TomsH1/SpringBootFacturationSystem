package com.backend.facturationsystem.models.repositories;

import com.backend.facturationsystem.models.documents.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
	public List<Message> findFirst15ByOrderByShippingDateDesc();
}
