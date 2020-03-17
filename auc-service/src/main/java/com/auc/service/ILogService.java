package com.auc.service;

import com.auc.dao.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-23
 */
public interface ILogService extends IService<Log> {

  void asyncInLog(Log log);

  void toHis(Integer beforeDayNum);
}
