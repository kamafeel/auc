package com.auc.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author qin.ye
 * @since 2019-10-28
 */
@TableName("auc_re_menu_info")
public class MenuInfo extends Model<MenuInfo> {

    private static final long serialVersionUID=1L;

    /**
     * 菜单id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父级菜单id
     */
    @JsonIgnore
    private Integer parentId;

    /**
     * ID全路径
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String path;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 英文名称标识
     */
    private String enName;

    /**
     * 菜单路径
     */
    private String url;

    /**
     * 重定向
     */
    private String redirect;

    /**
     * component
     */
    private String component;

    /**
     * 是否隐藏
     */
    private boolean hidden;

    /**
     * 菜单类型
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String type;

    /**
     * 菜单是否可用
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String flag;

    /**
     * 菜单排序
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer orderNo;

    /**
     * 状态 0 正常 1 删除
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String createUser;

    /**
     * 创建时间
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String updateUser;

    /**
     * 修改时间
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

    /**
     * 子级菜单
     */
    @TableField(exist = false)
    private List<MenuInfo> children;

    /**
     * 版本号
     */
    private Integer version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<MenuInfo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuInfo> children) {
        this.children = children;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MenuInfo{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", path=" + path +
        ", name=" + name +
        ", url=" + url +
        ", type=" + type +
        ", flag=" + flag +
        ", orderNo=" + orderNo +
        ", status=" + status +
        ", remark=" + remark +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", version=" + version +
        "}";
    }
}
