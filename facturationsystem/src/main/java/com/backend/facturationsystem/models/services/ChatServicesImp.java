package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.documents.Message;
import com.backend.facturationsystem.models.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServicesImp implements ChatServices{

  @Autowired
  MessageRepository repository;


  @Override
  public List<Message> loadMessages() {
	return this.repository.findFirst15ByOrderByShippingDateDesc();
  }

  @Override
  public Message saveMessage(Message message) {
	return this.repository.save(message);
  }
}
