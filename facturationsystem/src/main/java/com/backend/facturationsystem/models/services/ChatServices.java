package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.documents.Message;

import java.util.List;

public interface ChatServices {
  public List<Message> loadMessages();
  public Message saveMessage(Message message);
}
