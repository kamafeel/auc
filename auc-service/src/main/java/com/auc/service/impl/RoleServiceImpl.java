package com.auc.service.impl;

import com.auc.dao.Role;
import com.auc.mapper.RoleMapper;
import com.auc.service.IRoleCacheService;
import com.auc.service.IRoleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色类型表 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

  @Autowired
  private IRoleCacheService roleCacheService;

  @Cacheable("roleCache")
  @Override
  public Role getRoleByCode(String roleCode) {
    return baseMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleCode, roleCode));
  }

  @Override
  public List<Role> getRoleInfoByUserId(Integer userId) {
    List<Map<String, Object>> roles = roleCacheService.getAllRoleInfo().stream().filter(r->
        r.get("user_id").toString().equalsIgnoreCase(userId.toString())).collect(Collectors.toList());
    List<Role> roleList = new ArrayList<>();
    if(roles!=null && !roles.isEmpty()){
      roles.forEach(r->{
        Role role = new Role();
        role.setRoleName(r.get("role_name").toString());
        role.setRoleCode(r.get("role_code").toString());
        roleList.add(role);
      });
    }else{
      Role r = new Role();
      r.setRoleName("USER");
      r.setRoleCode("USER");
      //默认添加user权限
      roleList.add(r);
    }
    return roleList;
  }
}
