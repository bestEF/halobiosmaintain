package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.FisheggQualitative;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.IFisheggQualitativeService;
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
 * 鱼卵定性表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "鱼卵定性")
@RestController
@RequestMapping("/fisheggQualitative")
public class FisheggQualitativeController {

    @Resource
    private IFisheggQualitativeService fisheggQualitativeService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;



    @ApiOperation(value ="鱼卵定性数据查询_数据管理")
    @PostMapping("/listFisheggQualitative")
    public Response<IPage<FisheggQualitative>> listFisheggQualitative(@RequestParam(defaultValue = "1")Integer current,
                                                                      @RequestParam(defaultValue = "10")Integer size,
                                                                      String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<FisheggQualitative> fisheggQualitatives= fisheggQualitativeService.listFisheggQualitative(current,size,stationName,biologicalChineseName,startDate,endDate,reportId);
        return Responses.or(fisheggQualitatives);
    }

    @ApiOperation(value ="鱼卵定性数据删除_数据管理")
    @PostMapping("/deleteFisheggQualitative")
    public Response<Boolean> deleteFisheggQualitative(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<FisheggQualitative> fisheggQualitatives = fisheggQualitativeService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= fisheggQualitativeService.removeByMap(map);

        try{
        //删除站位数据表
        for (int i = 0; i < fisheggQualitatives.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", fisheggQualitatives.get(i).getStationId());
            //修改展位数据中的数据类型，删除鱼卵定性
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(fisheggQualitatives.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("鱼卵定性")) {
                        dataTypeNew +=item+";";
                    }
                }
                if(!Strings.isNullOrEmpty(dataTypeNew)){
                    dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);
                }

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, fisheggQualitatives.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
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

