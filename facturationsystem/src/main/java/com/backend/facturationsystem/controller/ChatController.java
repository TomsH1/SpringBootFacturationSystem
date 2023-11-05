package com.backend.facturationsystem.controller;

import com.backend.facturationsystem.models.documents.Message;
import com.backend.facturationsystem.models.services.ChatServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
public class ChatController {

  @Autowired
  SimpMessagingTemplate messagingTemplate;
  @Autowired
  ChatServices service;

  private static Logger log = LoggerFactory.getLogger(ChatController.class);
  @MessageMapping("/messages")
  @SendTo("/chat/messages")
  public Message ReceiveMessages(Message message, @Headers Map<String, Object> headers){
//	String requestHeaders = (String) headers.get("Authorization");
//	System.out.println("Authorization header = " + requestHeaders);
	String[] colors = {"red", "green", "purple", "orange", "rebeccapurple", "springgreen", "teal",
	"skyblue", "royalblue"};

	message.setShippingDate(new Date().getTime());
	if(message.getChatEvent().equals("NEW_USER")){
	  int randomColor = new Random().nextInt(colors.length);
	  message.setColor(colors[randomColor]); //: Añadir un color aleatorio
	  message.setText(message.getUsername().concat(" se ha unido a la sala"));
	}
	else if(message.getChatEvent().equals("NEW_MESSAGE")){
		this.service.saveMessage(message);
	}
	return message;
  }

  @MessageMapping("/writing")
  @SendTo("/chat/writing")
  public String userIsWriting(String username){
	return username.concat(" Está escribiendo...");
  }

  @MessageMapping("/history-messages")
  public void getHistoryMessages(String clientId){
	this.messagingTemplate.convertAndSend("/chat/history/"
			.concat(clientId), service.loadMessages());
  }
}
