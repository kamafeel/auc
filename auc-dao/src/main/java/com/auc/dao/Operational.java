package com.auc.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 前端功能操作
 * </p>
 *
 * @author qin.ye
 * @since 2019-12-12
 */
@TableName("auc_re_operational")
public class Operational extends Model<Operational> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作父id
     */
    private Integer parentId;

    /**
     * 操作名称
     */
    private String operName;

    /**
     * 操作编码
     */
    private String operCode;

    /**
     * 拦截URL前缀
     */
    private String stopUrlPrefix;

    private LocalDateTime createTime;


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

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getStopUrlPrefix() {
        return stopUrlPrefix;
    }

    public void setStopUrlPrefix(String stopUrlPrefix) {
        this.stopUrlPrefix = stopUrlPrefix;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operational that = (Operational) o;
        return operCode.equals(that.operCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operCode);
    }

    @Override
    public String toString() {
        return "Operational{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", operName=" + operName +
        ", operCode=" + operCode +
        ", stopUrlPrefix=" + stopUrlPrefix +
        ", createTime=" + createTime +
        "}";
    }
}
