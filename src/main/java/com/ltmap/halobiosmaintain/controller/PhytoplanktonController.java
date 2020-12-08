package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.ltmap.halobiosmaintain.service.IMacrobenthosQuantitativeService;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.IPhytoplanktonService;
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
 * 浮游植物表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "浮游植物")
@RestController
@RequestMapping("/phytoplankton")
public class PhytoplanktonController {

    @Resource
    private IPhytoplanktonService phytoplanktonService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="浮游植物数据查询_数据管理")
    @PostMapping("/listPhytoplankton")
    public Response<IPage<Phytoplankton>> listPhytoplankton(@RequestParam(defaultValue = "1")Integer current,
                                                            @RequestParam(defaultValue = "10")Integer size,
                                                            String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<Phytoplankton> phytoplanktons= phytoplanktonService.listPhytoplankton(current,size,stationName,biologicalChineseName,startDate,endDate);
        return Responses.or(phytoplanktons);
    }


    @ApiOperation(value ="浮游植物数据删除_数据管理")
    @PostMapping("/deletePhytoplankton")
    public Response<Boolean> deletePhytoplankton(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<Phytoplankton> phytoplanktons = phytoplanktonService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= phytoplanktonService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < phytoplanktons.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", phytoplanktons.get(i).getStationId());
            //修改展位数据中的数据类型，删除浮游植物
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(phytoplanktons.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("浮游植物")) {
                        dataTypeNew +=item;
                    }
                }
                dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, phytoplanktons.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }


}

