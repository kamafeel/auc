package com.auc.mapper;

import com.auc.dao.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色类型表 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-21
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

  /**
   * 获取用户角色信息
   *
   * @param userId
   */
  List<Role> getRoleInfoByUserId(Integer userId);

  List<Map<String, Object>> getAllRoleInfo();
}
