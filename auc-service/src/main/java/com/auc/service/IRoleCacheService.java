package com.auc.service;

import com.auc.dao.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色类型表 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-17
 */
public interface IRoleCacheService extends IService<Role> {
  void clearCache();
  List<Map<String, Object>> getAllRoleInfo();
}
