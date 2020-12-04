package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.service.IBiologicalQualityService;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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
 * 生物质量表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "生物质量")
@RestController
@RequestMapping("/biologicalQuality")
public class BiologicalQualityController {

    @Resource
    private IBiologicalQualityService biologicalQualityService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;

    @ApiOperation(value ="生物质量变化范围")
    @PostMapping("/biologicalQualityRange")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> biologicalQualityRangeOneYear(String year, String voyage, String element){

        return Responses.or(biologicalQualityService.biologicalQualityRangeOneYear(year,voyage,element));
    }

    @ApiOperation(value ="生物质量监测项目变化_多年数据")
    @PostMapping("/biologicalQualityRangeMultiYear")
    public Response<HashMap<String, HashMap<String, BigDecimal>>> biologicalQualityRangeMultiYear(String startYear,String endYear, String element){

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
            HashMap<String, HashMap<String, BigDecimal>> hashMapHashMap= biologicalQualityService.biologicalQualityRangeOneYear(yearList.get(i), null, element);
            result.put(yearList.get(i),hashMapHashMap.get("value").get("ave"));
        }
        resultMap.put("value",result);
        return Responses.or(resultMap);
    }

    @ApiOperation(value ="生物质量数据查询_数据管理")
    @PostMapping("/listBiologicalQuality")
    public Response<IPage<BiologicalQuality>> listBiologicalQuality(@RequestParam(defaultValue = "1")Integer current,
                                                                   @RequestParam(defaultValue = "10")Integer size,
                                                                   String stationName,String biologicalChineseName,String startDate,String endDate){
        IPage<BiologicalQuality> biologicalQualities= biologicalQualityService.listBiologicalQuality(current,size,stationName,biologicalChineseName,startDate,endDate);
        return Responses.or(biologicalQualities);
    }

    @ApiOperation(value ="生物质量数据删除_数据管理")
    @PostMapping("/deleteBiologicalQuality")
    public Response<Boolean> deleteBiologicalQuality(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<BiologicalQuality> biologicalQualities = biologicalQualityService.listByMap(map);

        //删除生物质量数据表
       Boolean deleted= biologicalQualityService.removeByMap(map);

       //删除站位数据表
        for (int i = 0; i < biologicalQualities.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map.put("station_id", biologicalQualities.get(i).getStationId());
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

