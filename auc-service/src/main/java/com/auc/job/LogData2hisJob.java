package com.auc.job;

import com.auc.common.constants.AucConst;
import com.auc.common.enums.VariableTypeEnum;
import com.auc.common.utils.Java8DateUtil;
import com.auc.service.ILogService;
import com.auc.service.IVariableService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志文件到历史表
 * @author zhangqi
 */
@Component
@Slf4j
public class LogData2hisJob {

  @Value("${sys.log2his.day}")
  private Integer beforeDayNum;
  @Autowired
  public ILogService logService;
  @Autowired
  public com.auc.service.IVariableService IVariableService;

  /**
   * lockAtLeastForString: 锁持有的最小时间（ms）。
   * 主要目的是在任务很短且节点之间的时钟差的情况下，防止从多个节点执行
   *
   * lockAtMostForString: 锁持有的最大时间（ms）。
   * 如果任务花费的时间超过 lockAtMostFor了所导致的行为，则可能无法预测（更多的进程将有效地持有该锁）
   */
  @Transactional
  @Scheduled(cron = "0 45 00 * * ?")
  @SchedulerLock(name = "LogData2hisJob", lockAtMostFor = 40*1000L,lockAtLeastFor = 3*1000L)
  public void toHis() {
    log.info("LogData transfer is start");
    logService.toHis(beforeDayNum);
    IVariableService.setVariable(VariableTypeEnum.CLEAR, AucConst.RU_VARIABLE_CLEAR_LOG,Java8DateUtil
        .formatter(new Date(),Java8DateUtil.DATE_FORMAT),null);
    log.info("LogData transfer is end");
  }
}