package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.entity.work.BirdObserveRecord;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurvey;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurveyRecord;
import com.ltmap.halobiosmaintain.service.IBirdObserveRecordService;
import com.ltmap.halobiosmaintain.service.IBirdObserveService;
import com.ltmap.halobiosmaintain.service.IVegetationSurveyRecordService;
import com.ltmap.halobiosmaintain.service.IVegetationSurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 植被调查记录主表 前端控制器
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Api(tags = "植被调查记录")
@RestController
@RequestMapping("/vegetationSurvey")
public class VegetationSurveyController {

    @Resource
    private IVegetationSurveyService vegetationSurveyService;
    @Resource
    private IVegetationSurveyRecordService vegetationSurveyRecordService;



    @ApiOperation(value ="植被调查记录主表查询_数据管理")
    @PostMapping("/listVegetationSurvey")
    public Response<IPage<VegetationSurvey>> listBirdObserve(@RequestParam(defaultValue = "1")Integer current,
                                                             @RequestParam(defaultValue = "10")Integer size,
                                                             String protecName, String place, String startDate, String endDate){
        IPage<VegetationSurvey> vegetationSurveyIPage= vegetationSurveyService.listVegetationSurvey(current,size,protecName,place,startDate,endDate);
        return Responses.or(vegetationSurveyIPage);
    }

    @ApiOperation(value ="植被调查记录子表查询_数据管理")
    @PostMapping("/listVegetationSurveyRecord")
    public Response<IPage<VegetationSurveyRecord>> listVegetationSurveyRecord(@RequestParam(defaultValue = "1")Integer current,
                                                                              @RequestParam(defaultValue = "10")Integer size,
                                                                              String chineseName, Long id){
        IPage<VegetationSurveyRecord> vegetationSurveyRecordIPage= vegetationSurveyRecordService.listVegetationSurveyRecord(current,size,chineseName,id);
        return Responses.or(vegetationSurveyRecordIPage);
    }
}

