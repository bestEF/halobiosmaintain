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
 * 游泳动物生物鉴定表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("swimminganimal_identification")
public class SwimminganimalIdentification extends Model<SwimminganimalIdentification> {

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
     * 种名
     */
    private String specificName;

    /**
     * 全长(mm)
     */
    private BigDecimal overallLength;

    /**
     * 体长/胴长(mm)
     */
    private BigDecimal bodyLength;

    /**
     * 体重(g)
     */
    private BigDecimal weight;

    /**
     * 纯体重(g)
     */
    private BigDecimal pureWeight;

    /**
     * 胃含物(级)
     */
    private String stomachContent;

    /**
     * 性别
     */
    private String sex;

    /**
     * 性腺成熟度(级)
     */
    private String fishgonadMaturity;

    /**
     * 年龄
     */
    private String age;

    /**
     * 备注
     */
    private String remark;

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

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(String specificName) {
        this.specificName = specificName;
    }

    public BigDecimal getOverallLength() {
        return overallLength;
    }

    public void setOverallLength(BigDecimal overallLength) {
        this.overallLength = overallLength;
    }

    public BigDecimal getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(BigDecimal bodyLength) {
        this.bodyLength = bodyLength;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getPureWeight() {
        return pureWeight;
    }

    public void setPureWeight(BigDecimal pureWeight) {
        this.pureWeight = pureWeight;
    }

    public String getStomachContent() {
        return stomachContent;
    }

    public void setStomachContent(String stomachContent) {
        this.stomachContent = stomachContent;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFishgonadMaturity() {
        return fishgonadMaturity;
    }

    public void setFishgonadMaturity(String fishgonadMaturity) {
        this.fishgonadMaturity = fishgonadMaturity;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "SwimminganimalIdentification{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", specificName=" + specificName +
        ", overallLength=" + overallLength +
        ", bodyLength=" + bodyLength +
        ", weight=" + weight +
        ", pureWeight=" + pureWeight +
        ", stomachContent=" + stomachContent +
        ", sex=" + sex +
        ", fishgonadMaturity=" + fishgonadMaturity +
        ", age=" + age +
        ", remark=" + remark +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
