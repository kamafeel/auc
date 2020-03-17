package com.auc.web.controller;

import com.auc.common.utils.FileUploadUtils;
import com.auc.common.enums.ErrorCodeEnum;
import com.auc.job.DSDataSyncJob;
import com.auc.job.UserDataSyncJob;
import com.auc.service.IClientService;
import com.auc.service.IRoleCacheService;
import com.auc.service.IUserService;
import com.auc.web.help.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 系统管理接口
 * @author zhangqi
 */
@Api(value = "系统管理模块", tags = "系统管理模块")
@RestController
@RequestMapping("api/system")
@Slf4j
public class SystemController {

  @Autowired
  public DSDataSyncJob dsDataSyncjob;
  @Autowired
  public UserDataSyncJob userDataSyncJob;
  @Autowired
  public IUserService userService;
  @Autowired
  public IClientService clientService;
  @Autowired
  private IRoleCacheService roleCacheService;
  @Resource
  private RedisLockRegistry redisLockRegistry;

  @ApiOperation(value = "同步数据源数据", notes = "同步数据源数据")
  @RequestMapping(value = "syncDSData", method = RequestMethod.GET)
  @ResponseBody
  public Result syncDSData() {
    Lock lock = redisLockRegistry.obtain("syncDSData");
    try {
      lock.tryLock(3, TimeUnit.SECONDS);
      dsDataSyncjob.syncDSData();
    } catch (Exception e) {
      log.error("syncDSData",e);
      return Result.failed(ErrorCodeEnum.LOCK_FAIL);
    }finally {
      lock.unlock();
    }
    return Result.success();
  }

  @ApiOperation(value = "同步人员数据", notes = "同步人员数据")
  @RequestMapping(value = "syncUserData", method = RequestMethod.GET)
  @ResponseBody
  public Result syncUserData() {
    Lock lock = redisLockRegistry.obtain("syncDSData");
    try {
      lock.tryLock(3, TimeUnit.SECONDS);
      userDataSyncJob.syncUserData();
    } catch (Exception e) {
      log.error("syncUserData",e);
      return Result.failed(ErrorCodeEnum.LOCK_FAIL);
    }finally {
      lock.unlock();
    }
    return Result.success();
  }

  @ApiOperation(value = "清空缓存userCache,clientCache,roleCache", notes = "清空缓存userCache,clientCache,roleCache")
  @RequestMapping(value = "/clearCache/{key}", method = RequestMethod.GET)
  @ResponseBody
  public Result clearCache(@PathVariable("key") String key) {
    switch (key){
      case "userCache":
        userService.clearCache();
        break;
      case "clientCache":
        clientService.clearCache();
          break;
      case "roleCache":
        roleCacheService.clearCache();
        break;
    }
    return Result.success();
  }


  @ApiOperation(value = "系统通用上传接口", notes = "返回附件URL地址")
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public Result file(MultipartFile file) throws IOException {
    return Result.success(FileUploadUtils.upload(file));
  }

}
