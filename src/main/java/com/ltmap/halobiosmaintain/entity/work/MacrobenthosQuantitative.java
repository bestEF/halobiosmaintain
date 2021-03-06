package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 大型底栖动物定量表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("macrobenthos_quantitative")
public class MacrobenthosQuantitative extends Model<MacrobenthosQuantitative> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
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
     * 监测日期
     */
    private LocalDate monitorDate;

    /**
     * 水深(m)
     */
    private BigDecimal waterDepth;

    /**
     * 底质类型
     */
    private String sedimentType;

    /**
     * 采泥器类型
     */
    private String dredgerType;

    /**
     * 采样次数
     */
    private Integer samplingTimes;

    /**
     * 样方面积(m^2)
     */
    private BigDecimal quadratArea;

    /**
     * 样品厚度(cm)
     */
    private BigDecimal sampleThickness;

    /**
     * 生物种中文学名
     */
    private String biologicalChineseName;

    /**
     * 生物种拉丁名
     */
    private String biologicalLatinName;

    /**
     * 数量(个)
     */
    private Integer count;

    /**
     * 密度(个/m^2)
     */
    private BigDecimal density;

    /**
     * 重量(g)
     */
    private BigDecimal weight;

    /**
     * 生物量(g/m^2)
     */
    private BigDecimal biomass;

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

    public String getSedimentType() {
        return sedimentType;
    }

    public void setSedimentType(String sedimentType) {
        this.sedimentType = sedimentType;
    }

    public String getDredgerType() {
        return dredgerType;
    }

    public void setDredgerType(String dredgerType) {
        this.dredgerType = dredgerType;
    }

    public Integer getSamplingTimes() {
        return samplingTimes;
    }

    public void setSamplingTimes(Integer samplingTimes) {
        this.samplingTimes = samplingTimes;
    }

    public BigDecimal getQuadratArea() {
        return quadratArea;
    }

    public void setQuadratArea(BigDecimal quadratArea) {
        this.quadratArea = quadratArea;
    }

    public BigDecimal getSampleThickness() {
        return sampleThickness;
    }

    public void setSampleThickness(BigDecimal sampleThickness) {
        this.sampleThickness = sampleThickness;
    }

    public String getBiologicalChineseName() {
        return biologicalChineseName;
    }

    public void setBiologicalChineseName(String biologicalChineseName) {
        this.biologicalChineseName = biologicalChineseName;
    }

    public String getBiologicalLatinName() {
        return biologicalLatinName;
    }

    public void setBiologicalLatinName(String biologicalLatinName) {
        this.biologicalLatinName = biologicalLatinName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getDensity() {
        return density;
    }

    public void setDensity(BigDecimal density) {
        this.density = density;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getBiomass() {
        return biomass;
    }

    public void setBiomass(BigDecimal biomass) {
        this.biomass = biomass;
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
        return "MacrobenthosQuantitative{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", waterDepth=" + waterDepth +
        ", sedimentType=" + sedimentType +
        ", dredgerType=" + dredgerType +
        ", samplingTimes=" + samplingTimes +
        ", quadratArea=" + quadratArea +
        ", sampleThickness=" + sampleThickness +
        ", biologicalChineseName=" + biologicalChineseName +
        ", biologicalLatinName=" + biologicalLatinName +
        ", count=" + count +
        ", density=" + density +
        ", weight=" + weight +
        ", biomass=" + biomass +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
