package com.auc.service.impl;

import com.auc.dao.MenuInfo;
import com.auc.mapper.MenuInfoMapper;
import com.auc.service.IMenuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-28
 */
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements IMenuInfoService {

  @Override
  @Cacheable("roleCache")
  public List<Integer> getMenuIdsByRoles(List<String> roles) {
    return baseMapper.getMenuIdsByRoles(roles);
  }
}
