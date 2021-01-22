package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典 实体类
 *
 * @author ming
 * @since 2020/04/16
 */
@TableName("t_sys_dict")
@Data
public class SysDict extends BaseEntity<SysDict> {

    private static final long serialVersionUID = -1553699349812847206L;

    /**
     * 字典编码
     */
    @TableField("dict_code")
    private String dictCode;

    /**
     * 字典名
     */
    @TableField("dict_name")
    private String dictName;

    /**
     * 父ID
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
