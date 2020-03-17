package com.auc.dubbo.user.mapper;

import com.auc.dubbo.user.dao.BaseUser;
import com.auc.dubbo.user.dao.DataSource;
import com.auc.dubbo.user.dao.MergeUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基础信息 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-14
 */
public interface BaseUserMapper extends BaseMapper<BaseUser> {

  BaseUser getByUserNameAndDomainCode(@Param("userName") String userName, @Param("domainCode") String domainCode);

  @Select("SELECT mu.mergeId,mu.userId, mu.userName,ds.sourceCode FROM t_s_merge_user_mapping  mu " +
      " LEFT JOIN t_datasource ds ON ds.sourceId = mu.sourceId")
  List<Map<String,Object>> getAllMergeAccounts();

  @Select("SELECT sourceId FROM t_datasource WHERE sourceCode = #{sourceCode} LIMIT 1")
  Integer getDataSourceId(@Param("sourceCode") String sourceCode);

  List<Map<String, Object>> syncBaseUser(@Param("sucSyncDateTime") String sucSyncDateTime, @Param("startId")Integer startId);

  List<Map<String, Object>> syncDataSource();
}
