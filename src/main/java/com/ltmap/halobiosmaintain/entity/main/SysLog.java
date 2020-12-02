package com.ltmap.halobiosmaintain.entity.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_log")
public class SysLog extends Model<SysLog> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 操作类型
     */
    private String opType;

    /**
     * 操作用户
     */
    private String userId;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatTime;

    /**
     * 操作内容
     */
    private String operatContent;

    /**
     * 备用字段1
     */
    private String byzd1;

    /**
     * 备用字段2
     */
    private String byzd2;

    /**
     * 备用字段3
     */
    private String byzd3;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
