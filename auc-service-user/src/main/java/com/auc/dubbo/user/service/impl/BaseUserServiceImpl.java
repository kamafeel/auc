package com.auc.dubbo.user.service.impl;


import com.auc.dubbo.user.service.IBaseUserService;
import com.auc.dubbo.user.dao.BaseUser;
import com.auc.dubbo.user.dao.MergeUser;
import com.auc.dubbo.user.mapper.BaseUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;


/**
 * <p>
 * 用户基础信息 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-14
 */

@Service(version = "1.0.0")
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements
    IBaseUserService {

  @Autowired
  private BaseUserMapper baseUserMapper;

  @Override
  public List<Map<String,Object>> getAllMergeAccounts() {
    return baseUserMapper.getAllMergeAccounts();
  }

  @Cacheable("us-dataSourceCache")
  public Integer getDataSourceId(String sourceCode) {
    return baseUserMapper.getDataSourceId(sourceCode);
  }

  @Override
  public BaseUser getByUsernameAndSourceCode(String username, String sourceCode){
    QueryWrapper<BaseUser> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
        .eq(BaseUser::getUserName, username)
        .eq(BaseUser::getSourceId, this.getDataSourceId(sourceCode));
    BaseUser baseUser = super.getOne(queryWrapper);
    return baseUser;
  }

  @Override
  public List<Map<String, Object>> syncBaseUser(String sucSyncDateTime, Integer startId) {
    return baseUserMapper.syncBaseUser(sucSyncDateTime,startId);
  }

  @Override
  public List<Map<String, Object>> syncDataSource() {
    return baseUserMapper.syncDataSource();
  }

}
