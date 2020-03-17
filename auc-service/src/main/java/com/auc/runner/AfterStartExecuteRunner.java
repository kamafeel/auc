package com.auc.runner;

import com.auc.common.utils.ConsistentHashRouter;
import com.auc.service.RedisService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 启动后放入热点数据(合并用户等)到redis
 */
@Slf4j
@Component
@Order(1)
public class AfterStartExecuteRunner implements CommandLineRunner {

  @Autowired
  private RedisService redisService;
  @Autowired
  private ConsistentHashRouter<String> consistentHashRouter;

  @Autowired
  private Environment environment;

  @Override
  public void run(String... strings) throws Exception {
    //布谷鸟过滤器
    //redisService.cuckooFilterCreate("disableUser","64K");
    //域多地址负载均衡
    this.domainLoadBalancing();
  }

  private void domainLoadBalancing() {
    consistentHashRouter
        .init(Arrays.asList(environment.getProperty("domain.gomedq.address").split(",")), 150);
  }

}
