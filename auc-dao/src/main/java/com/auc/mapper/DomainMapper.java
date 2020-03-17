package com.auc.mapper;

import com.auc.dao.Domain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 域信息 Mapper 接口
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-23
 */
public interface DomainMapper extends BaseMapper<Domain> {

    List<Domain> getDomainList();
}
