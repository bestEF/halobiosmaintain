package com.ltmap.halobiosmaintain.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author fjh
 * @date 2020/12/4 19:45
 * @email 1562106220@qq.com
 */
@Data
public class BiologicalQualityReq {
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