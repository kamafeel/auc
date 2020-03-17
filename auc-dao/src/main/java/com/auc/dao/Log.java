package com.auc.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.common.base.Splitter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-23
 */
@TableName("auc_ru_log")
@Data
@ApiModel(value = "日志信息")
public class Log extends Model<Log> {

  private static final long serialVersionUID = 1L;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @ApiModelProperty(value = "日志类型:0正常1错误")
  private Integer logType;

  @ApiModelProperty(value = "耗时(毫秒)")
  private Long spendTime;
  /**
   * 设备信息
   */
  @ApiModelProperty(value = "设备信息")
  private String device;

  private String ip;

  @ApiModelProperty(value = "接口调用和返回信息")
  private String content;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "用户ID")
  private String userId;

  @ApiModelProperty(hidden = true)
  private LocalDateTime createTime;

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

  public void setContent(String content) {
    this.content = Splitter.fixedLength(65535).limit(1).split(content).toString();
  }

  @Override
  public String toString() {
    return "Log{" +
        "id=" + id +
        ", logType=" + logType +
        ", device=" + device +
        ", ip=" + ip +
        ", userName=" + userName +
        ", content=" + content +
        "}";
  }
}
