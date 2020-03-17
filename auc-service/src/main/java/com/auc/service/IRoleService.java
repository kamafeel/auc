package com.auc.service;

import com.auc.dao.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色类型表 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-17
 */
public interface IRoleService extends IService<Role> {
  Role getRoleByCode(String roleCode);
  List<Role> getRoleInfoByUserId(Integer userId);

}
