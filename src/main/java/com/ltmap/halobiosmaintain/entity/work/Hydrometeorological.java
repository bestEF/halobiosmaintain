package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 水文气象表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("hydrometeorological")
public class Hydrometeorological extends Model<Hydrometeorological> {

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
     * 水深(m)
     */
    private BigDecimal waterDepth;

    /**
     * 采样层次
     */
    private String samplingLevel;

    /**
     * 采样深度(m)
     */
    private BigDecimal samplingDepth;

    /**
     * 水温(°C)
     */
    private BigDecimal watertemperature;

    /**
     * 透明度(m)
     */
    private BigDecimal pellucidity;

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
    private String stationName;

    /**
     * 计划经度
     */
    private BigDecimal planLon;

    /**
     * 计划纬度
     */
    private BigDecimal planLat;

    /**
     * 实测经度
     */
    private BigDecimal realLon;

    /**
     * 实测纬度
     */
    private BigDecimal realLat;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public LocalDate getMonitorDate() {
        return monitorDate;
    }

    public void setMonitorDate(LocalDate monitorDate) {
        this.monitorDate = monitorDate;
    }

    public BigDecimal getWaterDepth() {
        return waterDepth;
    }

    public void setWaterDepth(BigDecimal waterDepth) {
        this.waterDepth = waterDepth;
    }

    public String getSamplingLevel() {
        return samplingLevel;
    }

    public void setSamplingLevel(String samplingLevel) {
        this.samplingLevel = samplingLevel;
    }

    public BigDecimal getSamplingDepth() {
        return samplingDepth;
    }

    public void setSamplingDepth(BigDecimal samplingDepth) {
        this.samplingDepth = samplingDepth;
    }

    public BigDecimal getWatertemperature() {
        return watertemperature;
    }

    public void setWatertemperature(BigDecimal watertemperature) {
        this.watertemperature = watertemperature;
    }

    public BigDecimal getPellucidity() {
        return pellucidity;
    }

    public void setPellucidity(BigDecimal pellucidity) {
        this.pellucidity = pellucidity;
    }

    public String getByzd1() {
        return byzd1;
    }

    public void setByzd1(String byzd1) {
        this.byzd1 = byzd1;
    }

    public String getByzd2() {
        return byzd2;
    }

    public void setByzd2(String byzd2) {
        this.byzd2 = byzd2;
    }

    public String getByzd3() {
        return byzd3;
    }

    public void setByzd3(String byzd3) {
        this.byzd3 = byzd3;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Hydrometeorological{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", waterDepth=" + waterDepth +
        ", samplingLevel=" + samplingLevel +
        ", samplingDepth=" + samplingDepth +
        ", watertemperature=" + watertemperature +
        ", pellucidity=" + pellucidity +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
