package com.ltmap.halobiosmaintain.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author fjh
 * @date 2020/12/4 19:49
 * @email 1562106220@qq.com
 */
@Data
public class SedimentgrainReq {
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
     * 采样深度(cm)
     */
    private BigDecimal samplingDepth;

    /**
     * 砾石_>4(mm)
     */
    private BigDecimal gravel1;

    /**
     * 砾石 _4-2(mm)
     */
    private BigDecimal gravel2;

    /**
     * 砂_2-1(mm)
     */
    private BigDecimal sand1;

    /**
     * 砂_1-0.5(mm)
     */
    private BigDecimal sand2;

    /**
     * 砂_0.5-0.25(mm)
     */
    private BigDecimal sand3;

    /**
     * 砂_0.25-0.125(mm)
     */
    private BigDecimal sand4;

    /**
     * 砂_0.125-0.063(mm)
     */
    private BigDecimal sand5;

    /**
     * 粉砂_0.063-0.016(mm)
     */
    private BigDecimal pinksand1;

    /**
     * 粉砂_0.016-0.004(mm)
     */
    private BigDecimal pinksand2;

    /**
     * 粘土_0.004-0.001(mm)
     */
    private BigDecimal clay1;

    /**
     * 粘土_<0.001(mm)
     */
    private BigDecimal clay2;

    /**
     * 粒组含量_砾(%)
     */
    private BigDecimal granulecontent1;

    /**
     * 粒组含量_砂(%)
     */
    private BigDecimal granulecontent2;

    /**
     * 粒组含量_粉砂(%)
     */
    private BigDecimal granulecontent3;

    /**
     * 粒组含量_粘土(%)
     */
    private BigDecimal granulecontent4;

    /**
     * 名称及代号
     */
    private String nameAndCode;

    /**
     * 粒组系数_Mdф
     */
    private BigDecimal particlecoefficient1;

    /**
     * 粒组系数_QDф
     */
    private BigDecimal particlecoefficient2;

    /**
     * 粒组系数_SKф
     */
    private BigDecimal particlecoefficient3;



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