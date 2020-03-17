package com.auc.mapper;

import com.auc.dao.Privilege;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
public interface PrivilegeMapper extends BaseMapper<Privilege> {

    List<String> getRelatedRoleCode(Integer privilegeId);

    List<Privilege> selectByRoleId(Integer roleId);
}
