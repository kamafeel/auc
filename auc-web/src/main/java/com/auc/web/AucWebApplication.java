package com.auc.web;

import java.sql.DriverManager;
import java.sql.SQLException;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.auc")
@SpringBootApplication
@MapperScan("com.auc.mapper")
@EnableAsync
public class AucWebApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(AucWebApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    /**
     * You should not need them.
     * However, it is highly dependent on the driver version/OS version/Tomcat version/Java version. Even "recent" driver and JDK combinations may fail.
     * To be on the safe side, you need to use Class.forName
     */
    //Class.forName("com.mysql.jdbc.Driver");
    try {
      DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return builder.sources(AucWebApplication.class);
  }
}
