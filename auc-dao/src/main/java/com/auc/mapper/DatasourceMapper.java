package com.auc.mapper;

import com.auc.dao.Datasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 记录数据来源或国美各板块信息 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-11
 */
public interface DatasourceMapper extends BaseMapper<Datasource> {
  void clear();
  void batchInsert(@Param("list") List<Map<String, Object>> list);
}
