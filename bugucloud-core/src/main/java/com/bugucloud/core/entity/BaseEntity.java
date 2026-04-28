package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能描述: 父实体类
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:16
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键(雪花算法)")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time")
    private Date updateTime;
}
