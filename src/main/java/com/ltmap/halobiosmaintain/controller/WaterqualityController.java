package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.IWaterqualityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 水质表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "水质信息")
@RestController
@RequestMapping("/waterquality")
public class WaterqualityController {

    @Resource
    private IWaterqualityService waterqualityService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;



    @ApiOperation(value ="水质变化范围_一年内")
    @PostMapping("/waterQualityRange")
    public Response<HashMap<String,HashMap<String,BigDecimal>>> waterQualitystatisticOneYear(String year, String voyage, String element){

       return Responses.or(waterqualityService.waterQualitystatisticOneYear(year,voyage,element));
    }

    @ApiOperation(value ="水质评价标准等级_一年内")
    @PostMapping("/waterQualityOrder")
    public Response<HashMap<String,HashMap<String,BigDecimal>>> waterQualityOrder(String year, String voyage){

        return Responses.or(waterqualityService.waterQualityOrder(year,voyage));
    }

    @ApiOperation(value ="水质监测项目变化_多年数据")
    @PostMapping("/waterQualityRangeMultiYear")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> waterQualityRangeMultiYear(String startYear,String endYear, String element){

        BigDecimal interval=new BigDecimal(0);
        BigDecimal srartYearDec=new BigDecimal(startYear);
        BigDecimal endYearDec=new BigDecimal(endYear);
        interval = endYearDec.subtract(srartYearDec);
        List<String> yearList=new ArrayList<>();
        for (int i = 0; i < interval.intValue()+1; i++) {
            yearList.add(srartYearDec.add(new BigDecimal(i)).toString());
        }

        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        HashMap<String,BigDecimal> result=new HashMap<String, BigDecimal>();
        for (int i = 0; i < yearList.size(); i++) {
            HashMap<String, HashMap<String, BigDecimal>> hashMapHashMap= waterqualityService.waterQualitystatisticOneYear(yearList.get(i), null, element);
            result.put(yearList.get(i),hashMapHashMap.get("value").get("ave"));
        }
        resultMap.put("value",result);
        return Responses.or(resultMap);
    }

    @ApiOperation(value ="水质数据查询_数据管理")
    @PostMapping("/listWaterquality")
    public Response<IPage<Waterquality>> listWaterquality(@RequestParam(defaultValue = "1")Integer current,
                                                          @RequestParam(defaultValue = "10")Integer size,
                                                          String stationName, String startDate, String endDate,Long reportId){
        IPage<Waterquality> waterqualities= waterqualityService.listWaterquality(current,size,stationName,startDate,endDate,reportId);
        return Responses.or(waterqualities);
    }


    @ApiOperation(value ="水质数据删除_数据管理")
    @PostMapping("/deleteWaterquality")
    public Response<Boolean> deleteWaterquality(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<Waterquality> waterqualities = waterqualityService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= waterqualityService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < waterqualities.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", waterqualities.get(i).getStationId());
            //修改展位数据中的数据类型，删除水质
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(waterqualities.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("水质")) {
                        dataTypeNew +=item+";";
                    }
                }
                if(!Strings.isNullOrEmpty(dataTypeNew)){
                    dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);
                }

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, waterqualities.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

