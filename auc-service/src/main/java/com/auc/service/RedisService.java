package com.auc.service;


import com.auc.common.enums.RedisKey;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {

  void asyncDelete(String key);

  <T> T get(RedisKey key);

  String get(String key);

  Long getExpire(String key);

  <T> T get(String key, Class<T> clazz);

  <T> void delete(RedisKey key);

  <T> void delete(String key);

  <T> void set(RedisKey key, T t);

  <T> void set(RedisKey key, T t, long timeout, TimeUnit unit);

  <T> void set(String key, T t, long timeout, TimeUnit unit);

  <T> T hGet(RedisKey key, String hashKey);

  <T> void hSet(RedisKey key, String hashKey, T t);

  <T> void hDelete(RedisKey key, String hashKey);

  <T> void delete(RedisKey key, String keySuffix);

  <T> T get(RedisKey key, String keySuffix);

  boolean rangeLimitLua(RedisKey key, String keySuffix, int count, int time);

  boolean pwErrorLimitLua(RedisKey key, String keySuffix, int count, int time);

  void cuckooFilterCreate(String cuckooName, String cuckooSize);

  /**
   * 用于处理可自动过期的key
   * 
   * @param key
   * @param keySuffix
   * @param t
   * @param timeout
   * @param unit
   */
  <T> void set(RedisKey key, String keySuffix, T t, long timeout, TimeUnit unit);


  /**
   * 用于处理可自动过期的key
   * 
   * @param key
   * @param keySuffix
   * @param t
   * @param timeout
   * @param unit
   */
  <T> boolean setIfAbsent(RedisKey key, String keySuffix, T t, long timeout, TimeUnit unit);


  /**
   *   判断某个数据在key集合中是否存在
   * @param key
   * @param id
   * @param expire
   * @param <T>
   * @return
   */
  <T> Boolean isExsites(final String key, final String id, final long expire);

  /**
   * 模糊查询
   */
  Set<String> keys(RedisKey redisKey, String key);
}
