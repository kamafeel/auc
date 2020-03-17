package com.auc.web.controller;

import com.auc.common.AppException;
import com.auc.common.enums.DataSourceEnum;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.dao.AccountConvert;
import com.auc.dao.Client;
import com.auc.domain.dto.requestdto.AccountConvertRequestDTO;
import com.auc.domain.dto.requestdto.QueryPageParam;
import com.auc.dubbo.user.dao.MergeUser;
import com.auc.service.IAccountConvertService;
import com.auc.service.IClientService;
import com.auc.web.handler.TokenHandler;
import com.auc.web.handler.TokenHandlerContext;
import com.auc.web.help.ContextUtil;
import com.auc.web.help.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController

@Api(value = "账号转换模块", tags = "账号转换模块")
@RequestMapping("api/accountconvert")
public class AccountConvertController {
  @Autowired
  public IAccountConvertService accountConvertService;
  @Autowired
  public IClientService clientService;

  /**
   * 保存、修改 【区分id即可】
   *
   * @param accountconvert 传递的实体
   * @return Ajaxresult转换结果
   */
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:accountconvert:save')")
  @ApiOperation(value = "保存账号转换信息", notes = "参数中有ID时为修改，无ID时为新增")
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public Result<Boolean> save(@RequestBody AccountConvert accountconvert) {
    Client client = clientService.getByClientId(accountconvert.getClientId());
    if (client == null) {
      return Result.failed(ErrorCodeEnum.ILLEGAL_PARAMETER, "clientId有误");
    }
    //登录类型  0：域账号登录，1：系统账号登录
    if (client.getLoginType() == 1) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"该系统使用系统账号登录，不能配置账号转换");
    }
    if (accountconvert.getId() != null) {
      accountconvert.setUpdateUser(ContextUtil.getCurrentUserId());
      accountconvert.setUpdateTime(LocalDateTime.now());
      accountConvertService.updateById(accountconvert);
    } else {
      accountconvert.setCreateUser(ContextUtil.getCurrentUserId());
      accountconvert.setCreateTime(LocalDateTime.now());
      accountconvert.setUpdateUser(accountconvert.getCreateUser());
      accountconvert.setUpdateTime(accountconvert.getCreateTime());
      accountConvertService.save(accountconvert);
    }
    return Result.success(true);
  }

  //删除对象信息
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:accountconvert:delete')")
  @ApiOperation(value = "删除账号转换信息", notes = "根据ID删除账号转换信息")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public Result<Boolean> delete(@PathVariable("id") Long id) {
    return Result.success(accountConvertService.removeById(id));
  }

  //获取用户
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:accountconvert:query')")
  @ApiOperation(value = "获取账号转换信息", notes = "根据ID获取账号转换信息")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Result<AccountConvert> get(@PathVariable("id") Long id) {
    return Result.success(accountConvertService.getById(id));
  }

  /**
   * 分页查询数据：
   */
  @PreAuthorize("hasAnyRole('ADMIN') || hasAuthority('auc:accountconvert:query')")
  @ApiOperation(value = "获取账号转换信息分页列表", notes = "有分页，默认pageNo=1 pageSize=10")
  @RequestMapping(value = "/page", method = RequestMethod.POST)
  public Result<IPage<AccountConvert>> page(@RequestBody AccountConvertRequestDTO dto) {
    IPage<AccountConvert> accountConvertIPage = accountConvertService
        .page(new Page<AccountConvert>(dto.getPageNo(), dto.getPageSize()),
        Wrappers.<AccountConvert>lambdaQuery()
                .eq(!StringUtils.isEmpty(dto.getClientId()), AccountConvert::getClientId, dto.getClientId())
                .eq(!StringUtils.isEmpty(dto.getSourceCode()),AccountConvert::getSourceCode, dto.getSourceCode())
                .likeRight(!StringUtils.isEmpty(dto.getConvertLoginName()),AccountConvert::getConvertLoginName, dto.getConvertLoginName())
                .orderByDesc(AccountConvert::getCreateTime,AccountConvert::getUpdateTime));
    accountConvertIPage.getRecords().stream().forEach(accountconver -> {
      accountconver.setClientName(clientService.getByClientId(accountconver.getClientId()).getClientName());//TODO 是否影响效率
      String sourceCode = accountconver.getSourceCode();
      accountconver.setSourceName(StringUtils.isEmpty(sourceCode) ? "" : DataSourceEnum.valueOf(sourceCode).getSourceName());
    });
    return Result.success(accountConvertIPage);
  }

  @PreAuthorize("hasAnyRole('ADMIN') || hasAuthority('auc:accountconvert:import')")
  @ApiOperation(value = "批量导入", notes = "批量导")
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  @ResponseBody
  public Result importExcel(@ApiParam(value = "文件") MultipartFile file) {
    List<String> result  = accountConvertService.importExcel(file,ContextUtil.getCurrentUserId());
    return result.isEmpty() ? Result.success() : Result.failed(ErrorCodeEnum.FAIL_FILE,"",result);
  }

}
