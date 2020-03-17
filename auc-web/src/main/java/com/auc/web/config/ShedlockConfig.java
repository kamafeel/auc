package com.auc.web.config;

import java.time.Duration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock.InterceptMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 最大默认锁定时间1小时 前缀PT是固定的，最后的S代表秒，对应的还有M，分钟；H，小时 需配合Scheduling使用
 *
 * @author zhangqi
 */

@Configuration
@EnableScheduling
//InterceptMode.PROXY_METHOD 当前仅支持返回void的方法
//不允许作用于private
@EnableSchedulerLock(interceptMode = InterceptMode.PROXY_METHOD, defaultLockAtMostFor = "PT1H")
public class ShedlockConfig {

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
    return new RedisLockProvider(connectionFactory);
  }
}