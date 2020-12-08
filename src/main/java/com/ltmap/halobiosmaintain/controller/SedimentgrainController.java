package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.Sedimentgrain;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.ISedimentgrainService;
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
 * 沉积物粒度表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "沉积物粒度")
@RestController
@RequestMapping("/sedimentgrain")
public class SedimentgrainController {

    @Resource
    private ISedimentgrainService sedimentgrainService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="沉积物粒度数据查询_数据管理")
    @PostMapping("/listSediment")
    public Response<IPage<Sedimentgrain>> listSedimentgrain(@RequestParam(defaultValue = "1")Integer current,
                                                            @RequestParam(defaultValue = "10")Integer size,
                                                            String stationName, String startDate, String endDate){
        IPage<Sedimentgrain> sediments= sedimentgrainService.listSedimentgrain(current,size,stationName,startDate,endDate);
        return Responses.or(sediments);
    }


    @ApiOperation(value ="沉积物粒度数据删除_数据管理")
    @PostMapping("/deleteSedimentgrain")
    public Response<Boolean> deleteSedimentgrain(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<Sedimentgrain> sedimentgrains = sedimentgrainService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= sedimentgrainService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < sedimentgrains.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", sedimentgrains.get(i).getStationId());
            //修改展位数据中的数据类型，删除沉积物粒度
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(sedimentgrains.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("沉积物粒度")) {
                        dataTypeNew +=item;
                    }
                }
                dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, sedimentgrains.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }

            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

