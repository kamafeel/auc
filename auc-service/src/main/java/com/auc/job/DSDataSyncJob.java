package com.auc.job;

import com.auc.common.constants.AucConst;
import com.auc.common.enums.VariableTypeEnum;
import com.auc.common.utils.Java8DateUtil;
import com.auc.dao.Variable;
import com.auc.dubbo.user.service.IBaseUserService;
import com.auc.service.IDatasourceService;
import com.auc.service.IVariableService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 从智慧办公同步数据源数据到AUC
 * @author zhangqi
 */
@Component
@Slf4j
public class DSDataSyncJob {

  @Autowired
  public IVariableService IVariableService;

  @Autowired
  public IDatasourceService datasourceService;

  @Reference(version = "${service.version.user}", timeout = 40*1000, check = false)
  private IBaseUserService baseUserService;

  @Transactional
  @Scheduled(cron = "0 05 07 * * ?")
  @SchedulerLock(name = "DSDataSyncJob", lockAtMostFor = 40*1000L,lockAtLeastFor = 3*1000L)
  public void syncDSData(){
    log.info("DSDataSyncJob is start");
    String sucSyncDateTime = null;
    Variable var = IVariableService.getVariable(VariableTypeEnum.SYNC, AucConst.RU_VARIABLE_DS_SYNC);
    if(var == null || StringUtils.isEmpty(var.getVValue())){
      sucSyncDateTime = Java8DateUtil.formatter(new Date(),Java8DateUtil.DATE_FORMAT);
    }else{
      sucSyncDateTime = var.getVValue();
    }
    List<Map<String, Object>> syncData = baseUserService.syncDataSource();

    String finalSucSyncDateTime = sucSyncDateTime;
    boolean isSync = syncData.stream().filter(m->(m.get("Createtime")!=null &&
        Java8DateUtil.compareWith(Java8DateUtil.getDate(
        finalSucSyncDateTime,Java8DateUtil.DATE_FORMAT),
            (Timestamp)m.get("Createtime")))
        ||
        (m.get("Updatetime") !=null &&
            Java8DateUtil.compareWith(Java8DateUtil.getDate(
        finalSucSyncDateTime,Java8DateUtil.DATE_FORMAT),
                (Timestamp)m.get("Updatetime")))).findAny().orElse(null) != null;
    //需要同步,全量同步
    if(isSync){
      datasourceService.clear();
      datasourceService.batchInsert(syncData);
    }
    IVariableService.setVariable(VariableTypeEnum.SYNC, AucConst.RU_VARIABLE_DS_SYNC,
        Java8DateUtil.formatter(new Date(),Java8DateUtil.DATE_FORMAT),null);
    log.info("DSDataSyncJob is end");
  }
}
