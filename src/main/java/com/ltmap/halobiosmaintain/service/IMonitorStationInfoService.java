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

    List<MonitorStationInfo> queryStationInfoById(Long stationId,Long reportId,String stationName);
}
