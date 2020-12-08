package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.entity.work.BirdObserveRecord;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurvey;
import com.ltmap.halobiosmaintain.service.IBirdObserveRecordService;
import com.ltmap.halobiosmaintain.service.IBirdObserveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 * 鸟类观测记录主表 前端控制器
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Api(tags = "鸟类观测记录")
@RestController
@RequestMapping("/birdObserve")
public class BirdObserveController {

    @Resource
    private IBirdObserveService birdObserveService;
    @Resource
    private IBirdObserveRecordService birdObserveRecordService;



    @ApiOperation(value ="鸟类观测记录主表查询_数据管理")
    @PostMapping("/listBirdObserve")
    public Response<IPage<BirdObserve>> listBirdObserve(@RequestParam(defaultValue = "1")Integer current,
                                                              @RequestParam(defaultValue = "10")Integer size,
                                                              String protecName, String place, String startDate, String endDate){
        IPage<BirdObserve> birdObserveIPage= birdObserveService.listBirdObserve(current,size,protecName,place,startDate,endDate);
        return Responses.or(birdObserveIPage);
    }

    @ApiOperation(value ="鸟类观测记录子表查询_数据管理")
    @PostMapping("/listBirdObserveRecord")
    public Response<IPage<BirdObserveRecord>> listBirdObserveRecord(@RequestParam(defaultValue = "1")Integer current,
                                                              @RequestParam(defaultValue = "10")Integer size,
                                                              String chineseName, Long id){
        IPage<BirdObserveRecord> birdObserveRecordIPage= birdObserveRecordService.listBirdObserveRecord(current,size,chineseName,id);
        return Responses.or(birdObserveRecordIPage);
    }

    @ApiOperation(value ="鸟类观测记录主表删除_数据管理")
    @PostMapping("/deleteBirdObserveRecord")
    public Response<Boolean> deleteBirdObserveRecord(Long id){
        HashMap deleteMap=new HashMap();
        deleteMap.put("id",id);
        //删除子表
        birdObserveRecordService.removeByMap(deleteMap);
        //删除主表
        Boolean deleted = birdObserveService.removeById(id);
        return Responses.or(deleted);
    }

}

