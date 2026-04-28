package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 标签字典表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_tag")
@Data
@Schema(description = "标签字典表")
public class Tag extends BaseEntity {

    @Schema(description = "标签名称(唯一)")
    @TableField(value = "name")
    private String name;
}
