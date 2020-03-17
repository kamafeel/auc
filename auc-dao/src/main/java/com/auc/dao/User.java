package com.auc.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-06
 */
@TableName("auc_re_user")
@Data
@ApiModel(value = "用户信息")
public class User extends Model<User> {

    private static final long serialVersionUID=1L;

    /**
     * 和智慧办公保持一致
     */
    private Integer id;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名")
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 数据源ID
     */
    private Integer sourceId;

    @TableField(exist = false)
    private String sourceCode;

    @ApiModelProperty(value = "数据源名称")
    @TableField(exist = false)
    private String sourceName;

    /**
     * 员工编码
     */
    private String personnelCode;

    private String mobilePhone;

    private String email;

    /**
     *
     */
    @ApiModelProperty(value = "用户状态(1激活,0禁用)")
    private Integer status;

    @ApiModelProperty(value = "在职状态(0离职,1在职)")
    private Integer deleteFlag;

    /**
     * 密码
     */
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * 盐值
     */
    @ApiModelProperty(hidden = true)
    private String salt;

    @ApiModelProperty(value = "PSID")
    private String psId;

    @ApiModelProperty(value = "应用系统账号")
    private String appAccount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @ApiModelProperty(value = "用户角色")
    @TableField(exist = false)
    private List<Role> roles;

    @ApiModelProperty(value = "用户扩展信息")
    @TableField(exist = false)
    private Map<String,String> ext;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", userName=" + userName +
        ", realName=" + realName +
        ", sourceId=" + sourceId +
        ", personnelCode=" + personnelCode +
        ", mobilePhone=" + mobilePhone +
        ", email=" + email +
        ", status=" + status +
        ", password=" + password +
        ", salt=" + salt +
        ", psId=" + psId +
        ", appAccount=" + appAccount +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
