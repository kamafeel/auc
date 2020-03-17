package com.auc.service;

import com.auc.dao.Domain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 域信息 服务类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-23
 */
public interface IDomainService extends IService<Domain> {

    List<Domain> getDomainList();
}
