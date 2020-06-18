package com.auc.web.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * redis配置
 */

@Configuration
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig extends CachingConfigurerSupport {

  @Value("${cache.ttl.client}")
  private int clientTtl;
  @Value("${cache.ttl.user}")
  private int userTtl;
  @Value("${cache.ttl.role}")
  private int roleTtl;
  @Value("${cache.ttl.default}")
  private int defaultTtl;
  @Value("${cache.ttl.dataSource}")
  private int dsTtl;

  @Override
  @Bean
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append(method.getName());
        for (Object obj : params) {
          if(obj!=null){
            sb.append(obj.toString());
          }
        }
        return sb.toString();
      }
    };
  }

  /**
   * 最新版，设置redis缓存过期时间
   */
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
        //默认都是60秒过期
        this.getRedisCacheConfigurationWithTtl( defaultTtl), this.getRedisCacheConfigurationMap() // 指定 key 策略
    );
  }

  private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
    Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
    //对不同cache进行过期时间配置
    redisCacheConfigurationMap.put("clientCache", this.getRedisCacheConfigurationWithTtl( clientTtl));
    redisCacheConfigurationMap.put("roleCache", this.getRedisCacheConfigurationWithTtl( roleTtl));
    redisCacheConfigurationMap.put("userCache", this.getRedisCacheConfigurationWithTtl( userTtl));
    redisCacheConfigurationMap.put("dataSourceCache", this.getRedisCacheConfigurationWithTtl( dsTtl));
    return redisCacheConfigurationMap;
  }

  private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
    redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
        RedisSerializationContext
            .SerializationPair
            .fromSerializer(jackson2JsonRedisSerializer)
    ).entryTtl(Duration.ofSeconds(seconds));
    return redisCacheConfiguration;
  }

  @Bean("stringSerializableRedisTemplate")
  public RedisTemplate<String, Serializable> limitRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Serializable> template = new RedisTemplate<>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
    RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    //默认使用JdkSerializationRedisSerializer序列化方式;会出现乱码，改成StringRedisSerializer
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
    return redisTemplate;
  }

  @Bean
  @ConditionalOnMissingBean(StringRedisTemplate.class)
  public StringRedisTemplate stringRedisTemplate(
      LettuceConnectionFactory redisConnectionFactory) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }


  @Bean("concurrentLimitLua")
  public DefaultRedisScript<Number> getConcurrentRedisScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/concurrent.limit.lua"));
    redisScript.setResultType(java.lang.Number.class);
    return redisScript;
  }

  @Bean("rangeLimitLua")
  public DefaultRedisScript<Number> getRangelimitLua() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/range.limit.lua"));
    redisScript.setResultType(java.lang.Number.class);
    return redisScript;
  }

  @Bean("rateLimitLua")
  public DefaultRedisScript<Number> getRateRedisScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/rate.limit.lua"));
    redisScript.setResultType(java.lang.Number.class);
    return redisScript;
  }

  @Bean("pwErrorLimitLua")
  public DefaultRedisScript<Number> getPWErrorLimitLuaRedisScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/pw.error.limit.lua"));
    redisScript.setResultType(java.lang.Number.class);
    return redisScript;
  }

  @Bean("cuckooFilterCreateLua")
  public DefaultRedisScript cuckooFilterCreate() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/cuckoofilter.lua"));
    return redisScript;
  }

  @Bean("layerBloomFilterAddCreateLua")
  public DefaultRedisScript<Number> layerBloomFilterAddCreate() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/bloom-filter/layer-add.lua"));
    redisScript.setResultType(java.lang.Number.class);
    return redisScript;
  }

  @Bean("layerBloomFilterCheckCreateLua")
  public DefaultRedisScript<Boolean> layerBloomFilterCheckCreate() {
    DefaultRedisScript redisScript = new DefaultRedisScript();
    redisScript.setLocation(new ClassPathResource("lua/bloom-filter/layer-check.lua"));
    redisScript.setResultType(java.lang.Boolean.class);
    return redisScript;
  }

  @Bean
  public RedisLockRegistry redisLockRegistry(LettuceConnectionFactory redisConnectionFactory) {
    return new RedisLockRegistry(redisConnectionFactory, "auc-lock");
  }

}
