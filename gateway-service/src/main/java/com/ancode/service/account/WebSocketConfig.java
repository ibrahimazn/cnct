/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.ancode.service.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.converter.SmartMessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.ClassUtils;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * This Class WebSocketConfig is configure the MessageBroker and Stomp Client.
 * Achieving web socket concept through this configuration.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  @Override
  public boolean configureMessageConverters(List<MessageConverter> arg0) {
    WebSocketClient webSocketClient = new StandardWebSocketClient();
    WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    return true;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {

  }

//  @Bean
//  public WebSocketStompClient stompClient() {
//    StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
//    List<Transport> transports = new ArrayList<>();
//    transports.add(new WebSocketTransport(webSocketClient));
//    WebSocketStompClient client = new WebSocketStompClient(new SockJsClient(transports));
//    client.setMessageConverter(new MessageConverter() {
//
//      @Override
//      public Message<?> toMessage(Object payload, MessageHeaders headers) {
//        if (payload == null) {
//          return null;
//        }
//        if (headers != null) {
//          MessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(headers, MessageHeaderAccessor.class);
//          if (accessor != null && accessor.isMutable()) {
//            return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
//          }
//        }
//        return MessageBuilder.withPayload(payload).copyHeaders(headers).build();
//      }
//
//      @Override
//      public Object fromMessage(Message<?> message, Class<?> targetClass) {
//        Object payload = message.getPayload();
//        if (targetClass == null) {
//          return payload;
//        }
//        return (ClassUtils.isAssignableValue(targetClass, payload) ? payload : null);
//      }
//    });
//    return client;
//  }

}
