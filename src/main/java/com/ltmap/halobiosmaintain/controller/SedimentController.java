package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.ISedimentService;
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
 * 沉积物表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "沉积物数据")
@RestController
@RequestMapping("/sediment")
public class SedimentController {

    @Resource
    private ISedimentService sedimentService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="沉积物变化范围")
    @PostMapping("/sedimentRange")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> sedimentstatisticOneYear(String year, String voyage, String element){

        return Responses.or(sedimentService.sedimentstatisticOneYear(year,voyage,element));
    }


    @ApiOperation(value ="沉积物评价标准等级")
    @PostMapping("/sedimentOrder")
    public Response<HashMap<String,HashMap<String,BigDecimal>>> sedimentOrder(String year, String voyage){

        return Responses.or(sedimentService.sedimentOrder(year,voyage));
    }


    @ApiOperation(value ="沉积物监测项目变化_多年数据")
    @PostMapping("/sedimentRangeMultiYear")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> sedimentRangeMultiYear(String startYear,String endYear, String element){

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
            HashMap<String, HashMap<String, BigDecimal>> hashMapHashMap= sedimentService.sedimentstatisticOneYear(yearList.get(i), null, element);
            result.put(yearList.get(i),hashMapHashMap.get("value").get("ave"));
        }
        resultMap.put("value",result);
        return Responses.or(resultMap);
    }


    @ApiOperation(value ="沉积物数据查询_数据管理")
    @PostMapping("/listSediment")
    public Response<IPage<Sediment>> listSediment(@RequestParam(defaultValue = "1")Integer current,
                                                  @RequestParam(defaultValue = "10")Integer size,
                                                  String stationName, String startDate, String endDate,Long reportId){
        IPage<Sediment> sediments= sedimentService.listSediment(current,size,stationName,startDate,endDate,reportId);
        return Responses.or(sediments);
    }


    @ApiOperation(value ="沉积物数据删除_数据管理")
    @PostMapping("/deleteSediment")
    public Response<Boolean> deleteSediment(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<Sediment> sediments = sedimentService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= sedimentService.removeByMap(map);

        try{
        //删除站位数据表
        for (int i = 0; i < sediments.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("station_id", sediments.get(i).getStationId());
            //修改展位数据中的数据类型，删除沉积物
            List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(sediments.get(i).getStationId(),null,null);
            if(monitorStationInfos.size()==1){
                String dataTypeNew="";
                String[] dataType=monitorStationInfos.get(0).getDataType().split(";");
                for (String item:dataType
                ) {
                    if (!item.equals("沉积物")) {
                        dataTypeNew +=item+";";
                    }
                }
                if(!Strings.isNullOrEmpty(dataTypeNew)){
                    dataTypeNew=dataTypeNew.substring(0,dataTypeNew.length()-1);
                }

                LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, sediments.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
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

