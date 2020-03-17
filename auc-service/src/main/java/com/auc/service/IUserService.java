package com.auc.service;

import com.auc.common.AppException;
import com.auc.dao.Datasource;
import com.auc.dao.User;
import com.auc.domain.dto.responsedto.UpdatePasswordResponseDTO;
import com.auc.domain.vo.UserRoleRelationVO;
import com.auc.dubbo.user.dao.MergeUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户相关服务接口
 */
public interface IUserService extends IService<User> {

  void clearCache();

  void syncUserData(List<Map<String, Object>> syncData);

  Collection<Integer> getMergeUserIdsWithYourself(Integer userId);

  List<MergeUser> getMergeUsers(String userId);

  IPage<User> selectPageVo(Page page, Wrapper<User> queryWrapper);

  boolean saveRoleRelation(List<UserRoleRelationVO> userRoleRelations);

  List<User> checkAppAccount(String appAccount,String password,String sourceCode);

  UpdatePasswordResponseDTO updateAllPassword(Integer userId, String newPassword);

  List<User> selectUsersByUsername(String username);

  User getByUserNameAndDomainCode(String userName, String domainCode);

  User getByUsernameAndSourceCode(String userName, String sourceCode);

  User getByMobilePhoneAndSourceCode(String mobilePhone, String sourceCode);

  boolean isMobileExist(String mobilePhone);

  boolean isExpiredOfPassword(String adAccount, String adSource);

  List<Datasource> getDatasourceByUsernameAndType(String username, String type) throws AppException;

  User getMobilePhoneByUsernameAndScode(String username,String sourceCode,String type) throws AppException;

  List<Map<String, String>> getDataSourceByAppAccount(String appAccount);

  List<Map<String, String>> getDataSourceByMobilePhone(String mobilePhone);

  UpdatePasswordResponseDTO checkCodeAdUpdatePassword(String code, Integer userId,String newpassword);
}
