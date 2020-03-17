package com.auc.mapper;

import com.auc.dao.Operational;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 前端功能操作 Mapper 接口
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
public interface OperationalMapper extends BaseMapper<Operational> {

    List<String> selectByRoleCode(String roleCode);

    void savePrivilegeOperationalRelation(@Param("privilegeId") Integer privilegeId, @Param("operationalIds") List<Integer> operationalIds);

    void deleteRelationByPrivilegeId(Integer privilegeId);

}
