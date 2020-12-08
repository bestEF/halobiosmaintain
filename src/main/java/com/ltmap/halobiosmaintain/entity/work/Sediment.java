package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 沉积物表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("sediment")
public class Sediment extends Model<Sediment> {

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
     * 采样深度(cm)
     */
    private BigDecimal samplingDepth;

    /**
     * 硫化物(x10-6)
     */
    private BigDecimal lhw;

    /**
     * 有机碳(%)
     */
    private BigDecimal yjt;

    /**
     * 石油类(x10-6)
     */
    private BigDecimal syl;

    /**
     * 总磷(x10-6)
     */
    private BigDecimal tp;

    /**
     * 总氮(x10-6)
     */
    private BigDecimal tn;

    /**
     * 汞(x10-6)
     */
    private BigDecimal hg;

    /**
     * 砷(x10-6)
     */
    private BigDecimal ass;

    /**
     * 锌(x10-6)
     */
    private BigDecimal zn;

    /**
     * 镉(x10-6)
     */
    private BigDecimal cd;

    /**
     * 铅(x10-6)
     */
    private BigDecimal pb;

    /**
     * 铜(x10-6)
     */
    private BigDecimal cu;

    /**
     * 总铬(x10-6)
     */
    private BigDecimal tcr;

    /**
     * Eh
     */
    private BigDecimal eh;

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

    public BigDecimal getLhw() {
        return lhw;
    }

    public void setLhw(BigDecimal lhw) {
        this.lhw = lhw;
    }

    public BigDecimal getYjt() {
        return yjt;
    }

    public void setYjt(BigDecimal yjt) {
        this.yjt = yjt;
    }

    public BigDecimal getSyl() {
        return syl;
    }

    public void setSyl(BigDecimal syl) {
        this.syl = syl;
    }

    public BigDecimal getTp() {
        return tp;
    }

    public void setTp(BigDecimal tp) {
        this.tp = tp;
    }

    public BigDecimal getTn() {
        return tn;
    }

    public void setTn(BigDecimal tn) {
        this.tn = tn;
    }

    public BigDecimal getHg() {
        return hg;
    }

    public void setHg(BigDecimal hg) {
        this.hg = hg;
    }

    public BigDecimal getAss() {
        return ass;
    }

    public void setAss(BigDecimal ass) {
        this.ass = ass;
    }

    public BigDecimal getZn() {
        return zn;
    }

    public void setZn(BigDecimal zn) {
        this.zn = zn;
    }

    public BigDecimal getCd() {
        return cd;
    }

    public void setCd(BigDecimal cd) {
        this.cd = cd;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

    public BigDecimal getCu() {
        return cu;
    }

    public void setCu(BigDecimal cu) {
        this.cu = cu;
    }

    public BigDecimal getTcr() {
        return tcr;
    }

    public void setTcr(BigDecimal tcr) {
        this.tcr = tcr;
    }

    public BigDecimal getEh() {
        return eh;
    }

    public void setEh(BigDecimal eh) {
        this.eh = eh;
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
        return "Sediment{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", waterDepth=" + waterDepth +
        ", samplingLevel=" + samplingLevel +
        ", samplingDepth=" + samplingDepth +
        ", lhw=" + lhw +
        ", yjt=" + yjt +
        ", syl=" + syl +
        ", tp=" + tp +
        ", tn=" + tn +
        ", hg=" + hg +
        ", ass=" + ass +
        ", zn=" + zn +
        ", cd=" + cd +
        ", pb=" + pb +
        ", cu=" + cu +
        ", tcr=" + tcr +
        ", eh=" + eh +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
