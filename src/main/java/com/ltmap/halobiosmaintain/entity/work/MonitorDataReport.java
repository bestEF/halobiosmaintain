package com.ltmap.halobiosmaintain.entity.work;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@TableName("monitor_data_report")
public class MonitorDataReport extends Model<MonitorDataReport> {

    private static final long serialVersionUID = 1L;

    /**
     * 填报id
     */
      @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

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


    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getMonitoringArea() {
        return monitoringArea;
    }

    public void setMonitoringArea(String monitoringArea) {
        this.monitoringArea = monitoringArea;
    }

    public String getEcologicalType() {
        return ecologicalType;
    }

    public void setEcologicalType(String ecologicalType) {
        this.ecologicalType = ecologicalType;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public String getMonitorCompany() {
        return monitorCompany;
    }

    public void setMonitorCompany(String monitorCompany) {
        this.monitorCompany = monitorCompany;
    }

    public String getOrganizationCompany() {
        return organizationCompany;
    }

    public void setOrganizationCompany(String organizationCompany) {
        this.organizationCompany = organizationCompany;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
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
        return this.reportId;
    }

    @Override
    public String toString() {
        return "MonitorDataReport{" +
        "reportId=" + reportId +
        ", monitoringArea=" + monitoringArea +
        ", ecologicalType=" + ecologicalType +
        ", taskDate=" + taskDate +
        ", monitorCompany=" + monitorCompany +
        ", organizationCompany=" + organizationCompany +
        ", reportDate=" + reportDate +
        ", reportName=" + reportName +
        ", checkName=" + checkName +
        ", verifyName=" + verifyName +
        ", year=" + year +
        ", voyage=" + voyage +
        ", byzd1=" + byzd1 +
        ", byzd2=" + byzd2 +
        ", byzd3=" + byzd3 +
        "}";
    }
}
