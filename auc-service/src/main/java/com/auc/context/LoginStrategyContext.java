package com.auc.context;

import com.auc.common.enums.ClientEnum;
import com.auc.common.utils.SpringContextUtil;
import com.auc.dao.User;
import com.auc.domain.dto.requestdto.LoginBaseRequestDTO;
import com.auc.domain.dto.requestdto.UserDomainLoginRequestDTO;
import com.auc.domain.dto.responsedto.LoginInfoResponseDTO;
import com.auc.dubbo.user.dao.BaseUser;
import com.auc.service.IClientLoginStrategyService;
import com.auc.service.impl.strategy.AUCLoginStrategyServiceImpl;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 第三方登录策略（特殊处理）上下文
 */
@Component
@Slf4j
public class LoginStrategyContext {

  private Map<String, Class<?>> iClientLoginStrategyServiceMap;

  @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
  public void init() {
    iClientLoginStrategyServiceMap = new HashedMap<>();
    iClientLoginStrategyServiceMap.put(ClientEnum.AUC.getCode(), AUCLoginStrategyServiceImpl.class);
  }

  // 对外暴露的特别处理,异步
  @Async("strategyExecutor")
  public <T extends LoginBaseRequestDTO> void loginStrategy(T dto, User user, String client_id) {
    Class<?> providerClazz = iClientLoginStrategyServiceMap.get(client_id);
    if(providerClazz == null){
      return;
    }
    Object bean = SpringContextUtil.getBean(providerClazz);
    if (bean instanceof IClientLoginStrategyService) {
      IClientLoginStrategyService strategyService = (IClientLoginStrategyService) bean;
      strategyService.loginStrategy(dto, user, client_id);
    }
  }
}