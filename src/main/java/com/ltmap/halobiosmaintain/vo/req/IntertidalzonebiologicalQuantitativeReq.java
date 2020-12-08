package com.ltmap.halobiosmaintain.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author fjh
 * @date 2020/12/4 19:46
 * @email 1562106220@qq.com
 */
@Data
public class IntertidalzonebiologicalQuantitativeReq {
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
     * 检测日期
     */
    private LocalDate monitorDate;

    /**
     * 站位id
     */
    private Long stationId;

    /**
     * 潮带
     */
    private String intertidalZone;

    /**
     * 底质类型
     */
    private String sedimentType;

    /**
     * 采样次数
     */
    private Integer samplingTimes;

    /**
     * 样方面积(m^2)
     */
    private BigDecimal quadratArea;

    /**
     * 样品厚度(cm)
     */
    private BigDecimal sampleThickness;

    /**
     * 生物种中文学名
     */
    private String biologicalChineseName;

    /**
     * 生物种拉丁名
     */
    private String biologicalLatinName;

    /**
     * 数量(个)
     */
    private Integer count;

    /**
     * 密度(个/m^2)
     */
    private BigDecimal density;

    /**
     * 重量(g)
     */
    private BigDecimal weight;

    /**
     * 生物量(g/m^2)
     */
    private BigDecimal biomass;

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



    //监测站位信息表
//    /**
//     * 站位id
//     */
//    @TableId(value = "station_id", type = IdType.AUTO)
//    private Long stationId;

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
     * 存储数据类型，用分号隔开
     */
    private String dataType;

//    /**
//     * 填报id
//     */
//    private Long reportId;



    //监测数据填报信息表
//    /**
//     * 填报id
//     */
//    @TableId(value = "report_id", type = IdType.AUTO)
//    private Long reportId;

    /**
     * 监控区
     */
    private String monitoringArea;

    /**
     * 生态类型
     */
    private String ecologicalType;

    /**
     * 任务日期
     */
    private LocalDate taskDate;

    /**
     * 监测单位
     */
    private String monitorCompany;

    /**
     * 组织单位
     */
    private String organizationCompany;

    /**
     * 填报日期
     */
    private LocalDate reportDate;

    /**
     * 填报人
     */
    private String reportName;

    /**
     * 校对人
     */
    private String checkName;

    /**
     * 审核人
     */
    private String verifyName;

    /**
     * 年份
     */
    private String year;

    /**
     * 航次
     */
    private String voyage;
}