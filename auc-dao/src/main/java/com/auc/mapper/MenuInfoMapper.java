package com.auc.mapper;

import com.auc.dao.MenuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-28
 */
public interface MenuInfoMapper extends BaseMapper<MenuInfo> {

  @Select("SELECT menu_id FROM auc_re_role_menu_relation rm " +
      "LEFT JOIN auc_re_user_roles_relation ur ON rm.role_id = ur.role_id " +
      "WHERE ur.user_id = ${userId}")
  List<Integer> getMenuIdsByRoles(@Param("roles") List<String> roles);
}
