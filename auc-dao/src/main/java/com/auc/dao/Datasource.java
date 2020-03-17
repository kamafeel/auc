package com.auc.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 记录数据来源或国美各板块信息
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-11
 */
@TableName("auc_re_datasource")
@Data
public class Datasource extends Model<Datasource> {

    private static final long serialVersionUID=1L;

    /**
     * 数据源ID
     */
    @TableId(value = "source_id", type = IdType.AUTO)
    private Integer sourceId;

    /**
     * 数据源名称
     */
    private String sourceName;

    /**
     * 数据源编码
     */
    private String sourceCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 更新日期
     */
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.sourceId;
    }

    @Override
    public String toString() {
        return "Datasource{" +
        "sourceId=" + sourceId +
        ", sourceName=" + sourceName +
        ", sourceCode=" + sourceCode +
        ", description=" + description +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
