package com.ltmap.halobiosmaintain.controller;


import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.service.IWaterqualityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
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

    @ApiOperation(value ="水质变化范围")
    @PostMapping("/waterQualityRange")
    public Response<HashMap<String,HashMap<String,BigDecimal>>> waterQualitystatisticOneYear(String year, String voyage, String element){

       return Responses.or(waterqualityService.waterQualitystatisticOneYear(year,voyage,element));
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
}

