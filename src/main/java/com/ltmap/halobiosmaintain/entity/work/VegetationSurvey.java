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
 * 植被调查记录主表
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@TableName("vegetation_survey")
public class VegetationSurvey extends Model<VegetationSurvey> {

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
    private LocalDate surveyDate;

    /**
     * 天气
     */
    private String weather;

    /**
     * 调查地点
     */
    private String place;

    /**
     * 样方编号
     */
    private String num;

    /**
     * 样方面积
     */
    private BigDecimal area;

    /**
     * 海拔
     */
    private String high;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

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
     * 植被类型及组成
     */
    private String vegetationType;

    /**
     * 优势种
     */
    private String dominantSpecies;

    /**
     * 总盖度
     */
    private String totalCoverage;

    /**
     * 调查人
     */
    private String surveyName;

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

    public LocalDate getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(LocalDate surveyDate) {
        this.surveyDate = surveyDate;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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

    public String getVegetationType() {
        return vegetationType;
    }

    public void setVegetationType(String vegetationType) {
        this.vegetationType = vegetationType;
    }

    public String getDominantSpecies() {
        return dominantSpecies;
    }

    public void setDominantSpecies(String dominantSpecies) {
        this.dominantSpecies = dominantSpecies;
    }

    public String getTotalCoverage() {
        return totalCoverage;
    }

    public void setTotalCoverage(String totalCoverage) {
        this.totalCoverage = totalCoverage;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
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
        return "VegetationSurvey{" +
        "id=" + id +
        ", protecName=" + protecName +
        ", surveyDate=" + surveyDate +
        ", weather=" + weather +
        ", place=" + place +
        ", num=" + num +
        ", area=" + area +
        ", high=" + high +
        ", lon=" + lon +
        ", lat=" + lat +
        ", habitatType=" + habitatType +
        ", humanType=" + humanType +
        ", humanIntensity=" + humanIntensity +
        ", threatenedFactors=" + threatenedFactors +
        ", vegetationType=" + vegetationType +
        ", dominantSpecies=" + dominantSpecies +
        ", totalCoverage=" + totalCoverage +
        ", surveyName=" + surveyName +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
