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
 * 植被调查记录子表
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("vegetation_survey_record")
public class VegetationSurveyRecord extends Model<VegetationSurveyRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
      @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 主表id
     */
    private Long id;

    /**
     * 序号
     */
    private String no;

    /**
     * 种名
     */
    private String chineseName;

    /**
     * 拉丁学名
     */
    private String latinName;

    /**
     * 数量
     */
    private BigDecimal number;

    /**
     * 盖度
     */
    private BigDecimal coverage;

    /**
     * 高度
     */
    private String height;

    /**
     * 胸径
     */
    private String chestDiameter;

    /**
     * 受威胁因素
     */
    private String threatenedFactors;

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


    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getCoverage() {
        return coverage;
    }

    public void setCoverage(BigDecimal coverage) {
        this.coverage = coverage;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getChestDiameter() {
        return chestDiameter;
    }

    public void setChestDiameter(String chestDiameter) {
        this.chestDiameter = chestDiameter;
    }

    public String getThreatenedFactors() {
        return threatenedFactors;
    }

    public void setThreatenedFactors(String threatenedFactors) {
        this.threatenedFactors = threatenedFactors;
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
        return this.recordId;
    }

    @Override
    public String toString() {
        return "VegetationSurveyRecord{" +
        "recordId=" + recordId +
        ", id=" + id +
        ", no=" + no +
        ", chineseName=" + chineseName +
        ", latinName=" + latinName +
        ", number=" + number +
        ", coverage=" + coverage +
        ", height=" + height +
        ", chestDiameter=" + chestDiameter +
        ", threatenedFactors=" + threatenedFactors +
        ", remark=" + remark +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
