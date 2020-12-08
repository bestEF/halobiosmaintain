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
 * 大型底栖动物定性表
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
@TableName("macrobenthos_qualitative")
public class MacrobenthosQualitative extends Model<MacrobenthosQualitative> {

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
     * 网型
     */
    private String netType;

    /**
     * 网口宽度(m)
     */
    private BigDecimal netPortDensity;

    /**
     * 拖网距离(m)
     */
    private BigDecimal trawlDistance;

    /**
     * 拖网时间(min)
     */
    private BigDecimal trawlTime;

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

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public BigDecimal getNetPortDensity() {
        return netPortDensity;
    }

    public void setNetPortDensity(BigDecimal netPortDensity) {
        this.netPortDensity = netPortDensity;
    }

    public BigDecimal getTrawlDistance() {
        return trawlDistance;
    }

    public void setTrawlDistance(BigDecimal trawlDistance) {
        this.trawlDistance = trawlDistance;
    }

    public BigDecimal getTrawlTime() {
        return trawlTime;
    }

    public void setTrawlTime(BigDecimal trawlTime) {
        this.trawlTime = trawlTime;
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
        return "MacrobenthosQualitative{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", netType=" + netType +
        ", netPortDensity=" + netPortDensity +
        ", trawlDistance=" + trawlDistance +
        ", trawlTime=" + trawlTime +
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
