package com.auc.dao;

import com.auc.common.utils.JsonUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 第三方系统信息
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-21
 */
@TableName("auc_re_client")
@Data
public class Client extends Model<Client> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(notes="不传为新增，传则是修改")
    private Integer id;

    /**
     * 客户端ID
     */
    @ApiModelProperty(required=true, notes="客户端ID,全局唯一")
    @NotBlank(message = "系统编码不能为空")
    @Length(max = 100, message = "系统编码长度不能超过100")
    private String clientId;

    /**
     * 客户端名称
     */
    @ApiModelProperty(notes="客户端名称")
    @NotBlank(message = "系统名称不能为空")
    @Length(max = 64, message = "系统编码长度不能超过64")
    private String clientName;

    /**
     * 客户端图标
     */
    @ApiModelProperty(notes="客户端图标地址URL")
    @Length(max = 64, message = "客户端图标地址长度不能超过64")
    private String clientIcon;

    /**
     * 客户端登录URL
     */
    @ApiModelProperty(notes="客户端登录URL")
    private String clientLoginUrl;

    /**
     * 客户端密钥
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String clientSecret;

    /**
     * 鉴权类型
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String authorizedTypes;

    /**
     * 回调默认地址
     */
    @ApiModelProperty(notes="回调默认地址")
    @Length(max = 256, message = "回调地址长度不能超过256")
    private String webServerRedirectUri;

    /**
     * 客户端自定义jtw密钥
     */
    @ApiModelProperty(notes="客户端自定义jtw密钥")
    @Length(max = 256, message = "jtw密钥长度不能超过256")
    private String clientJwtSecret;

    /**
     * 0 显示， 1 不显示，
     */
    @ApiModelProperty(notes="是否在跳转列表显示（0 显示， 1 不显示）")
    @Range(max = 1, message = "状态值只能是0（显示）或1（不显示）")
    private int status;

    /**
     * 0 正常， 1 删除，
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private int delFlag;

    /**
     * 登录类型  0：域账号登录，1：系统账号登录
     */
    @ApiModelProperty(notes="登录类型（0：域账号登录，1：系统账号登录）")
    @Range(max = 1, message = "登录类型只能是0（域账号登录）或1（系统账号登录）")
    private int loginType;

    /**
     * 排序
     */
    @ApiModelProperty(notes="排序")
    private int sort;

    @ApiModelProperty(notes="备注")
    private String remarks;

    /**
     * 是否自定义登录
     */
    @ApiModelProperty(notes="是否自定义登录")
    private boolean customLogin;

    /**
     * 自定义登录范围json
     * {
     *   "SYSTEM": ["DQ","KG","TX","DC","HLW","JK"],
     *   "MOBILE": ["DQ","KG","TX","DC","HLW","JK"],
     *   "DOMAIN": ["GOMEDQ","GOMEKG"]
     * }
     */
    @ApiModelProperty(notes="自定义登录范围json")
    @Length(max = 256, message = "customLoginInfo长度不能超过256")
    private String customLoginInfo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Client{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", clientName=" + clientName +
        ", clientIcon=" + clientIcon +
        ", clientLoginUrl=" + clientLoginUrl +
        ", clientSecret=" + clientSecret +
        ", authorizedTypes=" + authorizedTypes +
        ", webServerRedirectUri=" + webServerRedirectUri +
        ", clientJwtSecret=" + clientJwtSecret +
        ", remarks=" + remarks +
        "}";
    }
}
