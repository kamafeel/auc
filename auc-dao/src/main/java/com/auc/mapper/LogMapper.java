package com.auc.mapper;

import com.auc.dao.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-23
 */
public interface LogMapper extends BaseMapper<Log> {
  void toHis(Integer beforeDayNum);
  void del2HisData(Integer beforeDayNum);
}
