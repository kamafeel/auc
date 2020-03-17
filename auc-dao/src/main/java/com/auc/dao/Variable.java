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
 * 运行参数配置表
 * </p>
 *
 * @author GenerateCode
 * @since 2019-12-06
 */
@TableName("auc_ru_variable")
@Data
public class Variable extends Model<Variable> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer type;

    private String vKey;

    private String vValue;

    private String ext;

    private LocalDateTime occurTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Variable{" +
        "id=" + id +
        ", type=" + type +
        ", vKey=" + vKey +
        ", vValue=" + vValue +
        ", ext=" + ext +
        ", occurTime=" + occurTime +
        "}";
    }
}
