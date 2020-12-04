package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.io.Serializable;

/**
 * <p>
 * 鸟类观测记录主表
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@TableName("bird_observe")
public class BirdObserve extends Model<BirdObserve> {

    private static final long serialVersionUID = 1L;

    /**
     * 主表id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 保护区名称
     */
    private String protecName;

    /**
     * 日期
     */
    private LocalDate observeDate;

    /**
     * 天气
     */
    private String weather;

    /**
     * 温度
     */
    private String tem;

    /**
     * 观测地点
     */
    private String place;

    /**
     * 海拔
     */
    private String high;

    /**
     * 观测者
     */
    private String observeName;

    /**
     * 记录者
     */
    private String recordName;

    /**
     * 样线编号
     */
    private String splineNumber;

    /**
     * 样线长度
     */
    private String splineLength;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 起点经度
     */
    private BigDecimal startLon;

    /**
     * 起点纬度
     */
    private BigDecimal startLat;

    /**
     * 终点经度
     */
    private BigDecimal endLon;

    /**
     * 终点纬度
     */
    private BigDecimal endLat;

    /**
     * 总种数
     */
    private Integer totalSpecies;

    /**
     * 生境类型
     */
    private String habitatType;

    /**
     * 人为干扰类型
     */
    private String humanType;

    /**
     * 人为干扰强度
     */
    private String humanIntensity;

    /**
     * 受威胁因素
     */
    private String threatenedFactors;

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

    public String getProtecName() {
        return protecName;
    }

    public void setProtecName(String protecName) {
        this.protecName = protecName;
    }

    public LocalDate getObserveDate() {
        return observeDate;
    }

    public void setObserveDate(LocalDate observeDate) {
        this.observeDate = observeDate;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getObserveName() {
        return observeName;
    }

    public void setObserveName(String observeName) {
        this.observeName = observeName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getSplineNumber() {
        return splineNumber;
    }

    public void setSplineNumber(String splineNumber) {
        this.splineNumber = splineNumber;
    }

    public String getSplineLength() {
        return splineLength;
    }

    public void setSplineLength(String splineLength) {
        this.splineLength = splineLength;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getStartLon() {
        return startLon;
    }

    public void setStartLon(BigDecimal startLon) {
        this.startLon = startLon;
    }

    public BigDecimal getStartLat() {
        return startLat;
    }

    public void setStartLat(BigDecimal startLat) {
        this.startLat = startLat;
    }

    public BigDecimal getEndLon() {
        return endLon;
    }

    public void setEndLon(BigDecimal endLon) {
        this.endLon = endLon;
    }

    public BigDecimal getEndLat() {
        return endLat;
    }

    public void setEndLat(BigDecimal endLat) {
        this.endLat = endLat;
    }

    public Integer getTotalSpecies() {
        return totalSpecies;
    }

    public void setTotalSpecies(Integer totalSpecies) {
        this.totalSpecies = totalSpecies;
    }

    public String getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(String habitatType) {
        this.habitatType = habitatType;
    }

    public String getHumanType() {
        return humanType;
    }

    public void setHumanType(String humanType) {
        this.humanType = humanType;
    }

    public String getHumanIntensity() {
        return humanIntensity;
    }

    public void setHumanIntensity(String humanIntensity) {
        this.humanIntensity = humanIntensity;
    }

    public String getThreatenedFactors() {
        return threatenedFactors;
    }

    public void setThreatenedFactors(String threatenedFactors) {
        this.threatenedFactors = threatenedFactors;
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
        return "BirdObserve{" +
        "id=" + id +
        ", protecName=" + protecName +
        ", observeDate=" + observeDate +
        ", weather=" + weather +
        ", tem=" + tem +
        ", place=" + place +
        ", high=" + high +
        ", observeName=" + observeName +
        ", recordName=" + recordName +
        ", splineNumber=" + splineNumber +
        ", splineLength=" + splineLength +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", startLon=" + startLon +
        ", startLat=" + startLat +
        ", endLon=" + endLon +
        ", endLat=" + endLat +
        ", totalSpecies=" + totalSpecies +
        ", habitatType=" + habitatType +
        ", humanType=" + humanType +
        ", humanIntensity=" + humanIntensity +
        ", threatenedFactors=" + threatenedFactors +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
