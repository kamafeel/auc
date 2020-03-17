package com.auc.service.impl;

import com.auc.dao.Role;
import com.auc.mapper.RoleMapper;
import com.auc.service.IRoleCacheService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色类型表 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-17
 */
@Service
public class RoleCacheServiceImpl extends ServiceImpl<RoleMapper, Role> implements
    IRoleCacheService {

  /**
   * 清空缓存
   */
  @CacheEvict(value="roleCache",allEntries=true)
  @Override
  public void clearCache() {

  }

  @Override
  @Cacheable("roleCache")
  public List<Map<String, Object>> getAllRoleInfo(){
    return baseMapper.getAllRoleInfo();
  }
}
