package com.auc.service;

import com.auc.common.AppException;
import com.auc.domain.dto.requestdto.MPLoginRequestDTO;
import com.auc.domain.dto.requestdto.ProxyReqRequestDTO;
import com.auc.domain.dto.requestdto.SmsCodeRequestDTO;
import com.auc.domain.dto.requestdto.UserDomainLoginRequestDTO;
import com.auc.domain.dto.requestdto.UserSystemLoginRequestDTO;
import com.auc.domain.dto.responsedto.DomainLoginInfoResponseDTO;
import com.auc.domain.dto.responsedto.LoginInfoResponseDTO;
import com.auc.domain.dto.responsedto.MpLoginInfoResponseDTO;
import java.util.List;
import java.util.Map;

/**
 * 登录服务
 */
public interface LoginService {

  /**
   * 域登录
   * @param dto
   * @return
   */
  DomainLoginInfoResponseDTO domainLogin(UserDomainLoginRequestDTO dto);

  LoginInfoResponseDTO appAccountLogin(UserSystemLoginRequestDTO dto);

  <T extends LoginInfoResponseDTO> T proxyLogin(ProxyReqRequestDTO dto);

  /**
   * 手机验证码登录
   * @param dto
   * @return
   */
  MpLoginInfoResponseDTO mpLogin(MPLoginRequestDTO dto);

  /**
   * 第三方后端调用失效token
   * @return
   */
  boolean inValid(String userId);

  /**
   * 发送手机验证码
   * @param dto
   * @return
   */
  boolean sendSMSCode(SmsCodeRequestDTO dto);

  void checkCode(String code,String mobilePhome) throws AppException;
}
