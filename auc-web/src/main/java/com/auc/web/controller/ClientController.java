package com.auc.web.controller;

import com.auc.common.AppException;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.dao.AccountConvert;
import com.auc.domain.dto.requestdto.ClientQueryRequestDTO;
import com.auc.domain.dto.requestdto.QueryPageParam;
import com.auc.dubbo.user.dao.MergeUser;
import com.auc.service.IAccountConvertService;
import com.auc.service.IClientService;
import com.auc.dao.Client;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Api(value = "第三方系统模块", tags = "第三方系统模块")
@RequestMapping("api/client")
public class ClientController {

  @Autowired
  public IClientService clientService;
  @Autowired
  public IAccountConvertService accountConvertService;

  /**
   * 保存、修改 【区分id即可】
   *
   * @param client 传递的实体
   */
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:client:save')")
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public Result save(@RequestBody @Validated Client client) {
    int count = clientService.count(Wrappers.<Client>lambdaQuery()
        .eq(Client::getClientId, client.getClientId())
        .eq(Client::getDelFlag, 0)
        .ne(client.getId() != null, Client::getId, client.getId()));
    if (count > 0) {
      return Result.failed(ErrorCodeEnum.FAIL_DATABASE, "clientId已存在");
    }
    if (client.getId() != null) {
      clientService.updateById(client);
    } else {
      clientService.save(client);
    }
    clientService.clearCache();
    return Result.success("保存成功");
  }

  //删除对象信息
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:client:delete')")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public Result<Boolean> delete(@PathVariable("id") Integer id) {
    if (id == null) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED, "ID不能为空", false);
    }
    Client c = clientService.getById(id);
    if (c == null) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED, "传入ID有误", false);
    }
    // 先验证账号转换表是否有该client的配置信息，如果有则不删除client
    int accountNum = accountConvertService.count(Wrappers.<AccountConvert>lambdaQuery().eq(AccountConvert::getClientId, c.getClientId()));
    if (accountNum > 0) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED, "请先删除相关的账号转换信息", false);
    }
    c.setId(id);
    c.setDelFlag(1);
    clientService.updateById(c);
    clientService.clearCache();
    return Result.success(true);
  }

  //获取对象
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:client:query')")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Result<Client> get(@PathVariable("id") Long id) {
    return Result.success(clientService.getById(id));
  }


  //获取所有对象
  @RequestMapping(value = {"/list", "/list/{keyword}"}, method = RequestMethod.GET)
  @ResponseBody
  public Result<List<Client>> list(
      @ApiParam(value = "关键字", required = false) @PathVariable(required = false) String keyword) {
    return Result.success(clientService.getClientsByName(keyword));
  }

  /**
   * 使用域登录的client列表
   * @return
   */
  @RequestMapping(value = {"/domainLoginList"}, method = RequestMethod.GET)
  @ResponseBody
  public Result<List<Client>> domainLoginList() {
    return Result.success(clientService.list(Wrappers.<Client>lambdaQuery()
        //.eq(Client::getStatus, 0)  // 状态为显示
        .eq(Client::getDelFlag, 0) // 没有被删除
        .eq(Client::getLoginType,1) // 采用域登录
        .last(" ORDER BY sort ") // 使用用配置的顺序
    ));
  }

  /**
   * 获取三方系统跳转链接
   * 1、到合并用户表里查是否是合并用户，如果不是合并用户则直接用该登录账号进行鉴权
   * 2、如果是合并用户则查询出所有合并的账号，到账号转换表里查账户转换配置，如果有账号转换配置则用配置信息，则用配置的账户
   * 3、如果没有账号转换配置则用clientId查询用户转换表是否有通配符配置，如果没有则直接使用当前登录人账号
   * 4、如果有通配符配置，则过滤合并用户信息，找对应数据源的账户，如果没有找到，还是使用当前登录人账号,如果有匹配到对应合并用户，则使用合并用户的账户
   * @param clientId
   * @return
   */
  @ApiOperation(value = "获取三方系统跳转链接", notes = "获取三方系统跳转链接")
  @RequestMapping(value = "/openClient/{clientId}", method = RequestMethod.GET)
  @ResponseBody
  public Result<String> openClient(
      @ApiParam(value = "第三方系统ID", required = true) @PathVariable("clientId") String clientId) {
    // 根据clientId查询对应三方系统的配置方案，生成带鉴权信息的链接
    Client client = clientService.getByClientId(clientId);
    if (client == null) {
      return Result.failed(ErrorCodeEnum.UNEXCEPTED, "第三方信息错误");
    }
    // 用于鉴权的用户名
    MergeUser mu = new MergeUser();
    mu.setUserName(ContextUtil.getCurrentUserName());
    mu.setSourceCode(ContextUtil.getJWTAuthentication().getSourceCode());
    mu = accountConvertService.convertUserName(mu, ContextUtil.getCurrentUserId(), clientId);

    TokenHandler tokenHandler = TokenHandlerContext.getTokenHandler(clientId);
    if (tokenHandler == null) {
      throw new AppException(ErrorCodeEnum.UNEXCEPTED, "还不能进行跳转");
    }

    String token = tokenHandler.handle(client, mu.getUserName(), mu.getSourceCode());
    return Result.success(client.getClientLoginUrl() + token);
  }

  /**
   * 分页查询数据：
   */
  @PreAuthorize("hasRole('SUPER') || hasAuthority('auc:client:query')")
  @RequestMapping(value = "/page", method = RequestMethod.POST)
  public Result<IPage<Client>> page(@RequestBody ClientQueryRequestDTO dto) {

    return Result.success(clientService.page(new Page<Client>(dto.getPageNo(), dto.getPageSize()),
        Wrappers.<Client>lambdaQuery()
        .like(!StringUtils.isEmpty(dto.getClientId()), Client::getClientId, dto.getClientId())
        .like(!StringUtils.isEmpty(dto.getClientName()), Client::getClientName, dto.getClientName())
        .eq(dto.getStatus()!=null, Client::getStatus, dto.getStatus())
        .eq(dto.getLoginType()!=null, Client::getLoginType, dto.getLoginType())
        .eq(Client::getDelFlag, 0)
    ));
  }
}
