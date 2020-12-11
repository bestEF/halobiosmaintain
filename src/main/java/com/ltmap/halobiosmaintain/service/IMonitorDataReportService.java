package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.MonitorDataReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IMonitorDataReportService extends IService<MonitorDataReport> {

    /**
     * 根据条件更新（即删除）这些条件信息下相同的数据
     * @param monitorDataReport
     * @param fisheggQuantitativeType
     * @return
     */
    boolean updateData(MonitorDataReport monitorDataReport, int fisheggQuantitativeType,String dataType);

    List<MonitorDataReport> monitorDataReportInfo(String monitoringArea,String ecologicalType,String monitorCompany,String startDate,String endDate,String year,String voyage);

    /**
     * 保存填报信息
     * @param monitorDataReport0
     * @return
     */
    Long addMonitorDataReport(MonitorDataReport monitorDataReport0);
}
