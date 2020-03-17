package com.auc.web.controller;

import com.auc.common.enums.LogTypeEnum;
import com.auc.common.utils.JsonUtil;
import com.auc.dao.User;
import com.auc.domain.dto.requestdto.LogQueryRequestDTO;
import com.auc.domain.dto.requestdto.QueryPageParam;
import com.auc.service.ILogService;
import com.auc.dao.Log;
import com.auc.service.IUserService;
import com.auc.web.help.ContextUtil;
import com.auc.web.help.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "日志查询模块", tags = "日志查询模块")
@RestController
@RequestMapping("api/log")
public class LogController {

  @Autowired
  public ILogService logService;
  @Resource
  private IUserService userService;

  @ApiOperation(value = "日志类型列表", notes = "日志类型列表")
  @RequestMapping(value = "getLogType", method = RequestMethod.GET)
  @ResponseBody
  public Result getLogType() {
    ArrayNode an = JsonUtil.createArrayNode();
    Arrays.stream(LogTypeEnum.values()).forEach(l->{
      ObjectNode on = JsonUtil.createObjectNode();
      on.put("code",l.getCode());
      on.put("name",l.getName());
      on.put("para",l.name());
      an.add(on);
    });
    return Result.success(an);
  }

  /**
   * 分页查询数据：
   */
  @PreAuthorize("hasRole('SUPER') || hasAuthority('system:log:query')")
  @ApiOperation(value = "获取分页日志信息", notes = "获取分页日志信息")
  @RequestMapping(value = "/page", method = RequestMethod.POST)
  public Result page(@RequestBody LogQueryRequestDTO dto) {
    List<String> roles =  ContextUtil.getRoles();
    //如果是一般用户只能查询自己的记录(包括其合并用户)
    if(!roles.isEmpty() && roles.size()==1 && roles.stream().anyMatch(g->g.equals("USER"))){
      dto.setUserId(userService.getMergeUserIdsWithYourself(Integer.valueOf(ContextUtil.getCurrentUserId())));
    }

    List<Log> logs = logService.list(Wrappers.<Log>lambdaQuery()
        .lt(dto.getId()!=null,  Log::getId, dto.getId())
        .in(dto.getUserId()!=null && !dto.getUserId().isEmpty(),Log::getUserId,dto.getUserId())
        .between(dto.getStartTime() != null, Log::getCreateTime, dto.getStartTime(),
            dto.getEndTime())
        .eq(StringUtils.isNotEmpty(dto.getUserName()), Log::getUserName,dto.getUserName())
        .eq(dto.getLogTypeEnum() != null, Log::getLogType,
            dto.getLogTypeEnum() != null ? dto.getLogTypeEnum().getCode() : 0)
        .like(StringUtils.isNotEmpty(dto.getLogContent()), Log::getContent, dto.getLogContent())
        .orderByDesc(Log::getId)
        .last(" LIMIT " + dto.getPageSize()));

    Map<String, Object> result = ImmutableMap.<String, Object> builder()
        .put("list", logs)
        .put("latestId", logs.isEmpty()?"":logs.get(logs.size()-1).getId().toString())
        .build();

    return Result.success(result);
//    return Result.success(logService.page(new Page<Log>(dto.getPageNo(), dto.getPageSize()),
//        Wrappers.<Log>lambdaQuery()
//            .between(dto.getStartTime() != null, Log::getCreateTime, dto.getStartTime(),
//                dto.getEndTime())
//            .eq(StringUtils.isNotEmpty(dto.getUserName()), Log::getUserName,dto.getUserName())
//            .eq(dto.getLogTypeEnum() != null, Log::getLogType,
//                dto.getLogTypeEnum() != null ? dto.getLogTypeEnum().getCode() : 0)
//            .like(StringUtils.isNotEmpty(dto.getLogContent()), Log::getContent, dto.getLogContent())
//            .orderByDesc(Log::getId)));
  }
}
