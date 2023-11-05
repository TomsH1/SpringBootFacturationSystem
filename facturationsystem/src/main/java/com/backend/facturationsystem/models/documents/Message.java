package com.backend.facturationsystem.models.documents;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document(collection = "messages")
public class Message {
  	@Id
    private String id;
  	private String text;
	private Long shippingDate;
	private String username;
	private String chatEvent;
	private String color;

}
