package com.auc.mapper;

import com.auc.dao.Client;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * <p>
 * 第三方系统信息 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-21
 */
public interface ClientMapper extends BaseMapper<Client> {

  List<Client> getClientsByName(String keyword);
}
