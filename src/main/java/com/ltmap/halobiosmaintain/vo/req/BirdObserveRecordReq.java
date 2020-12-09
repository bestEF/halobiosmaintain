package com.ltmap.halobiosmaintain.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author fjh
 * @date 2020/12/8 16:23
 * @email 1562106220@qq.com
 */
@Data
public class BirdObserveRecordReq implements Serializable {
    public static final Long serialVersionUID=1L;

    //鸟类观测记录子表
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



    //鸟类观测记录主表
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
}