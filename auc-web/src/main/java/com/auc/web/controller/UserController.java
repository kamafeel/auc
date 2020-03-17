package com.auc.web.controller;

import com.auc.common.AppException;
import com.auc.common.constants.AucConst;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.common.utils.RegularExpression;
import com.auc.dao.*;
import com.auc.domain.dto.requestdto.LoginBaseRequestDTO;
import com.auc.domain.dto.requestdto.UserQueryRequestDTO;
import com.auc.domain.dto.responsedto.UpdatePasswordResponseDTO;
import com.auc.service.*;
import com.auc.domain.vo.UserRoleRelationVO;
import com.auc.web.help.ContextUtil;
import com.auc.web.help.Result;
import com.auc.web.security.JWTAuthenticationToken;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author zhangqi
 *
 */
@RestController
@Api(value = "用户模块", tags = "用户模块")
@RequestMapping("api/user")
public class UserController {

  @Resource
  private LoginService loginService;
  @Resource
  private IUserService userService;
  @Autowired
  private IMenuInfoService menuInfoService;
  @Autowired
  private IRoleService roleService;
  @Resource
  private IClientService iClientService;
  @Autowired
  private IRoleCacheService roleCacheService;



  /**
   * 获取用户信息
   */
  @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息")
  @RequestMapping(value = "baseUserInfo", method = RequestMethod.POST)
  @ResponseBody
  public Result<JWTAuthenticationToken> getBaseUserInfo(HttpServletRequest request) {
    return Result.success(ContextUtil.getJWTAuthentication());
  }

  /**
   * 第三方后端调用失效token(非系统登出)
   */
  @ApiOperation(value = "手动失效token", notes = "手动失效token")
  @RequestMapping(value = "inValid", method = RequestMethod.POST)
  @ResponseBody
  public Result<Boolean> inValid(@RequestBody @Valid LoginBaseRequestDTO dto,HttpServletRequest request) {
    Client client = iClientService.getByClientId(dto.getClient_id());
    if(client == null){
      return Result.failed(ErrorCodeEnum.UNEXCEPTED,"第三方信息错误");
    }
    return Result.success(loginService.inValid(ContextUtil.getCurrentUserId()));
  }

  //获取对象
  @ApiOperation(value = "获取指定用户信息", notes = "获取指定用户信息")
  @RequestMapping(value = "/{id}",method = RequestMethod.GET)
  public Result<User> get(@PathVariable("id")Long id) {
    User user = userService.getOne(Wrappers.<User>lambdaQuery()
        .select(User.class,u->!u.getColumn().equals("password") && !u.getColumn().equals("salt")).eq(User::getId,id));
    if(user !=null){
      user.setRoles(roleService.getRoleInfoByUserId(user.getId()));
    }
    return Result.success(user);
  }

  //禁用
  @PreAuthorize("hasAnyRole('ADMIN','SUPER') || hasAuthority('system:user:disable')")
  @ApiOperation(value = "禁用用户", notes = "禁用用户")
  @RequestMapping(value = "/disable/{id}",method = RequestMethod.PUT)
  public Result<Boolean> disable(@PathVariable("id") Long id) {
    loginService.inValid(String.valueOf(id));
    userService.update(Wrappers.<User>lambdaUpdate().set(User::getStatus, 0).eq(User::getId, id));
    return Result.success(true);
  }

  //启用
  @PreAuthorize("hasAnyRole('ADMIN','SUPER') || hasAuthority('system:user:enable')")
  @ApiOperation(value = "启用用户", notes = "启用用户")
  @RequestMapping(value = "enable/{id}",method = RequestMethod.PUT)
  public Result<Boolean> enable(@PathVariable("id") Long id) {
    userService.update(Wrappers.<User>lambdaUpdate().set(User::getStatus, 1).eq(User::getId, id));
    return Result.success(true);
  }

  /**
   * 分页查询数据：
   */
  @RequestMapping(value = "/page",method = RequestMethod.POST)
  public Result<IPage<User>> page(@RequestBody UserQueryRequestDTO dto) {
    List<String> roles =  ContextUtil.getRoles();
    //如果是一般用户只能查询自己的记录(包括其合并用户)
    if(!roles.isEmpty() && roles.size()==1 && roles.stream().anyMatch(g->g.equals("USER"))){
      dto.setUserId(userService.getMergeUserIdsWithYourself(Integer.valueOf(ContextUtil.getCurrentUserId())));
    }
    return Result.success(userService.selectPageVo(new Page<User>(dto.getPageNo(),dto.getPageSize()),
        Wrappers.<User>lambdaQuery()
            .in(dto.getUserId()!=null && !dto.getUserId().isEmpty(),User::getId,dto.getUserId())
            .eq(dto.getSourceId()!=null && dto.getSourceId()!=0,User::getSourceId,dto.getSourceId())
            .likeRight(StringUtils.isNotEmpty(dto.getUserName()), User::getUserName,dto.getUserName())
            .eq(StringUtils.isNotEmpty(dto.getPersonnelCode()), User::getPersonnelCode,dto.getPersonnelCode())
            .likeRight(StringUtils.isNotEmpty(dto.getRealName()), User::getRealName, dto.getRealName())));
  }

