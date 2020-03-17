package com.auc.service.impl;

import com.auc.dao.Domain;
import com.auc.mapper.DomainMapper;
import com.auc.service.IDomainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 域信息 服务实现类
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-23
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements IDomainService {

   public List<Domain> getDomainList(){
       return baseMapper.getDomainList();
    }
}
