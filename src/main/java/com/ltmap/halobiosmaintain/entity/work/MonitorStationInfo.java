package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Niko
 * @since 2020-11-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("monitor_station_info")
public class MonitorStationInfo extends Model<MonitorStationInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 站位id
     */
      @TableId(value = "station_id", type = IdType.AUTO)
    private Long stationId;

    /**
     * 站位名称
     */
    private String stationName;

    /**
     * 站位所在地
     */
    private String stationLocation;

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

    /**
     * 水质类别
     */
    private String waterClass;

    /**
     * 沉积物类别
     */
    private String sedimentClass;

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

    /**
     * 存储数据类型，用分号隔开
     */
    private String dataType;

    /**
     * 填报id
     */
    private Long reportId;


    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationLocation() {
        return stationLocation;
    }

    public void setStationLocation(String stationLocation) {
        this.stationLocation = stationLocation;
    }

    public BigDecimal getPlanLon() {
        return planLon;
    }

    public void setPlanLon(BigDecimal planLon) {
        this.planLon = planLon;
    }

    public BigDecimal getPlanLat() {
        return planLat;
    }

    public void setPlanLat(BigDecimal planLat) {
        this.planLat = planLat;
    }

    public BigDecimal getRealLon() {
        return realLon;
    }

    public void setRealLon(BigDecimal realLon) {
        this.realLon = realLon;
    }

    public BigDecimal getRealLat() {
        return realLat;
    }

    public void setRealLat(BigDecimal realLat) {
        this.realLat = realLat;
    }

    public String getWaterClass() {
        return waterClass;
    }

    public void setWaterClass(String waterClass) {
        this.waterClass = waterClass;
    }

    public String getSedimentClass() {
        return sedimentClass;
    }

    public void setSedimentClass(String sedimentClass) {
        this.sedimentClass = sedimentClass;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    protected Serializable pkVal() {
        return this.stationId;
    }

    @Override
    public String toString() {
        return "MonitorStationInfo{" +
        "stationId=" + stationId +
        ", stationName=" + stationName +
        ", stationLocation=" + stationLocation +
        ", planLon=" + planLon +
        ", planLat=" + planLat +
        ", realLon=" + realLon +
        ", realLat=" + realLat +
        ", waterClass=" + waterClass +
        ", sedimentClass=" + sedimentClass +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        ", dataType=" + dataType +
        ", reportId=" + reportId +
        "}";
    }
}