  /**
   * 修改用户域密码
   */
  @ApiOperation(value = "修改用户域密码", notes = "修改用户域密码")
  @RequestMapping(value = "updateOperationPassword", method = RequestMethod.POST)
  @ResponseBody
  public Result updateOperationPassword(Integer userId,String nowPassword) {
    boolean isMatch = RegularExpression.isContainsSpecialCharacterAndNumberAndCharacter(nowPassword);
    if (isMatch) {//密码规则
      try {
//        //根据用户id查出用户对象
//        User user =  userService.getById(userId);
        UpdatePasswordResponseDTO dto =  userService.updateAllPassword(userId,nowPassword);
        return Result.success(dto);
      }catch (AppException a){
        return Result.failed(a.getCode(),a.getMessage());
      }
    } else {
      return Result.failed(ErrorCodeEnum.PASSWORD_NOT_DISQUALIFIED,"密码太过简单,必须包含特殊字符、数字、字母的组合");
    }
  }

  @PreAuthorize("hasAuthority('system:user:edit')")
  @ApiOperation(value = "保存用户和角色的关联关系", notes = "userId（用户ID），" +
      "roleCode（角色ID-当前分级管理员默认为ADMIN），" +
      "action（操作码-0：删除，1：保存）")
  @RequestMapping(value = "/roleRelation",method = RequestMethod.POST)
  public Result<Boolean> saveRoleRelation(@RequestBody List<UserRoleRelationVO> userRoleRelations) {
    boolean rb = userService.saveRoleRelation(userRoleRelations);
    if(rb) {
      roleCacheService.clearCache();
    }
    return Result.success(true);
  }

  @ApiOperation(value = "获取当前登录用户的用户信息", notes = "返回结果包含菜单、角色、权限")
  @RequestMapping(value = "/info",method = RequestMethod.GET)
  public Result info() {
    JWTAuthenticationToken token = ContextUtil.getJWTAuthentication();
    if (token == null) {
      return Result.failed(ErrorCodeEnum.USER_NOT_LOGIN);
    }
    List<String> roles =  ContextUtil.getRoles();
    // 用登录人所拥有的角色查询对应角色拥有哪些菜单权限
    List<Integer> menuIds = menuInfoService.getMenuIdsByRoles(roles);
    if (menuIds.isEmpty()) {
      HashMap<String,Object> result = new HashMap<String,Object>(2);
      result.put("menus", Lists.newArrayList());
      result.put("urls", Lists.newArrayList());
      return Result.success(result);
    }
    QueryWrapper<MenuInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
        .eq(MenuInfo::getStatus, "0") // 0为正常，1为删除，不查询已删除的菜单
        .eq(MenuInfo::getFlag, "1") // 是否可以，只查可用的菜单
        .in(MenuInfo::getId, menuIds);
    List<MenuInfo> topMenus = toTree(menuInfoService.list(queryWrapper));// 第一次只查顶级菜单
    List<String> urls = topMenus.stream().map(MenuInfo::getUrl).collect(Collectors.toList());
    HashMap<String,Object> result = new HashMap<String,Object>(4);
    result.put("menus", topMenus);
    result.put("urls", urls);
    result.put("roles", roles);
    result.put("authorities",token.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(a -> !a.contains("ROLE_")).collect(Collectors.toList()));
    return Result.success(result);
  }

  private List<MenuInfo> toTree(List<MenuInfo> menus) {
    // 过滤出顶级节点
    List<MenuInfo> tops = menus.stream().filter(menuInfo -> menuInfo.getParentId() == 0).collect(Collectors.toList());
    getChildren(tops, menus);
    return tops;
  }

  private void getChildren(List<MenuInfo> parents, List<MenuInfo> all) {
    if (parents == null || parents.isEmpty()) {
      return;
    }
    parents.stream().forEach(parent -> {
      List<MenuInfo> children = all.stream()
          .filter(menuInfo -> menuInfo.getParentId().equals(parent.getId()))
          .collect(Collectors.toList());
      if (!children.isEmpty()) {
        getChildren(children, all);
      }
      parent.setChildren(children);
    });
  }

}
