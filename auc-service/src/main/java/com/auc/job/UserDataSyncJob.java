package com.auc.job;

import com.auc.common.constants.AucConst;
import com.auc.common.enums.VariableTypeEnum;
import com.auc.common.utils.Java8DateUtil;
import com.auc.dao.Variable;
import com.auc.dubbo.user.service.IBaseUserService;
import com.auc.service.IUserService;
import com.auc.service.IVariableService;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 从智慧办公同步人员数据到AUC
 * @author zhangqi
 */
@Component
@Slf4j
public class UserDataSyncJob {

  @Autowired
  public IVariableService IVariableService;

  @Autowired
  public IUserService userService;

  @Reference(version = "${service.version.user}", timeout = 40*1000, check = false)
  private IBaseUserService baseUserService;

  @Transactional
  @Scheduled(cron = "0 10 07 * * ?")
  @SchedulerLock(name = "UserDataSyncJob", lockAtMostFor = 40*1000L,lockAtLeastFor = 3*1000L)
  public void syncUserData(){
    log.info("syncUserData is start");
    Integer startId = 0;
    String sucSyncDateTime = null;
    Variable var = IVariableService.getVariable(VariableTypeEnum.SYNC, AucConst.RU_VARIABLE_USER_SYNC);
    //如果存在日期,才使用日期字段进行增量同步,否正使用id进行增量同步
    if(var != null){
      if(StringUtils.isNotEmpty(var.getVValue())){
        sucSyncDateTime = var.getVValue();
        startId = null;
      }else if(StringUtils.isNotEmpty(var.getExt())){
        startId = Integer.valueOf(var.getExt());
      }
    }
    //密码和盐值,只在初次时同步,后续不再需要智慧办公的相关数据
    List<Map<String, Object>> syncData = baseUserService.syncBaseUser(sucSyncDateTime,startId);
    if(syncData==null || syncData.isEmpty()){
      //无数据，默认采用日期更新
      IVariableService.setVariable(VariableTypeEnum.SYNC, AucConst.RU_VARIABLE_USER_SYNC,
          Java8DateUtil.formatter(new Date(),Java8DateUtil.DATE_FORMAT),null);
      log.info("no data need to sync");
      return;
    }
    //合并
    userService.syncUserData(syncData);
    //如果是时间模式进行增量更新,不做ID处理
    if(startId != null){
      //获取本次的最大ID
      startId = syncData.stream().map(m ->Integer.valueOf(m.get("id").toString())).max(Comparator.comparing(Integer::valueOf)).get();
      //只更新最大id,不更新日期
      IVariableService.setVariable(VariableTypeEnum.SYNC, AucConst.RU_VARIABLE_USER_SYNC,
          null,String.valueOf(startId));
    }else{
      //时间模式更新
      IVariableService.setVariable(VariableTypeEnum.SYNC, AucConst.RU_VARIABLE_USER_SYNC,
          Java8DateUtil.formatter(new Date(),Java8DateUtil.DATE_FORMAT),null);
    }

    log.info("syncUserData is end");
  }
}
