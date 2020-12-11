package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.FisheggQualitative;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.IFisheggQualitativeService;
import com.ltmap.halobiosmaintain.service.IFisheggQuantitativeService;
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
 * 鱼卵定量表 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "鱼卵定量")
@RestController
@RequestMapping("/fisheggQuantitative")
public class FisheggQuantitativeController {

    @Resource
    private IFisheggQuantitativeService fisheggQuantitativeService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;



    @ApiOperation(value ="鱼卵定量数据查询_数据管理")
    @PostMapping("/listFisheggQuantitative")
    public Response<IPage<FisheggQuantitative>> listFisheggQuantitative(@RequestParam(defaultValue = "1")Integer current,
                                                                        @RequestParam(defaultValue = "10")Integer size,
                                                                        String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<FisheggQuantitative> fisheggQualitatives= fisheggQuantitativeService.listFisheggQuantitative(current,size,stationName,biologicalChineseName,startDate,endDate,reportId);
        return Responses.or(fisheggQualitatives);
    }

    @ApiOperation(value ="鱼卵定量数据删除_数据管理")
    @PostMapping("/deleteFisheggQuantitative")
    public Response<Boolean> deleteFisheggQuantitative(String reportId){

        HashMap<String, Object> map = new HashMap<>();
        map.put("report_id", reportId);
        //查询站位id
        List<FisheggQuantitative> fisheggQuantitatives = fisheggQuantitativeService.listByMap(map);

        //删除鱼卵定量数据表
        Boolean deleted= fisheggQuantitativeService.removeByMap(map);

        try {
            //删除站位数据表
            for (int i = 0; i < fisheggQuantitatives.size(); i++) {
                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("station_id", fisheggQuantitatives.get(i).getStationId());
                //修改展位数据中的数据类型，删除鱼卵定量
                List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfoById(fisheggQuantitatives.get(i).getStationId(), null, null);
                if (monitorStationInfos.size() == 1) {
                    String dataTypeNew = "";
                    String[] dataType = monitorStationInfos.get(0).getDataType().split(";");
                    for (String item : dataType
                    ) {
                        if (!item.equals("鱼卵定量")) {
                            dataTypeNew += item + ";";
                        }
                    }
                    if (!Strings.isNullOrEmpty(dataTypeNew)) {
                        dataTypeNew = dataTypeNew.substring(0, dataTypeNew.length() - 1);
                    }

                    LambdaUpdateWrapper<MonitorStationInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorStationInfo::getStationId, fisheggQuantitatives.get(i).getStationId()).set(MonitorStationInfo::getDataType, dataTypeNew);
                    monitorStationInfoService.update(null, lambdaUpdateWrapper);
                }
                Boolean deleted2=  monitorStationInfoService.removeByMap(map2);
            }

            //删除填报数据
            Boolean deleted3=  monitorDataReportService.removeById(reportId);
            return Responses.or(deleted);
        }
        catch (Exception e){
            return Responses.or(deleted);
        }

    }
}

