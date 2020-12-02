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
 * 水质表
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("waterquality")
public class Waterquality extends Model<Waterquality> {

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
     * 溶解氧(mg/L)
     */
    private BigDecimal dorjy;

    /**
     * 化学需氧量(mg/L)
     */
    private BigDecimal cod;

    /**
     * 盐度
     */
    private BigDecimal yd;

    /**
     * 悬浮物(mg/L)
     */
    private BigDecimal xfw;

    /**
     * 活性磷酸盐(mg/L)
     */
    private BigDecimal hxlsy;

    /**
     * 亚硝酸盐-氮(mg/L)
     */
    private BigDecimal yxsyD;

    /**
     * 硝酸盐-氮(mg/L)
     */
    private BigDecimal xsyD;

    /**
     * 氨-氮(mg/L)
     */
    private BigDecimal aD;

    /**
     * 硅酸盐(mg/L)
     */
    private BigDecimal gsy;

    /**
     * 叶绿素-a(μg/L)
     */
    private BigDecimal ylsA;

    /**
     * 石油类(mg/L)
     */
    private BigDecimal syl;

    /**
     * pH
     */
    private BigDecimal ph;

    /**
     * 有机磷(mg/L)
     */
    private BigDecimal yjl;

    /**
     * 总有机碳(mg/L)
     */
    private BigDecimal toc;

    /**
     * 总磷(mg/L)
     */
    private BigDecimal tp;

    /**
     * 总氮(mg/L)
     */
    private BigDecimal tn;

    /**
     * 砷(mg/L)
     */
    private BigDecimal ass;

    /**
     * 汞(mg/L)
     */
    private BigDecimal hg;

    /**
     * 铜(mg/L)
     */
    private BigDecimal cu;

    /**
     * 铅(mg/L)
     */
    private BigDecimal pb;

    /**
     * 镉(mg/L)
     */
    private BigDecimal cd;

    /**
     * 锌(mg/L)
     */
    private BigDecimal zn;

    /**
     * 铬(mg/L)
     */
    private BigDecimal cr;

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

    public BigDecimal getDorjy() {
        return dorjy;
    }

    public void setDorjy(BigDecimal dorjy) {
        this.dorjy = dorjy;
    }

    public BigDecimal getCod() {
        return cod;
    }

    public void setCod(BigDecimal cod) {
        this.cod = cod;
    }

    public BigDecimal getYd() {
        return yd;
    }

    public void setYd(BigDecimal yd) {
        this.yd = yd;
    }

    public BigDecimal getXfw() {
        return xfw;
    }

    public void setXfw(BigDecimal xfw) {
        this.xfw = xfw;
    }

    public BigDecimal getHxlsy() {
        return hxlsy;
    }

    public void setHxlsy(BigDecimal hxlsy) {
        this.hxlsy = hxlsy;
    }

    public BigDecimal getYxsyD() {
        return yxsyD;
    }

    public void setYxsyD(BigDecimal yxsyD) {
        this.yxsyD = yxsyD;
    }

    public BigDecimal getXsyD() {
        return xsyD;
    }

    public void setXsyD(BigDecimal xsyD) {
        this.xsyD = xsyD;
    }

    public BigDecimal getaD() {
        return aD;
    }

    public void setaD(BigDecimal aD) {
        this.aD = aD;
    }

    public BigDecimal getGsy() {
        return gsy;
    }

    public void setGsy(BigDecimal gsy) {
        this.gsy = gsy;
    }

    public BigDecimal getYlsA() {
        return ylsA;
    }

    public void setYlsA(BigDecimal ylsA) {
        this.ylsA = ylsA;
    }

    public BigDecimal getSyl() {
        return syl;
    }

    public void setSyl(BigDecimal syl) {
        this.syl = syl;
    }

    public BigDecimal getPh() {
        return ph;
    }

    public void setPh(BigDecimal ph) {
        this.ph = ph;
    }

    public BigDecimal getYjl() {
        return yjl;
    }

    public void setYjl(BigDecimal yjl) {
        this.yjl = yjl;
    }

    public BigDecimal getToc() {
        return toc;
    }

    public void setToc(BigDecimal toc) {
        this.toc = toc;
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

    public BigDecimal getAss() {
        return ass;
    }

    public void setAss(BigDecimal ass) {
        this.ass = ass;
    }

    public BigDecimal getHg() {
        return hg;
    }

    public void setHg(BigDecimal hg) {
        this.hg = hg;
    }

    public BigDecimal getCu() {
        return cu;
    }

    public void setCu(BigDecimal cu) {
        this.cu = cu;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

    public BigDecimal getCd() {
        return cd;
    }

    public void setCd(BigDecimal cd) {
        this.cd = cd;
    }

    public BigDecimal getZn() {
        return zn;
    }

    public void setZn(BigDecimal zn) {
        this.zn = zn;
    }

    public BigDecimal getCr() {
        return cr;
    }

    public void setCr(BigDecimal cr) {
        this.cr = cr;
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
        return "Waterquality{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", waterDepth=" + waterDepth +
        ", samplingLevel=" + samplingLevel +
        ", samplingDepth=" + samplingDepth +
        ", dorjy=" + dorjy +
        ", cod=" + cod +
        ", yd=" + yd +
        ", xfw=" + xfw +
        ", hxlsy=" + hxlsy +
        ", yxsyD=" + yxsyD +
        ", xsyD=" + xsyD +
        ", aD=" + aD +
        ", gsy=" + gsy +
        ", ylsA=" + ylsA +
        ", syl=" + syl +
        ", ph=" + ph +
        ", yjl=" + yjl +
        ", toc=" + toc +
        ", tp=" + tp +
        ", tn=" + tn +
        ", ass=" + ass +
        ", hg=" + hg +
        ", cu=" + cu +
        ", pb=" + pb +
        ", cd=" + cd +
        ", zn=" + zn +
        ", cr=" + cr +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
