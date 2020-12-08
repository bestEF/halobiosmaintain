package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.IntertidalzonebiologicalQuantitative;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.IIntertidalzonebiologicalQuantitativeService;
import com.ltmap.halobiosmaintain.service.ILargezooplanktonInetService;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 大型浮游动物_I型网_表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "大型浮游动物_I型网")
@RestController
@RequestMapping("/largezooplanktonInet")
public class LargezooplanktonInetController {

    @Resource
    private ILargezooplanktonInetService largezooplanktonInetService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="大型浮游动物_I型网数据查询_数据管理")
    @PostMapping("/listLargezooplanktonInet")
    public Response<IPage<LargezooplanktonInet>> listLargezooplanktonInet(@RequestParam(defaultValue = "1")Integer current,
                                                                          @RequestParam(defaultValue = "10")Integer size,
                                                                          String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<LargezooplanktonInet> intertidalzonebiologicalQuantitatives= largezooplanktonInetService.listLargezooplanktonInet(current,size,stationName,biologicalChineseName,startDate,endDate);
        return Responses.or(intertidalzonebiologicalQuantitatives);
    }



    @ApiOperation(value ="大型浮游动物_I型网数据删除_数据管理")
    @PostMapping("/deleteLargezooplanktonInet")
    public Response<Boolean> deleteLargezooplanktonInet(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<LargezooplanktonInet> largezooplanktonInets = largezooplanktonInetService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= largezooplanktonInetService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < largezooplanktonInets.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", largezooplanktonInets.get(i).getStationId());
            //修改展位数据中的数据类型，删除浮游动物（I型网）
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(largezooplanktonInets.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("浮游动物（I型网）")) {
                        dataTypeNew +=item;
                    }
                }
                dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, largezooplanktonInets.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

