package com.auc.service;

import com.auc.dao.Privilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
public interface IPrivilegeService {

    void save(Privilege privilege);

    Privilege getById(Integer id);

    void deleteById(Integer id);

    List<String> getRelatedRoleCode(Integer privilegeId);

    void clearOperationCacheByPrivilegeId(Integer privilegeId);

    List<Privilege> selectByRoleId(Integer roleId);
}

