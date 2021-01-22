package com.base.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 基础实体
 *
 * @author ming
 * @since 2020/04/16
 */
@Data
@Accessors(chain = true)
public abstract class BaseEntity<T> extends Model<BaseEntity<T>> {

    private static final long serialVersionUID = 5796049886922555842L;
    @TableId(value = "id")
    public String id;

    /**
     * 1: true 0:false
     * default 1
     */
    @TableField("valid")
    public Boolean valid;

    /**
     * 创建者
     */
    @TableField("creator")
    public String creator;

    /**
     * 修改者
     */
    @TableField("modifier")
    public String modifier;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    public String createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("modify_time")
    public String modifyTime;

    /**
     * 查询关键字
     */
    @TableField(exist = false)
    public String searchKey;

    /**
     * 可用性(0: 正常 1: 删除)
     */
    public static final Integer VALID = 0;
    public static final Integer IN_VALID = 1;
}
