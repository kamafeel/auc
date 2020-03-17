package com.auc.dubbo.user.service;


import com.auc.dubbo.user.dao.BaseUser;
import com.auc.dubbo.user.dao.DataSource;
import com.auc.dubbo.user.dao.MergeUser;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基础信息 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-14
 */
public interface IBaseUserService extends IService<BaseUser> {

  List<Map<String,Object>> getAllMergeAccounts();

  BaseUser getByUsernameAndSourceCode(String username, String sourceCode);

  List<Map<String, Object>> syncBaseUser(String sucSyncDateTime, Integer startId);

  List<Map<String, Object>> syncDataSource();

}
