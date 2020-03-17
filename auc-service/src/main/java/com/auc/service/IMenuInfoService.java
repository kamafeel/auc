package com.auc.service;

import com.auc.dao.MenuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-28
 */
public interface IMenuInfoService extends IService<MenuInfo> {

  List<Integer> getMenuIdsByRoles(List<String> roles);
}
