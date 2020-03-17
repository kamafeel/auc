package com.auc.mapper;

import com.auc.dao.AccountConvert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 登录名转换配置表 Mapper 接口
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-21
 */
public interface AccountConvertMapper extends BaseMapper<AccountConvert> {
  void insertList(@Param("list") List<AccountConvert> list);
}
