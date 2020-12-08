package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQualitative;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.ISmallfishQualitativeService;
import com.ltmap.halobiosmaintain.service.ISmallfishQuantitativeService;
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
 * 仔鱼定量表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "仔鱼定量")
@RestController
@RequestMapping("/smallfishQuantitative")
public class SmallfishQuantitativeController {

    @Resource
    private ISmallfishQuantitativeService smallfishQuantitativeService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="仔鱼定量数据查询_数据管理")
    @PostMapping("/listSmallfishQualitative")
    public Response<IPage<SmallfishQuantitative>> listSmallfishQuantitative(@RequestParam(defaultValue = "1")Integer current,
                                                                            @RequestParam(defaultValue = "10")Integer size,
                                                                            String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<SmallfishQuantitative> smallfishQuantitatives= smallfishQuantitativeService.listSmallfishQuantitative(current,size,stationName,biologicalChineseName,startDate,endDate);
        return Responses.or(smallfishQuantitatives);
    }


    @ApiOperation(value ="仔鱼定量数据删除_数据管理")
    @PostMapping("/deleteSmallfishQuantitative")
    public Response<Boolean> deleteSmallfishQuantitative(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<SmallfishQuantitative> smallfishQuantitatives = smallfishQuantitativeService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= smallfishQuantitativeService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < smallfishQuantitatives.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", smallfishQuantitatives.get(i).getStationId());
            //修改展位数据中的数据类型，删除仔鱼定量
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(smallfishQuantitatives.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("仔鱼定量")) {
                        dataTypeNew +=item;
                    }
                }
                if(!Strings.isNullOrEmpty(dataTypeNew)){
                    dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);
                }

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, smallfishQuantitatives.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

