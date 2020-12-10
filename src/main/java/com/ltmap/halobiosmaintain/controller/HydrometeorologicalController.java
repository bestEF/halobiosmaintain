package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.entity.work.Hydrometeorological;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.IHydrometeorologicalService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 水文气象表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "水文气象")
@RestController
@RequestMapping("/hydrometeorological")
public class HydrometeorologicalController {

    @Resource
    private IHydrometeorologicalService hydrometeorologicalService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="水文气象变化范围_一年内")
    @PostMapping("/hydrometeorologicalRangeOneYear")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> hydrometeorologicalRangeOneYear(String year, String voyage, String element){

        return Responses.or(hydrometeorologicalService.hydrometeorologicalRangeOneYear(year,voyage,element));
    }

    @ApiOperation(value ="水文气象监测项目变化_多年数据")
    @PostMapping("/hydrometeorologicalRangeMultiYear")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> hydrometeorologicalRangeMultiYear(String startYear,String endYear, String element){

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
            HashMap<String, HashMap<String, BigDecimal>> hashMapHashMap= hydrometeorologicalService.hydrometeorologicalRangeOneYear(yearList.get(i), null, element);
            result.put(yearList.get(i),hashMapHashMap.get("value").get("ave"));
        }
        resultMap.put("value",result);
        return Responses.or(resultMap);
    }

    @ApiOperation(value ="水文气象数据查询_数据管理")
    @PostMapping("/listHydrometeorological")
    public Response<IPage<Hydrometeorological>> listHydrometeorological(@RequestParam(defaultValue = "1")Integer current,
                                                                        @RequestParam(defaultValue = "10")Integer size,
                                                                        String stationName, String startDate, String endDate,Long reportId){
        IPage<Hydrometeorological> hydrometeorologicals= hydrometeorologicalService.listHydrometeorological(current,size,stationName,startDate,endDate,reportId);
        return Responses.or(hydrometeorologicals);
    }


    @ApiOperation(value ="水文气象数据删除_数据管理")
    @PostMapping("/deleteHydrometeorological")
    public Response<Boolean> deleteHydrometeorological(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<Hydrometeorological> hydrometeorologicals = hydrometeorologicalService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= hydrometeorologicalService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < hydrometeorologicals.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", hydrometeorologicals.get(i).getStationId());
            //修改展位数据中的数据类型，删除水文气象
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(hydrometeorologicals.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("水文气象")) {
                        dataTypeNew +=item;
                    }
                }
                if(!Strings.isNullOrEmpty(dataTypeNew)){
                    dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);
                }

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, hydrometeorologicals.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                monitorStationInfoService.update(null,lambdaUpdateWrapper);
            }
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

