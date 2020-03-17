package com.auc.service.impl;

import com.auc.common.AppException;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.ExcelOperate;
import com.auc.dao.AccountConvert;
import com.auc.dubbo.user.dao.MergeUser;
import com.auc.mapper.AccountConvertMapper;
import com.auc.service.IAccountConvertService;
import com.auc.service.ICacheService;
import com.auc.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 登录名转换配置表 服务实现类
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-21
 */
@Service
@Slf4j
public class AccountConvertServiceImpl extends ServiceImpl<AccountConvertMapper, AccountConvert> implements
    IAccountConvertService {
  @Autowired
  private ICacheService iCacheService;

  @Override
  public List<String> importExcel(MultipartFile file, String userId){
    List<String> resultList = Lists.newArrayList();
    // 获取excel中的数据
    try {
      List<String[]>  result = ExcelOperate.getData(file, 1);
      List<AccountConvert> acList = new ArrayList<>();
      result.forEach(sa->{
        AccountConvert ac = new AccountConvert();
        ac.setSourceCode(sa[0]);
        ac.setConvertLoginName(sa[1]);
        ac.setClientId(sa[2]);
        ac.setCreateUser(userId);
        acList.add(ac);
      });
      baseMapper.insertList(acList);
    } catch (IOException e) {
      log.error("importExcel is fail",e);
      throw new AppException(ErrorCodeEnum.FAIL_FILE,ExceptionUtils.getStackTrace(e));
    }
    return resultList;
  }

  /**
   * 按配置进行账号转换
   * @param original
   * @param userId
   * @param clientId
   * @return
   */
  @Override
  public MergeUser convertUserName(MergeUser original, String userId,String clientId) {
    List<MergeUser> mergeAccounts = Lists.newArrayList();
    // 先查用户合并信息，如果有合并账号信息，则用合并账号的username和sourceCode到账号转换表查转换信息
    List<Map<String,Object>> mas = iCacheService.getAllMergeAccounts();
    Map<String,Object> mergeMap = mas.stream().filter(m-> String.valueOf(m.get("userId")).equals(userId))
        .findAny().orElse(null);
    if(mergeMap == null){
      return original;
    }
    mas.stream()
        .filter(m-> String.valueOf(m.get("mergeId")).equals(String.valueOf(mergeMap.get("mergeId"))))
        .forEach(m->{
          MergeUser mu = new MergeUser();
          mu.setUserName(String.valueOf(m.get("userName")));
          mu.setSourceCode(String.valueOf(m.get("sourceCode")));
          mergeAccounts.add(mu);
        });
    if (!mergeAccounts.isEmpty()) {
      // 如果有账号转换信息则用配置的转换账户进行鉴权
      AccountConvert accountConvert = baseMapper.selectOne(new QueryWrapper<AccountConvert>().lambda()
          .eq(AccountConvert::getClientId, clientId)
          .in(AccountConvert::getSourceCode, mergeAccounts.stream().map(MergeUser::getSourceCode).distinct().collect(Collectors.toList()))
          .in(AccountConvert::getConvertLoginName, mergeAccounts.stream().map(MergeUser::getUserName).distinct().collect(Collectors.toList()))
          .last("limit 1"));
      if (accountConvert != null) {
        original.setSourceCode(accountConvert.getSourceCode());
        original.setUserName(accountConvert.getConvertLoginName());
        return original;
      }

      // 根据clientId查询转换表是否有通配符配置
      accountConvert = baseMapper.selectOne(new QueryWrapper<AccountConvert>().lambda()
          .eq(AccountConvert::getConvertLoginName, "*")
          .eq(AccountConvert::getClientId, clientId)
          .last("limit 1"));
      if (accountConvert != null) { // 如果有通配符配置则查询账号转换配置信息
        String convertSourceCode = accountConvert.getSourceCode(); // 通配符配置的数据源编码
        MergeUser mergeUser = mergeAccounts.stream()
            .filter(mergeAccount -> convertSourceCode.equals(mergeAccount.getSourceCode()))
            .findFirst()
            .orElse(null);
        if (mergeUser != null) {
          original.setSourceCode(mergeUser.getSourceCode());
          original.setUserName(mergeUser.getUserName());
          return original;
        }
      }
    }
    return original;
  }
}
