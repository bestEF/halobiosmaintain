package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 沉积物粒度表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("sedimentgrain")
@Data
public class Sedimentgrain extends Model<Sedimentgrain> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 填报id
     */
    private Long reportId;

    /**
     * 站位id
     */
    private Long stationId;

    /**
     * 检测日期
     */
    private LocalDate monitorDate;

    /**
     * 采样深度(cm)
     */
    private BigDecimal samplingDepth;

    /**
     * 砾石_>4(mm)
     */
    private BigDecimal gravel1;

    /**
     * 砾石 _4-2(mm)
     */
    private BigDecimal gravel2;

    /**
     * 砂_2-1(mm)
     */
    private BigDecimal sand1;

    /**
     * 砂_1-0.5(mm)
     */
    private BigDecimal sand2;

    /**
     * 砂_0.5-0.25(mm)
     */
    private BigDecimal sand3;

    /**
     * 砂_0.25-0.125(mm)
     */
    private BigDecimal sand4;

    /**
     * 砂_0.125-0.063(mm)
     */
    private BigDecimal sand5;

    /**
     * 粉砂_0.063-0.016(mm)
     */
    private BigDecimal pinksand1;

    /**
     * 粉砂_0.016-0.004(mm)
     */
    private BigDecimal pinksand2;

    /**
     * 粘土_0.004-0.001(mm)
     */
    private BigDecimal clay1;

    /**
     * 粘土_<0.001(mm)
     */
    private BigDecimal clay2;

    /**
     * 粒组含量_砾(%)
     */
    private BigDecimal granulecontent1;

    /**
     * 粒组含量_砂(%)
     */
    private BigDecimal granulecontent2;

    /**
     * 粒组含量_粉砂(%)
     */
    private BigDecimal granulecontent3;

    /**
     * 粒组含量_粘土(%)
     */
    private BigDecimal granulecontent4;

    /**
     * 名称及代号
     */
    private String nameAndCode;

    /**
     * 粒组系数_Mdф
     */
    private BigDecimal particlecoefficient1;

    /**
     * 粒组系数_QDф
     */
    private BigDecimal particlecoefficient2;

    /**
     * 粒组系数_SKф
     */
    private BigDecimal particlecoefficient3;

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

    //站位数据
    /**
     * 站位名称
     */
    @TableField(exist = false)
    private String stationName;

    /**
     * 计划经度
     */
    @TableField(exist = false)
    private BigDecimal planLon;

    /**
     * 计划纬度
     */
    @TableField(exist = false)
    private BigDecimal planLat;

    /**
     * 实测经度
     */
    @TableField(exist = false)
    private BigDecimal realLon;

    /**
     * 实测纬度
     */
    @TableField(exist = false)
    private BigDecimal realLat;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
