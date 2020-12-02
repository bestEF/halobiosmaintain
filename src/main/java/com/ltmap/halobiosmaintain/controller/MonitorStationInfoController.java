package com.ltmap.halobiosmaintain.controller;


import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  监测站位信息 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "监测站位信息")
@RestController
@RequestMapping("/monitorStationInfo")
public class MonitorStationInfoController {
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;

    /*
     * @Description:查询所有监测站位
     * @Param
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/28 9:42
     */
    @ApiOperation(value ="查询所有监测站位")
    @PostMapping("/queryAllStationInfo")
    public Response<List<MonitorStationInfo>> queryStationInfo(String year,String voyage){
        List<MonitorStationInfo> monitorStationInfoList=monitorStationInfoService.queryStationInfo(year,voyage);
        return Responses.or(monitorStationInfoList);
    }

    /*
     * @Description:查询监测站位通过站位Id
     * @Param stationId:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/28 11:01
     */
    @ApiOperation(value ="查询监测站位通过站位Id")
    @PostMapping("/queryStationInfoById")
    @ApiImplicitParam(name = "stationId",value = "岸基站Id",required = true)
    public Response<List<MonitorStationInfo>> queryStationInfoById(Long stationId,Long reportId,String stationName){
        List<MonitorStationInfo> monitorStationInfoList=monitorStationInfoService.queryStationInfoById(stationId,reportId,stationName);
        return Responses.or(monitorStationInfoList);
    }
}

