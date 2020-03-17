package com.auc.service;

import java.util.List;
import java.util.Map;

/**
 * 缓存中间层接口
 * @author zhangqi
 */
public interface ICacheService {
  List<Map<String, Object>> getAllMergeAccounts();

}
