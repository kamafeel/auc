package com.auc.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;

@Slf4j
public class TokenHandlerContext {

  private static ApplicationContext applicationContext;

  private static HashMap<String, Class> tokenHandlerMap;

  public static TokenHandler getTokenHandler(String clientId) {
    Class key = tokenHandlerMap.get(clientId);
    return key != null ? (TokenHandler) applicationContext.getBean(key) : null;
  }

  public static void init(HashMap<String, Class> tokenHandlerMap, ApplicationContext applicationContext) {
    TokenHandlerContext.applicationContext = applicationContext;
    TokenHandlerContext.tokenHandlerMap = tokenHandlerMap;
  }
}
