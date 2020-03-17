package com.auc.service.impl;

import com.auc.common.constants.CacheKeyConstants;
import com.auc.dao.Privilege;
import com.auc.mapper.PrivilegeMapper;
import com.auc.service.IPrivilegeService;
import com.auc.service.RedisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
@Service
public class PrivilegeServiceImpl implements IPrivilegeService {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public void save(Privilege privilege) {
        // 1、保存
        if (privilege.getId() != null) {
            privilegeMapper.updateById(privilege);
        } else {
            privilegeMapper.insert(privilege);
        }
        // 2、找到相关联角色的编码，清除对应角色的缓存
        this.clearOperationCacheByPrivilegeId(privilege.getId());
    }

    @Override
    public Privilege getById(Integer id) {
        return privilegeMapper.selectById(id);
    }

    @Override
    public void deleteById(Integer id) {
        // 1、删除
        privilegeMapper.deleteById(id);
        // 2、找到相关联角色的编码，清除对应角色的缓存
        this.clearOperationCacheByPrivilegeId(id);
    }

    @Override
    public List<String> getRelatedRoleCode(Integer privilegeId) {
        return privilegeMapper.getRelatedRoleCode(privilegeId);
    }

    public void clearOperationCacheByPrivilegeId(Integer privilegeId) {
        List<String> roleCodes = privilegeMapper.getRelatedRoleCode(privilegeId);
        roleCodes.forEach(roleCode -> redisService.delete(CacheKeyConstants.OPERATIONAL_ROLE_CODE_PREFIX + roleCode));
    }

    @Override
    public List<Privilege> selectByRoleId(Integer roleId) {
        return privilegeMapper.selectByRoleId(roleId);
    }
}
