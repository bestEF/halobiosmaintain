package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.entity.work.SmallzooplanktonIinet;
import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.ISmallzooplanktonIinetService;
import com.ltmap.halobiosmaintain.service.ISwimminganimalIdentificationService;
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
 * 游泳动物生物鉴定表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "游泳动物")
@RestController
@RequestMapping("/swimminganimalIdentification")
public class SwimminganimalIdentificationController {

    @Resource
    private ISwimminganimalIdentificationService swimminganimalIdentificationService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="游泳动物数据查询_数据管理")
    @PostMapping("/listSwimminganimalIdentification")
    public Response<IPage<SwimminganimalIdentification>> listSwimminganimalIdentification(@RequestParam(defaultValue = "1")Integer current,
                                                                                   @RequestParam(defaultValue = "10")Integer size,
                                                                                   String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<SwimminganimalIdentification> swimminganimalIdentificationIPage= swimminganimalIdentificationService.listSwimminganimalIdentification(current,size,stationName,biologicalChineseName,startDate,endDate, reportId);
        return Responses.or(swimminganimalIdentificationIPage);
    }


    @ApiOperation(value ="游泳动物数据删除_数据管理")
    @PostMapping("/deleteSwimminganimalIdentification")
    public Response<Boolean> deleteSwimminganimalIdentification(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<SwimminganimalIdentification> swimminganimalIdentifications = swimminganimalIdentificationService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= swimminganimalIdentificationService.removeByMap(map);

        try{
        //删除站位数据表
        for (int i = 0; i < swimminganimalIdentifications.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", swimminganimalIdentifications.get(i).getStationId());
            //修改展位数据中的数据类型，删除游泳动物
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(swimminganimalIdentifications.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("游泳动物")) {
                        dataTypeNew +=item+";";
                    }
                }
                if(!Strings.isNullOrEmpty(dataTypeNew)){
                    dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);
                }

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, swimminganimalIdentifications.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
        }
        catch (Exception e){
            return Responses.or(deleted);
        }
    }
}

