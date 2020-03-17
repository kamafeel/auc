package com.auc.service;

import com.auc.dao.Client;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 第三方系统信息 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-18
 */
public interface IClientService extends IService<Client> {
  void clearCache();
  Client getByClientId(String clientId);
  List<Client> getClientsByName(String keyword);

}
