package com.auc.web.config.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置类
 *
 */
@Configuration
public class KaptchaController {
    @Bean(name="captchaProducer")
    public DefaultKaptcha getKaptchaBean(){
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        //验证码是否带边框 No
        properties.setProperty("kaptcha.border", "no");
        //验证码字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        //验证码整体宽度
        properties.setProperty("kaptcha.image.width", "400");
        //验证码整体高度
        properties.setProperty("kaptcha.image.height", "125");
        //文字个数
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //文字大小
        properties.setProperty("kaptcha.textproducer.font.size","120");
        //文字随机字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        //文字距离
        properties.setProperty("kaptcha.textproducer.char.space","16");
        //干扰线颜色
        properties.setProperty("kaptcha.noise.color","blue");
        //自定义验证码样式
        properties.setProperty("kaptcha.obscurificator.impl","com.auc.web.config.kaptcha.DisKaptchaCssImpl");
        //自定义验证码背景
        properties.setProperty("kaptcha.background.impl","com.auc.web.config.kaptcha.NoKaptchaBackhround");
        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
