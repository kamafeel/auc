package com.auc.service;

import com.auc.common.enums.VariableTypeEnum;
import com.auc.dao.Variable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 运行参数配置表 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-06
 */
public interface IVariableService extends IService<Variable> {
  Variable getVariable(VariableTypeEnum vt,String key);
  void setVariable(VariableTypeEnum vt,String key,String value, String ext);
}
