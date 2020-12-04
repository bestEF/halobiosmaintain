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
 * 沉积物粒度表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("sedimentgrain")
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

    public BigDecimal getSamplingDepth() {
        return samplingDepth;
    }

    public void setSamplingDepth(BigDecimal samplingDepth) {
        this.samplingDepth = samplingDepth;
    }

    public BigDecimal getGravel1() {
        return gravel1;
    }

    public void setGravel1(BigDecimal gravel1) {
        this.gravel1 = gravel1;
    }

    public BigDecimal getGravel2() {
        return gravel2;
    }

    public void setGravel2(BigDecimal gravel2) {
        this.gravel2 = gravel2;
    }

    public BigDecimal getSand1() {
        return sand1;
    }

    public void setSand1(BigDecimal sand1) {
        this.sand1 = sand1;
    }

    public BigDecimal getSand2() {
        return sand2;
    }

    public void setSand2(BigDecimal sand2) {
        this.sand2 = sand2;
    }

    public BigDecimal getSand3() {
        return sand3;
    }

    public void setSand3(BigDecimal sand3) {
        this.sand3 = sand3;
    }

    public BigDecimal getSand4() {
        return sand4;
    }

    public void setSand4(BigDecimal sand4) {
        this.sand4 = sand4;
    }

    public BigDecimal getSand5() {
        return sand5;
    }

    public void setSand5(BigDecimal sand5) {
        this.sand5 = sand5;
    }

    public BigDecimal getPinksand1() {
        return pinksand1;
    }

    public void setPinksand1(BigDecimal pinksand1) {
        this.pinksand1 = pinksand1;
    }

    public BigDecimal getPinksand2() {
        return pinksand2;
    }

    public void setPinksand2(BigDecimal pinksand2) {
        this.pinksand2 = pinksand2;
    }

    public BigDecimal getClay1() {
        return clay1;
    }

    public void setClay1(BigDecimal clay1) {
        this.clay1 = clay1;
    }

    public BigDecimal getClay2() {
        return clay2;
    }

    public void setClay2(BigDecimal clay2) {
        this.clay2 = clay2;
    }

    public BigDecimal getGranulecontent1() {
        return granulecontent1;
    }

    public void setGranulecontent1(BigDecimal granulecontent1) {
        this.granulecontent1 = granulecontent1;
    }

    public BigDecimal getGranulecontent2() {
        return granulecontent2;
    }

    public void setGranulecontent2(BigDecimal granulecontent2) {
        this.granulecontent2 = granulecontent2;
    }

    public BigDecimal getGranulecontent3() {
        return granulecontent3;
    }

    public void setGranulecontent3(BigDecimal granulecontent3) {
        this.granulecontent3 = granulecontent3;
    }

    public BigDecimal getGranulecontent4() {
        return granulecontent4;
    }

    public void setGranulecontent4(BigDecimal granulecontent4) {
        this.granulecontent4 = granulecontent4;
    }

    public String getNameAndCode() {
        return nameAndCode;
    }

    public void setNameAndCode(String nameAndCode) {
        this.nameAndCode = nameAndCode;
    }

    public BigDecimal getParticlecoefficient1() {
        return particlecoefficient1;
    }

    public void setParticlecoefficient1(BigDecimal particlecoefficient1) {
        this.particlecoefficient1 = particlecoefficient1;
    }

    public BigDecimal getParticlecoefficient2() {
        return particlecoefficient2;
    }

    public void setParticlecoefficient2(BigDecimal particlecoefficient2) {
        this.particlecoefficient2 = particlecoefficient2;
    }

    public BigDecimal getParticlecoefficient3() {
        return particlecoefficient3;
    }

    public void setParticlecoefficient3(BigDecimal particlecoefficient3) {
        this.particlecoefficient3 = particlecoefficient3;
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
        return "Sedimentgrain{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", samplingDepth=" + samplingDepth +
        ", gravel1=" + gravel1 +
        ", gravel2=" + gravel2 +
        ", sand1=" + sand1 +
        ", sand2=" + sand2 +
        ", sand3=" + sand3 +
        ", sand4=" + sand4 +
        ", sand5=" + sand5 +
        ", pinksand1=" + pinksand1 +
        ", pinksand2=" + pinksand2 +
        ", clay1=" + clay1 +
        ", clay2=" + clay2 +
        ", granulecontent1=" + granulecontent1 +
        ", granulecontent2=" + granulecontent2 +
        ", granulecontent3=" + granulecontent3 +
        ", granulecontent4=" + granulecontent4 +
        ", nameAndCode=" + nameAndCode +
        ", particlecoefficient1=" + particlecoefficient1 +
        ", particlecoefficient2=" + particlecoefficient2 +
        ", particlecoefficient3=" + particlecoefficient3 +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
