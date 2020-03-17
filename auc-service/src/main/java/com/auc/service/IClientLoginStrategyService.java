package com.auc.service;

import com.auc.dao.User;
import com.auc.domain.dto.requestdto.LoginBaseRequestDTO;
import com.auc.domain.dto.requestdto.UserDomainLoginRequestDTO;
import com.auc.domain.dto.responsedto.LoginInfoResponseDTO;
import com.auc.dubbo.user.dao.BaseUser;

/**
 * 实现类不能使用 Async Cacheable 等注解
 */
public interface IClientLoginStrategyService {
  <T extends LoginBaseRequestDTO> void loginStrategy(T dto, User user,String client_id);
}
