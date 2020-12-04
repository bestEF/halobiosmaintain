package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.service.IMacrobenthosQualitativeService;
import com.ltmap.halobiosmaintain.service.IMacrobenthosQuantitativeService;
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
 * 大型底栖动物定量表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "大型底栖动物定量")
@RestController
@RequestMapping("/macrobenthosQuantitative")
public class MacrobenthosQuantitativeController {

    @Resource
    private IMacrobenthosQuantitativeService macrobenthosQuantitativeService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;


    @ApiOperation(value ="大型底栖动物定量数据查询_数据管理")
    @PostMapping("/listMacrobenthosQuantitative")
    public Response<IPage<MacrobenthosQuantitative>> listMacrobenthosQuantitative(@RequestParam(defaultValue = "1")Integer current,
                                                                                  @RequestParam(defaultValue = "10")Integer size,
                                                                                  String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<MacrobenthosQuantitative> macrobenthosQuantitatives= macrobenthosQuantitativeService.listMacrobenthosQuantitative(current,size,stationName,biologicalChineseName,startDate,endDate);
        return Responses.or(macrobenthosQuantitatives);
    }


    @ApiOperation(value ="大型底栖动物定量数据删除_数据管理")
    @PostMapping("/deleteMacrobenthosQuantitative")
    public Response<Boolean> deleteMacrobenthosQuantitative(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<MacrobenthosQuantitative> macrobenthosQuantitatives = macrobenthosQuantitativeService.listByMap(map);

        //删除生物质量数据表
        Boolean deleted= macrobenthosQuantitativeService.removeByMap(map);

        //删除站位数据表
        for (int i = 0; i < macrobenthosQuantitatives.size(); i++) {
            HashMap<String, Object> map2 = new HashMap<>();
            map.put("station_id", macrobenthosQuantitatives.get(i).getStationId());
            monitorStationInfoService.removeByMap(map2);
        }

        //删除填报数据
        monitorDataReportService.removeById(reportId);

        return Responses.or(deleted);
    }
}

