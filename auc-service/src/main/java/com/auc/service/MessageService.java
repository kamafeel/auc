package com.auc.service;

import java.util.Map;

public interface MessageService {

  /**
   * 发送验证码消息（验证码一类）
   * @param to 接收人
   * @param content 消息内容
   * @return boolean 是否发送成功
   */
  boolean send(String to, String content);

  /**
   * 将消息模板转换为消息
   * @param template 模板
   * @param map 要替换的内容
   * @return String 转换后的内容
   */
  String buildContent(String template, Map<String, ?> map );
}
