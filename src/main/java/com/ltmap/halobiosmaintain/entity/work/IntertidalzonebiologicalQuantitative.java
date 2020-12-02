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
 * 潮间带生物定量表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("intertidalzonebiological_quantitative")
public class IntertidalzonebiologicalQuantitative extends Model<IntertidalzonebiologicalQuantitative> {

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
     * 检测日期
     */
    private LocalDate monitorDate;

    /**
     * 站位id
     */
    private Long stationId;

    /**
     * 潮带
     */
    private String intertidalZone;

    /**
     * 底质类型
     */
    private String sedimentType;

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

    public LocalDate getMonitorDate() {
        return monitorDate;
    }

    public void setMonitorDate(LocalDate monitorDate) {
        this.monitorDate = monitorDate;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getIntertidalZone() {
        return intertidalZone;
    }

    public void setIntertidalZone(String intertidalZone) {
        this.intertidalZone = intertidalZone;
    }

    public String getSedimentType() {
        return sedimentType;
    }

    public void setSedimentType(String sedimentType) {
        this.sedimentType = sedimentType;
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
        return "IntertidalzonebiologicalQuantitative{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", monitorDate=" + monitorDate +
        ", stationId=" + stationId +
        ", intertidalZone=" + intertidalZone +
        ", sedimentType=" + sedimentType +
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
