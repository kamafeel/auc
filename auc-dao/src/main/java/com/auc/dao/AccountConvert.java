package com.auc.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 登录名转换配置表
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-22
 */
@TableName("auc_re_account_convert")
public class AccountConvert extends Model<AccountConvert> {

    private static final long serialVersionUID=1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 第三方系统ID
     */
    @NotBlank(message = "第三方系统ID不能为空")
    private String clientId;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String clientName;

    /**
     * 转换账号对应用户的数据源
     */
    @NotBlank(message = "数据源不能为空")
    private String sourceCode;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String sourceName;

    /**
     * 转换账号对应用户名
     */
    private String convertLoginName;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private String createUser;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden = true)
    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getConvertLoginName() {
        return convertLoginName;
    }

    public void setConvertLoginName(String convertLoginName) {
        this.convertLoginName = convertLoginName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AccountConvert{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", sourceCode=" + sourceCode +
        ", convertLoginName=" + convertLoginName +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        "}";
    }
}
