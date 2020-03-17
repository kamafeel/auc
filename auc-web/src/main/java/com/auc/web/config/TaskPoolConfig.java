package com.auc.web.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程池
 */
@Configuration
public class TaskPoolConfig {

  @Bean(name = "logTaskExecutor")
  public Executor logTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 核心线程数
    executor.setCorePoolSize(50);
    // 最大线程数
    executor.setMaxPoolSize(100);
    // 缓存队列
    executor.setQueueCapacity(30000);
    // 允许线程的空闲时间60秒
    executor.setKeepAliveSeconds(60);
    // 线程池名的前缀
    executor.setThreadNamePrefix("logTaskExecutor-");
    // 线程池对拒绝任务的处理策略
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    executor.setWaitForTasksToCompleteOnShutdown(true);
    // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭
    executor.setAwaitTerminationSeconds(10);
    return executor;
  }

  @Bean(name = "redisExecutor")
  public Executor redisExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 核心线程数
    executor.setCorePoolSize(20);
    // 最大线程数
    executor.setMaxPoolSize(20);
    // 缓存队列
    executor.setQueueCapacity(2000);
    // 允许线程的空闲时间60秒
    executor.setKeepAliveSeconds(60);
    // 线程池名的前缀
    executor.setThreadNamePrefix("redisExecutor-");
    // 线程池对拒绝任务的处理策略
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    executor.setWaitForTasksToCompleteOnShutdown(true);
    // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭
    executor.setAwaitTerminationSeconds(60);
    return executor;
  }

  @Bean(name = "strategyExecutor")
  public Executor strategyExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 核心线程数
    executor.setCorePoolSize(30);
    // 最大线程数
    executor.setMaxPoolSize(30);
    // 缓存队列
    executor.setQueueCapacity(4048);
    // 允许线程的空闲时间60秒
    executor.setKeepAliveSeconds(60);
    // 线程池名的前缀
    executor.setThreadNamePrefix("strategyExecutor-");
    // 线程池对拒绝任务的处理策略
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    executor.setWaitForTasksToCompleteOnShutdown(true);
    // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭
    executor.setAwaitTerminationSeconds(60);
    return executor;
  }
}
