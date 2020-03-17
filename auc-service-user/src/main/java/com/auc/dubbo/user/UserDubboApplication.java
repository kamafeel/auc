package com.auc.dubbo.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.auc.dubbo.user")
@SpringBootApplication
@MapperScan("com.auc.dubbo.user.mapper")
@EnableAutoConfiguration
@EnableDubbo
public class UserDubboApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(UserDubboApplication.class)
        .web(WebApplicationType.NONE)
        .run(args);
  }

}
