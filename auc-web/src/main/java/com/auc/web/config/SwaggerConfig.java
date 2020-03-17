package com.auc.web.config;

import static com.google.common.collect.Lists.newArrayList;

import com.auc.common.constants.AucConst;
import com.auc.web.help.CustomFormHttpMessageConverter;
import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//通过@EnableSwagger2注解来启用Swagger2
@EnableSwagger2
//@ConditionalOnExpression 为Spring的注解，用户是否实例化本类，用于是否启用Swagger的判断（生产环境需要屏蔽Swagger）
@ConditionalOnExpression("${swagger.enabled:true}")
public class SwaggerConfig implements WebMvcConfigurer {

  @Bean
  public Docket productApi() {

    ParameterBuilder ticketPar = new ParameterBuilder();
    List<Parameter> pars = new ArrayList<Parameter>();
    ticketPar.name(AucConst.JWT_AUTH_TOKEN_KEY_NAME).description("JWTToken")
        .modelRef(new ModelRef("string")).parameterType("header")
        .required(false).build(); //header中的ticket参数非必填，传空也可以
    pars.add(ticketPar.build()); //根据每个方法名也知道当前方法在设置什么参数

    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.auc.web.controller"))
        .paths(Predicates.not(PathSelectors.regex("/error.*")))
        //.paths(PathSelectors.any())
        .build()
        .globalOperationParameters(pars)
        .apiInfo(apiInfo())
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.GET, newArrayList(new ResponseMessageBuilder().code(500)
                .message("500 message")
                .responseModel(new ModelRef("Error"))
                .build(),
            new ResponseMessageBuilder().code(403)
                .message("Forbidden!!!!!")
                .build()));
  }
  private ApiInfo apiInfo() {
    return new ApiInfo("AUC Swagger app",
        "description of API",
        "1.0.0", "",
        new Contact("auc", "office.gome.com.cn", "~"),
        "License of API", "API license URL",
        Collections.emptyList());
  }
}
