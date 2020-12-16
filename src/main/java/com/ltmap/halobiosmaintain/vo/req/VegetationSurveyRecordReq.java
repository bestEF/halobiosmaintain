package com.ltmap.halobiosmaintain.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author fjh
 * @date 2020/12/8 16:27
 * @email 1562106220@qq.com
 */
@Data
public class VegetationSurveyRecordReq implements Serializable {
    public static final Long serialVersionUID=1L;

    //植被调查记录子表
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
    private String threatenedFactors2;

    /**
     * 备注
     */
    private String remark;



    //植被调查记录主表
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
    private String area;

    /**
     * 海拔
     */
    private String high;

    /**
     * 经度
     */
    private String lon;

    /**
     * 纬度
     */
    private String lat;

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
}