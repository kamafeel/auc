package com.auc.service.impl;

import com.auc.dao.Datasource;
import com.auc.mapper.DatasourceMapper;
import com.auc.service.IDatasourceService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录数据来源或国美各板块信息 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-11
 */
@Service
public class DatasourceServiceImpl extends ServiceImpl<DatasourceMapper, Datasource> implements IDatasourceService {

  @Override
  public void clear() {
    baseMapper.clear();
  }

  /**
   * 清空缓存
   */
  @CacheEvict(value="dataSourceCache",allEntries=true)
  @Override
  public void clearCache() {}

  @Override
  public void batchInsert(List<Map<String, Object>> list) {
    baseMapper.batchInsert(list);
  }

  @Override
  @Cacheable("dataSourceCache")
  public Integer getIdBySourceCode(String sourceCode) {
    if(StringUtils.isEmpty(sourceCode)){
      return null;
    }
    return baseMapper.selectOne(Wrappers.<Datasource>lambdaQuery().eq(Datasource::getSourceCode, sourceCode)).getSourceId();
  }

  @Override
  @Cacheable("dataSourceCache")
  public String getCodeBySourceId(Integer sourceId) {
    if(sourceId == null){
      return null;
    }
    return baseMapper.selectOne(Wrappers.<Datasource>lambdaQuery().eq(Datasource::getSourceId, sourceId)).getSourceCode();
  }
}
