package com.auc.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket基础配置
 *
 * @author zhangqi
 */
@Configuration
//开启使用STOMP协议来传输基于代理的消息
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  //用来配置消息代理，由于我们是实现推送功能
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  /**
   * 注册STOMP协议的节点，并指定映射的URL
   * @param registry
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    //来注册STOMP协议节点，同时指定使用SockJS协议
    registry.addEndpoint("/auc-websocket").withSockJS();
  }

}