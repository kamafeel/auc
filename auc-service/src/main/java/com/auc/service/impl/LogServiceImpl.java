package com.auc.service.impl;

import com.auc.dao.Log;
import com.auc.mapper.LogMapper;
import com.auc.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-23
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

  @Override
  public void asyncInLog(Log log) {
    baseMapper.insert(log);
  }

  @Override
  @Transactional
  public void toHis(Integer beforeDayNum) {
    baseMapper.toHis(beforeDayNum);
    baseMapper.del2HisData(beforeDayNum);
  }

}
