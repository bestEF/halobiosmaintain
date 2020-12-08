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
 * 鸟类观测记录子表
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
@TableName("bird_observe_record")
public class BirdObserveRecord extends Model<BirdObserveRecord> {

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
     * 中文名
     */
    private String chineseName;

    /**
     * 学名
     */
    private String latinName;

    /**
     * 成体数量
     */
    private BigDecimal adultNumber;

    /**
     * 幼体数量
     */
    private BigDecimal larvaeNumber;

    /**
     * 个体总数
     */
    private BigDecimal totalNumber;

    /**
     * 凭据
     */
    private String credential;

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

    public BigDecimal getAdultNumber() {
        return adultNumber;
    }

    public void setAdultNumber(BigDecimal adultNumber) {
        this.adultNumber = adultNumber;
    }

    public BigDecimal getLarvaeNumber() {
        return larvaeNumber;
    }

    public void setLarvaeNumber(BigDecimal larvaeNumber) {
        this.larvaeNumber = larvaeNumber;
    }

    public BigDecimal getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(BigDecimal totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
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
        return "BirdObserveRecord{" +
        "recordId=" + recordId +
        ", id=" + id +
        ", no=" + no +
        ", chineseName=" + chineseName +
        ", latinName=" + latinName +
        ", adultNumber=" + adultNumber +
        ", larvaeNumber=" + larvaeNumber +
        ", totalNumber=" + totalNumber +
        ", credential=" + credential +
        ", remark=" + remark +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
