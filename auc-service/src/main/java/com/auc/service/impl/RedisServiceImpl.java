package com.auc.service.impl;

import com.auc.common.enums.RedisKey;
import com.auc.service.RedisService;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

  private static final String SUFFIX_SEPARATOR = "_";

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @Resource
  private StringRedisTemplate stringRedisTemplate;

  @Autowired
  private RedisTemplate<String, Serializable> stringSerializableRedisTemplate;

  @Qualifier("rangeLimitLua")
  @Resource
  RedisScript<Long> rangeLimitLua;

  @Qualifier("pwErrorLimitLua")
  @Resource
  RedisScript<Long> pwErrorLimitLua;

  @Qualifier("cuckooFilterCreateLua")
  @Resource
  RedisScript cuckooFilterCreateLua;

  private <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
    if (o == null) {
      return null;
    }
    if (!clazz.isAssignableFrom(o.getClass())) {
      throw new RuntimeException(
          String.format("[Redis Error] except type %s but %s", clazz.getName(), o.getClass()));
    }
    return clazz.cast(o);
  }

  private <T> void validateSetOperate(RedisKey key, T t) {
    if (t == null) {
      throw new RuntimeException("[Redis Error] value must not be null");
    }
    if (!key.getClazz().isAssignableFrom(t.getClass())) {
      throw new RuntimeException(
          String.format("[Redis Error] value type not match,except type %s but %s", key.getClazz(),
              t.getClass()));
    }
  }


  @Override
  public <T> T get(RedisKey key) {
    Object obj = redisTemplate.opsForValue().get(key.toString());
    return convertInstanceOfObject(obj, key.getClazz());
  }

  @Override
  public String get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

  @Override
  public Long getExpire(String key) {
    return redisTemplate.getExpire(key,TimeUnit.SECONDS);
  }

  @Override
  public <T> T get(String key, Class<T> clazz) {
    Object obj = redisTemplate.opsForValue().get(key.toString());
    return convertInstanceOfObject(obj, clazz);
  }

  @Override
  public <T> void set(RedisKey key, T t) {
    validateSetOperate(key, t);
    redisTemplate.opsForValue().set(key.toString(), t);
  }

  @Override
  public <T> void set(RedisKey key, T t, long timeout, TimeUnit unit) {
    validateSetOperate(key, t);
    redisTemplate.opsForValue().set(key.toString(), t, timeout, unit);
  }

  @Override
  public <T> void set(String key, T t, long timeout, TimeUnit unit) {
    redisTemplate.opsForValue().set(key, t, timeout, unit);
  }

  @Override
  public <T> T hGet(RedisKey key, String hashKey) {
    Object obj = redisTemplate.opsForHash().get(key.toString(), hashKey);
    return convertInstanceOfObject(obj, key.getClazz());
  }

  @Override
  public <T> void hSet(RedisKey key, String hashKey, T t) {
    validateSetOperate(key, t);
    redisTemplate.opsForHash().put(key.toString(), hashKey, t);
  }

  @Override
  public <T> void delete(RedisKey key) {
    redisTemplate.delete(key.toString());
  }

  @Override
  public <T> void delete(String key) {
    redisTemplate.delete(key);
  }

  @Async("redisExecutor")
  @Override
  public void asyncDelete(String key) {
    redisTemplate.delete(key);
  }

  @Override
  public <T> void hDelete(RedisKey key, String hashKey) {
    redisTemplate.opsForHash().delete(key.toString(), hashKey);
  }

  @Override
  public <T> T get(RedisKey key, String keySuffix) {
    String realKey = key.toString() + SUFFIX_SEPARATOR + keySuffix;
    Object obj = redisTemplate.opsForValue().get(realKey);
    return convertInstanceOfObject(obj, key.getClazz());
  }

  @Override
  public boolean rangeLimitLua(RedisKey key, String keySuffix, int count, int time) {
    return this.limit(key,rangeLimitLua,keySuffix,count,time);
  }

  /**
   * 默认增加AucConst.PW_ERROR_REDIS_PATH 前缀
   * @param key
   * @param keySuffix
   * @param count
   * @param time
   * @return
   */
  @Override
  public boolean pwErrorLimitLua(RedisKey key, String keySuffix, int count, int time) {
    return this.limit(key,pwErrorLimitLua,keySuffix,count,time);
  }

  private boolean limit(RedisKey key, RedisScript<Long> redisScript,String keySuffix, int count, int time) {
    String realKey = key + SUFFIX_SEPARATOR + keySuffix;
    Number number = stringSerializableRedisTemplate.execute(redisScript, Collections.singletonList(realKey), count, time);
    if (number != null && number.intValue() != 0) {
      return true;
    }
    return false;
  }

  @Override
  public void cuckooFilterCreate(String cuckooName, String cuckooSize) {
    stringSerializableRedisTemplate.execute(cuckooFilterCreateLua,null, cuckooName,cuckooSize);
  }

  @Override
  public <T> void set(RedisKey key, String keySuffix, T t, long timeout, TimeUnit unit) {
    validateSetOperate(key, t);
    String realKey = key.toString() + SUFFIX_SEPARATOR + keySuffix;
    redisTemplate.opsForValue().set(realKey, t, timeout, unit);
  }

  @Override
  public <T> boolean setIfAbsent(RedisKey key, String keySuffix, T t, long timeout, TimeUnit unit) {
    validateSetOperate(key, t);
    String realKey = key.toString() + SUFFIX_SEPARATOR + keySuffix;
    boolean res = redisTemplate.opsForValue().setIfAbsent(realKey, t);
    if (res) {
      redisTemplate.expire(realKey, timeout, unit);
    }
    return res;
  }

  @Override
  public <T> void delete(RedisKey key, String keySuffix) {
    String realKey = key.toString() + SUFFIX_SEPARATOR + keySuffix;
    redisTemplate.delete(realKey);
  }

  /**
   * 判断key集合中是否存在某个数据
   */
  @Override
  public <T> Boolean isExsites(final String key, final String value, final long expire) {
    final byte[] keys = key.getBytes();
    final byte[] values = value.getBytes();
    return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
      @Override
      public Boolean doInRedis(RedisConnection redisConnection) {
        if (!redisConnection.exists(keys)) {
          redisConnection.sAdd(keys, values);
          redisConnection.expire(keys, expire);
          return false;
        }
        try {
          return validateKeys(redisConnection, keys, values);
        } finally {
          redisConnection.unwatch();
        }
      }
    });
  }


  @Override
  /**
   * 模糊查询匹配
   */
  public Set<String> keys(RedisKey redisKey, String key) {
    String realKey = redisKey.toString() + SUFFIX_SEPARATOR + "*" + key + "*";
    return redisTemplate.keys(realKey);
  }

  /**
   * 判断是否存在
   */
  private Boolean validateKeys(RedisConnection redisConnection, byte[] keys, byte[] values) {
    redisConnection.watch(keys);
    if (redisConnection.sIsMember(keys, values)) {
      return true;
    } else {
      redisConnection.sAdd(keys, values);
      return false;
    }
  }

}
