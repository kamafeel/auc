package com.auc.service.impl;

import com.auc.dao.Client;
import com.auc.dubbo.user.service.IBaseUserService;
import com.auc.mapper.ClientMapper;
import com.auc.service.ICacheService;
import com.auc.service.IClientService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 缓存实现类
 * @author zhangqi
 */
@Service
public class CacheServiceImpl implements ICacheService {

  @Reference(version = "${service.version.user}", check = false)
  private IBaseUserService baseUserService;

  @Override
  @Cacheable("userCache")
  public List<Map<String, Object>> getAllMergeAccounts() {
    return baseUserService.getAllMergeAccounts();
  }
}
