package com.ltmap.halobiosmaintain.entity.work;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 生物质量表
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
@TableName("biological_quality")
public class BiologicalQuality extends Model<BiologicalQuality> {

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
     * 类群
     */
    private String category;

    /**
     * 生物种中文学名
     */
    private String biologicalChineseName;

    /**
     * 生物种拉丁名
     */
    private String biologicalLatinName;

    /**
     * 样品体（壳）长(mm)
     */
    private BigDecimal sampleBodyLength;

    /**
     * 总汞(x10-6)
     */
    private BigDecimal thg;

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
     * 砷(x10-6)
     */
    private BigDecimal ass;

    /**
     * 六六六(x10-6)
     */
    private BigDecimal sixsixsix;

    /**
     * 铬(x10-6)
     */
    private BigDecimal cr;

    /**
     * 滴滴涕(x10-6)
     */
    private BigDecimal ddt;

    /**
     * 多氯联苯(x10-6)
     */
    private BigDecimal pcbs;

    /**
     * 石油烃(x10-6)
     */
    private BigDecimal syt;

    /**
     * 粪大肠菌群(个/g)
     */
    private BigDecimal fdcjq;

    /**
     * 氯霉素(x10-6)
     */
    private BigDecimal lms;

    /**
     * 抗生素(x10-6)
     */
    private BigDecimal kss;

    /**
     * 腹泻性贝毒(MU/100g)
     */
    private BigDecimal dsp;

    /**
     * 麻痹性贝毒(MU/100g)
     */
    private BigDecimal psp;

    /**
     * 锌(x10-6)
     */
    private BigDecimal zn;

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

    public BigDecimal getWaterDepth() {
        return waterDepth;
    }

    public void setWaterDepth(BigDecimal waterDepth) {
        this.waterDepth = waterDepth;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public BigDecimal getSampleBodyLength() {
        return sampleBodyLength;
    }

    public void setSampleBodyLength(BigDecimal sampleBodyLength) {
        this.sampleBodyLength = sampleBodyLength;
    }

    public BigDecimal getThg() {
        return thg;
    }

    public void setThg(BigDecimal thg) {
        this.thg = thg;
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

    public BigDecimal getAss() {
        return ass;
    }

    public void setAss(BigDecimal ass) {
        this.ass = ass;
    }

    public BigDecimal getSixsixsix() {
        return sixsixsix;
    }

    public void setSixsixsix(BigDecimal sixsixsix) {
        this.sixsixsix = sixsixsix;
    }

    public BigDecimal getCr() {
        return cr;
    }

    public void setCr(BigDecimal cr) {
        this.cr = cr;
    }

    public BigDecimal getDdt() {
        return ddt;
    }

    public void setDdt(BigDecimal ddt) {
        this.ddt = ddt;
    }

    public BigDecimal getPcbs() {
        return pcbs;
    }

    public void setPcbs(BigDecimal pcbs) {
        this.pcbs = pcbs;
    }

    public BigDecimal getSyt() {
        return syt;
    }

    public void setSyt(BigDecimal syt) {
        this.syt = syt;
    }

    public BigDecimal getFdcjq() {
        return fdcjq;
    }

    public void setFdcjq(BigDecimal fdcjq) {
        this.fdcjq = fdcjq;
    }

    public BigDecimal getLms() {
        return lms;
    }

    public void setLms(BigDecimal lms) {
        this.lms = lms;
    }

    public BigDecimal getKss() {
        return kss;
    }

    public void setKss(BigDecimal kss) {
        this.kss = kss;
    }

    public BigDecimal getDsp() {
        return dsp;
    }

    public void setDsp(BigDecimal dsp) {
        this.dsp = dsp;
    }

    public BigDecimal getPsp() {
        return psp;
    }

    public void setPsp(BigDecimal psp) {
        this.psp = psp;
    }

    public BigDecimal getZn() {
        return zn;
    }

    public void setZn(BigDecimal zn) {
        this.zn = zn;
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
        return "BiologicalQuality{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", stationId=" + stationId +
        ", monitorDate=" + monitorDate +
        ", waterDepth=" + waterDepth +
        ", category=" + category +
        ", biologicalChineseName=" + biologicalChineseName +
        ", biologicalLatinName=" + biologicalLatinName +
        ", sampleBodyLength=" + sampleBodyLength +
        ", thg=" + thg +
        ", cd=" + cd +
        ", pb=" + pb +
        ", cu=" + cu +
        ", ass=" + ass +
        ", sixsixsix=" + sixsixsix +
        ", cr=" + cr +
        ", ddt=" + ddt +
        ", pcbs=" + pcbs +
        ", syt=" + syt +
        ", fdcjq=" + fdcjq +
        ", lms=" + lms +
        ", kss=" + kss +
        ", dsp=" + dsp +
        ", psp=" + psp +
        ", zn=" + zn +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
