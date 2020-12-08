package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IMonitorStationInfoService extends IService<MonitorStationInfo> {


    /*
     * @Description:查询所有监测站位
     * @Param
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/28 9:50
     */
    List<MonitorStationInfo> queryStationInfo(String year,String voyage);

    List<MonitorStationInfo> queryStationInfobyDataType(String year,String voyage,String dataType);

    List<MonitorStationInfo> queryStationInfoById(Long stationId,Long reportId,String stationName);

    /**
     * 根据填报id删除所有站位信息
     * @param reportId
     * @return
     */
    Boolean deleteByReportId(Long reportId,String dataType);

    /**
     * 保存站位信息
     * @param monitorStationInfo
     * @return
     */
    Long addMonitorStation(MonitorStationInfo monitorStationInfo);
}
