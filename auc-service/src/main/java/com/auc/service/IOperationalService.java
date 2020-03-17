package com.auc.service;

import com.auc.dao.Operational;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 前端功能操作 服务类
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
public interface IOperationalService {

    /**
     * 保存
     * @param operational
     */
    void save(Operational operational);

    /**
     * 删除
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 用ID获取
     * @param id
     * @return
     */
    Operational getById(Integer id);

    /**
     * 根据角色编码获取该角色有哪些操作
     * @param roleCodes 角色编码
     * @return List<Operational>
     */
    List<String> getOperationalByRole(List<String> roleCodes);


    /**
     * 保存权限和操作关联关系
     * @param privilegeId
     * @param operationalId
     */
    void savePrivilegeOperationalRelation(Integer privilegeId, List<Integer> operationalIds);

}
