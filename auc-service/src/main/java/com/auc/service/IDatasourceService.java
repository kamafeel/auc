package com.auc.service;

import com.auc.dao.Datasource;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 记录数据来源或国美各板块信息 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-11
 */
public interface IDatasourceService extends IService<Datasource> {
  void clear();
  void clearCache();
  void batchInsert(List<Map<String, Object>> list);
  Integer getIdBySourceCode(String sourceCode);
  String getCodeBySourceId(Integer sourceId);
}
