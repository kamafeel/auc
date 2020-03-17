package com.auc.mapper;

import com.auc.dao.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-06
 */
public interface UserMapper extends BaseMapper<User> {

  void batchInsertUserSync(@Param("list") List<Map<String, Object>> list);

  void deleteUserBySync();
  //备份AUC的密码和盐值表
  void backupAUCUser();
  void deleteAUCUserBackup();

  void syncUser();

  void deleteUserSync();

  IPage<User> selectPageVo(Page page, @Param("ew")Wrapper<User> queryWrapper);


  void deleteRoleRelation(@Param("userId")Integer userId, @Param("roleId")Integer roleId);

  void saveRoleRelation(@Param("userId")Integer userId, @Param("userName")String userName, @Param("roleId")Integer roleId);

  User getByUserNameAndDomainCode(@Param("userName")String userName, @Param("domainCode")String domainCode);

  User getByUsernameAndSourceCode(@Param("userName")String userName, @Param("sourceCode")String sourceCode);

  User getByMobilePhoneAndSourceCode(@Param("mobilePhone") String mobilePhone, @Param("sourceCode") String sourceCode);

  List<Map<String, String>> getDataSourceByMobilePhone(@Param("mobilePhone")String mobilePhone);

  List<Map<String, String>> getDataSourceByAppAccount(@Param("appAccount")String appAccount);

 List<User> getUsersByUids(@Param("userIds") Collection<Integer> userIds);

}
