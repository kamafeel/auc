package com.auc.service.impl;

import com.auc.common.constants.CacheKeyConstants;
import com.auc.dao.Operational;
import com.auc.mapper.OperationalMapper;
import com.auc.service.IOperationalService;
import com.auc.service.IPrivilegeService;
import com.auc.service.RedisService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端功能操作 服务实现类
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
@Service
public class OperationalServiceImpl implements IOperationalService {

    @Autowired
    private OperationalMapper operationalMapper;

    @Autowired
    private IPrivilegeService privilegeService;

    @Autowired
    private RedisService redisService;

    @Override
    public void save(Operational operational) {
        if (operational.getId() == null) {
            if (operational.getParentId() == null) { // 如果没有父级ID，则将父ID设置为0，0表示顶级节点
                operational.setParentId(0);
            }
            operationalMapper.insert(operational);
        } else {
            operationalMapper.updateById(operational);
        }
        // 清理超管的缓存
        redisService.delete(CacheKeyConstants.OPERATIONAL_ROLE_CODE_PREFIX+"SUPER");
    }

    @Override
    public void deleteById(Integer id) {
        operationalMapper.deleteById(id);
        // 清理超管的缓存
        redisService.delete(CacheKeyConstants.OPERATIONAL_ROLE_CODE_PREFIX+"SUPER");
    }

    @Override
    public Operational getById(Integer id) {
        return operationalMapper.selectById(id);
    }

    @Override
    public List<String> getOperationalByRole(List<String> roleCodes) {
        List<String> operational = Lists.newArrayList();
        // 如果是角色编码列表为空，则返回空list
        if (roleCodes == null || roleCodes.isEmpty()) {
            return operational;
        }
        roleCodes.forEach(roleCode -> {
            operational.addAll(this.selectByRoleCode(roleCode));
        });
        return operational;
    }

    @Transactional
    @Override
    public void savePrivilegeOperationalRelation(Integer privilegeId, List<Integer> operationalIds) {
        // 1、找到这个操作相关联的角色编码，清除对应缓存
        privilegeService.clearOperationCacheByPrivilegeId(privilegeId);
        // 2、数据库insert关联关系
        operationalMapper.deleteRelationByPrivilegeId(privilegeId);
        operationalMapper.savePrivilegeOperationalRelation(privilegeId, operationalIds);

    }

    private List<String> selectByRoleCode(String roleCode){
        String key = CacheKeyConstants.OPERATIONAL_ROLE_CODE_PREFIX+roleCode;
        List<String> operationals = redisService.get(key, List.class);
        if (operationals != null) {
            return operationals;
        }
        if ("SUPER".equals(roleCode)) { // 如果是超管，则返回所有操作权限
            operationals = operationalMapper.selectList(Wrappers.<Operational>lambdaQuery()
                .isNotNull(Operational::getParentId)
                .isNotNull(Operational::getOperCode))
                .stream()
                .map(Operational::getOperCode)
                .collect(Collectors.toList());

        } else {
            operationals = operationalMapper.selectByRoleCode(roleCode);
        }
        redisService.set(key, operationals, 24, TimeUnit.HOURS);
        return operationals;
    }


}
