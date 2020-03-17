package com.auc.service.impl;

import com.auc.common.enums.VariableTypeEnum;
import com.auc.dao.Variable;
import com.auc.mapper.VariableMapper;
import com.auc.service.IVariableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运行参数配置表 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-06
 */
@Service
public class VariableServiceImpl extends ServiceImpl<VariableMapper, Variable> implements IVariableService {

  @Override
  public Variable getVariable(VariableTypeEnum vt, String key) {
    return baseMapper.selectOne(Wrappers.<Variable>lambdaQuery().eq(Variable::getType, vt.getType())
        .eq(Variable::getVKey, key));
  }

  @Override
  public void setVariable(VariableTypeEnum vt, String key, String value, String ext) {
    baseMapper.delete(Wrappers.<Variable>lambdaQuery().eq(Variable::getType, vt.getType())
        .eq(Variable::getVKey, key));
    Variable var = new Variable();
    var.setType(vt.getType());
    var.setVKey(key);
    var.setVValue(value);
    var.setExt(ext);
    baseMapper.insert(var);
  }
}
