package com.base.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件存储 实体类
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/12/31 1:16
 **/
@Data
@TableName("t_sys_file_store")
public class SysFileStore extends BaseEntity<SysFileStore> {

    private static final long serialVersionUID = 5540225530500214422L;

    /**
     * 文件名
     */
    @TableField("name")
    private String name;

    /**
     * 文件路径
     */
    @TableField("path")
    private String path;

    /**
     * 文件类型
     */
    @TableField("type")
    private String type;

    /**
     * 文件后缀
     */
    @TableField("suffix")
    private String suffix;

    /**
     * 文件大小
     */
    @TableField("size")
    private Long size;

    /**
     * 访问链接
     */
    @TableField("url")
    private String url;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
