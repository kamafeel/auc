package com.auc.service.impl;

import com.auc.service.MessageService;
import com.auc.utils.HttpUtils;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SmsMessageServiceImpl implements MessageService {

  /**
   * 短信发送api
   */
  private static final String SEND_SMS_MESSAGE_API = "/MWGate/wmgw.asmx/MongateCsSpSendSmsNew";

  /**
   * 短信模板字符替换正则表达式pattern
   */
  private static final Pattern TEMPLATE_PATTERN = Pattern.compile("[{]([^{}]*?)[}]");

  @Value("${message.sms.address}")
  private String address;

  @Value("${message.sms.userId}")
  private String userId;

  @Value("${message.sms.password}")
  private String password;

  @Autowired
  private HttpUtils httpUtils;

  @Override
  public boolean send(String to, String content) {
    HashMap<String,Object> params = createBaseParams(to, content);
    try {
      String result = httpUtils.formPost(address + SEND_SMS_MESSAGE_API, params);
      log.info("发送结果 === {}", result);
    } catch (Exception e) {
      log.error("发送失败 === {}", e.getMessage());
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 创建基本的短信发送参数
   * 包含发送方的ID和密码
   * @return
   */
  /**
   * 创建基本的短信发送参数
   * 包含发送方的ID和密码
   * @param mobilePhone 接收人手机号
   * @param content 短信内容
   * @return JSONObject
   */
  private HashMap<String,Object> createBaseParams(String mobilePhone, String content) {
    HashMap<String,Object> param = new HashMap();
    param.put("userId", userId);
    param.put("password", password);
    param.put("iMobiCount", 1);
    param.put("pszSubPort", "*");
    param.put("pszMobis", mobilePhone); // 接收人手机号
    param.put("pszMsg", content); // 短信内容
    return param;
  }

  @Override
  public String buildContent(String template, Map<String, ?> map ){
    Scanner scanner = new Scanner(template);
    StringBuffer result = new StringBuffer();
    try{
      while (scanner.hasNext()) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(scanner.nextLine());
        while (matcher.find()) { //查找并替换参数
          //从map中根据key获取值
          matcher.appendReplacement(result, map.get(matcher.group(1)) != null ? map.get(matcher.group(1)).toString() : "");
        }
        matcher.appendTail(result);
      }
    }catch(Exception e){
      log.error("替换短信模板内容报错: {}", e.getMessage());
      e.printStackTrace();
    }finally{
      scanner.close();
    }
    return result.toString();
  }

}
