package com.backend.facturationsystem.WebSocketConfig;

import org.springframework.context.annotation.Configuration;
  import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry config) {
	config.addEndpoint("/chat-websocket")
			.setAllowedOrigins("http://localhost:4200")
			.withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
	// Nombre del evento del chat
	config.enableSimpleBroker("/chat/");
	// Tipo de destino donde se envían los mensajes
	config.setApplicationDestinationPrefixes("/app");

  }
}
