package com.ltmap.halobiosmaintain.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author fjh
 * @date 2020/12/4 19:50
 * @email 1562106220@qq.com
 */
@Data
public class WaterqualityReq {
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