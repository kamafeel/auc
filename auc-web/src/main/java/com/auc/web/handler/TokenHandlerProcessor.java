package com.auc.web.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class TokenHandlerProcessor implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(HandlerType.class);
    Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
    HashMap<String, Class> tokenHandlerMap = new HashMap<>(entitySet.size());
    for (Map.Entry<String, Object> entry : entitySet) {
      Class<? extends Object> clazz = entry.getValue().getClass();//获取bean对象
      HandlerType handlerType = AnnotationUtils.findAnnotation(clazz, HandlerType.class);
      if (tokenHandlerMap.containsKey(handlerType.value())) {
        throw new RuntimeException("init TokenHandlerContext failed, repeat defined handler type "
            + handlerType.value());
      }
      tokenHandlerMap.put(handlerType.value(), clazz);
    }
    TokenHandlerContext.init(tokenHandlerMap, applicationContext);
  }
}
