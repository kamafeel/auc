package com.auc.service.impl;

import com.auc.dao.Client;
import com.auc.mapper.ClientMapper;
import com.auc.service.IClientService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 第三方系统信息 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-18
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

  /**
   * 清空缓存
   */
  @CacheEvict(value="clientCache",allEntries=true)
  @Override
  public void clearCache() {
  }

  @Override
  @Cacheable("clientCache")
  public Client getByClientId(String clientId) {
    return baseMapper.selectOne(Wrappers.<Client>lambdaQuery().eq(Client::getClientId, clientId));
  }

  @Override
  @Cacheable("clientCache")
  public List<Client> getClientsByName(String keyword) {
    return baseMapper.getClientsByName(keyword);
  }
}
