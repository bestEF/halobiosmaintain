package com.ltmap.halobiosmaintain.controller;


import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  监测站位信息 前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Api(tags = "监测站位信息")
@RestController
@RequestMapping("/monitorStationInfo")
public class MonitorStationInfoController {
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IMacrobenthosQuantitativeService macrobenthosQuantitativeService;
    @Resource
    private IMacrobenthosQualitativeService macrobenthosQualitativeService;
    @Resource
    private IFisheggQuantitativeService fisheggQuantitativeService;
    @Resource
    private IFisheggQualitativeService fisheggQualitativeService;
    @Resource
    private ISmallfishQuantitativeService smallfishQuantitativeService;
    @Resource
    private ISmallfishQualitativeService smallfishQualitativeService;
    @Resource
    private ISwimminganimalIdentificationService swimminganimalIdentificationService;
    @Resource
    private IIntertidalzonebiologicalQuantitativeService intertidalzonebiologicalQuantitativeService;
    @Resource
    private IPhytoplanktonService phytoplanktonService;
    @Resource
    private ILargezooplanktonInetService largezooplanktonInetService;
    @Resource
    private ISmallzooplanktonIinetService smallzooplanktonIinetService;

    /*
     * @Description:查询所有监测站位
     * @Param
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/28 9:42
     */
    @ApiOperation(value ="查询所有监测站位")
    @PostMapping("/queryAllStationInfo")
    public Response<List<MonitorStationInfo>> queryStationInfo(String year,String voyage){
        List<MonitorStationInfo> monitorStationInfoList=monitorStationInfoService.queryStationInfo(year,voyage);
        return Responses.or(monitorStationInfoList);
    }

    /*
     * @Description:查询监测站位通过站位Id
     * @Param stationId:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/28 11:01
     */
    @ApiOperation(value ="查询监测站位通过站位Id")
    @PostMapping("/queryStationInfoById")
    @ApiImplicitParam(name = "stationId",value = "岸基站Id",required = true)
    public Response<List<MonitorStationInfo>> queryStationInfoById(Long stationId,Long reportId,String stationName){
        List<MonitorStationInfo> monitorStationInfoList=monitorStationInfoService.queryStationInfoById(stationId,reportId,stationName);
        return Responses.or(monitorStationInfoList);
    }


    /*
     * @Description:生物种类统计信息展示（地图）
     * @Param year:
     * @Param voyage:
     * @Param statisticType:  统计类型
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 15:37
     */
    @ApiOperation(value ="生物种类统计信息展示")
    @PostMapping("/statisticTypeOneMap")
    public Response<HashMap<String,Object>> statisticTypeOneMap(String year, String voyage,String statisticType){
        HashMap<String,Object> result=new HashMap<String, Object>();
        //站位信息
        List<MonitorStationInfo> monitorStationInfoList = monitorStationInfoService.queryStationInfo(year, voyage);
        result.put("stationCount", new BigDecimal(monitorStationInfoList.size()));

        //大型底栖动物
//        //大型底栖动物定量
//        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeService.queryBiologicalType(year, voyage);
//        //result.put("macrobenthosQuantitativeCount",macrobenthosQuantitativeList.size());
//        //大型底栖动物定性
//        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeService.queryBiologicalType(year, voyage);
        //result.put("macrobenthosQualitativeCount",macrobenthosQualitativeList.size());

        //鱼卵仔鱼
//        //鱼卵定量
//        List<FisheggQuantitative> fisheggQuantitativeList = fisheggQuantitativeService.queryBiologicalType(year, voyage);
//        //鱼卵定性
//        List<FisheggQualitative> fisheggQualitativeList = fisheggQualitativeService.queryBiologicalType(year, voyage);
//        //仔鱼定量
//        List<SmallfishQuantitative> smallfishQuantitativeList = smallfishQuantitativeService.queryBiologicalType(year, voyage);
//        //仔鱼定性
//        List<SmallfishQualitative> smallfishQualitativeList = smallfishQualitativeService.queryBiologicalType(year, voyage);

//        //游泳动物
//        List<SwimminganimalIdentification> swimminganimalIdentificationList = swimminganimalIdentificationService.queryBiologicalType(year, voyage);

//        //潮间带生物
//        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList = intertidalzonebiologicalQuantitativeService.queryBiologicalType(year, voyage);

        //浮游植物
        List<Phytoplankton> phytoplanktonList = phytoplanktonService.queryBiologicalType(year, voyage);

        //浮游动物
        //大型浮游动物一型网
        List<LargezooplanktonInet> largezooplanktonInetList = largezooplanktonInetService.queryBiologicalType(year, voyage);
        //小型浮游动物二型网
        List<SmallzooplanktonIinet> smallzooplanktonIinetList = smallzooplanktonIinetService.queryBiologicalType(year, voyage);
        //按统计条件汇总生物种类总量
        result.put("biologicCount", new BigDecimal(phytoplanktonList.size()
                + largezooplanktonInetList.size() + smallzooplanktonIinetList.size()));

        if(statisticType.equals("bioType")) {

            //大型底栖动物
            result.put("macrobenthosCount", null);
            //潮间带生物
            result.put("intertidalzonebiologicalCount", null);
            //浮游动物
            result.put("zooplanktonCount", new BigDecimal(largezooplanktonInetList.size() + smallzooplanktonIinetList.size()));
            //鱼卵仔鱼
            result.put("fisheggSmallCount", null);
            //游泳动物
            result.put("swimminganimalCount", null);
            //浮游植物
            result.put("phytoplanktonCount", new BigDecimal(phytoplanktonList.size()));

        }
        if(statisticType.equals("density")){
            //大型底栖动物
            //大型底栖动物定量
            HashMap<String,BigDecimal> macrobenthosQuantitativeDensityMap=macrobenthosQuantitativeService.queryBiologicalDensity(year,voyage);
            //大型底栖动物定性
            HashMap<String,BigDecimal> macrobenthosQualitativeDensityMap=macrobenthosQualitativeService.queryBiologicalDensity(year,voyage);

            BigDecimal density1=new BigDecimal(0);
            density1=macrobenthosQuantitativeDensityMap.get("density").add(macrobenthosQualitativeDensityMap.get("density"));
            if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(1))){
                density1=density1.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
            }
            if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("macrobenthosCount",null);
            }
            else{
                result.put("macrobenthosCount",density1);
            }

            //潮间带生物
            HashMap<String,BigDecimal> intertidalzonebiologicalQuantitativeDensityMap=intertidalzonebiologicalQuantitativeService.queryBiologicalDensity(year,voyage);
            if(intertidalzonebiologicalQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("intertidalzonebiologicalCount",null);
            }
            else{
                result.put("intertidalzonebiologicalCount",intertidalzonebiologicalQuantitativeDensityMap.get("density"));
            }

            //浮游动物
            //大型浮游动物一型网
            HashMap<String,BigDecimal> largezooplanktonInetDensityMap=largezooplanktonInetService.queryBiologicalDensity(year,voyage);
            if(largezooplanktonInetDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("largezooplanktonCount",null);
            }
            else{
                result.put("largezooplanktonCount",largezooplanktonInetDensityMap.get("density"));
            }
            //小型浮游动物二型网smallzooplanktonIinetService
            HashMap<String,BigDecimal> smallzooplanktonIinetDensityMap=smallzooplanktonIinetService.queryBiologicalDensity(year,voyage);
            if(smallzooplanktonIinetDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("smallzooplanktonCount",null);
            }
            else{
                result.put("smallzooplanktonCount",smallzooplanktonIinetDensityMap.get("density"));
            }

            //鱼卵仔鱼
            //鱼卵定量
            HashMap<String,BigDecimal> fisheggQuantitativeDensityMap= fisheggQuantitativeService.queryBiologicalDensity(year,voyage);
            //仔鱼定量
            HashMap<String,BigDecimal> smallfishQuantitativeDensityMap= smallfishQuantitativeService.queryBiologicalDensity(year,voyage);
            BigDecimal density2=new BigDecimal(0);
            density2=fisheggQuantitativeDensityMap.get("density").add(smallfishQuantitativeDensityMap.get("density"));
            if(fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(1))){
                density2=density2.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
            }
            if(fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("fisheggSmallCount",null);
            }
            else{
                result.put("fisheggSmallCount",density2);
            }

            //游泳动物
            result.put("swimminganimalCount", null);

            //浮游植物

        }
        if(statisticType.equals("biomass")){
            //大型底栖动物
            //大型底栖动物定量
            HashMap<String,BigDecimal> macrobenthosQuantitativeBiomassMap= macrobenthosQuantitativeService.queryBiologicalBiomass(year,voyage);
            //大型底栖动物定性
            HashMap<String,BigDecimal> macrobenthosQualitativeBiomassMap=macrobenthosQualitativeService.queryBiologicalBiomass(year,voyage);
            BigDecimal biomass1=new BigDecimal(0);
            biomass1=macrobenthosQuantitativeBiomassMap.get("density").add(macrobenthosQualitativeBiomassMap.get("density"));
            if(macrobenthosQuantitativeBiomassMap.get("result").equals(new BigDecimal(1))&&macrobenthosQualitativeBiomassMap.get("result").equals(new BigDecimal(1))){
                biomass1=biomass1.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
            }
            if(macrobenthosQuantitativeBiomassMap.get("result").equals(new BigDecimal(0))&&macrobenthosQualitativeBiomassMap.get("result").equals(new BigDecimal(0))){
                result.put("macrobenthosCount",null);
            }
            else{
                result.put("macrobenthosCount",biomass1);
            }


            //潮间带生物
            HashMap<String,BigDecimal> intertidalzonebiologicalQuantitativeDensityMap=intertidalzonebiologicalQuantitativeService.queryBiologicalBiomass(year,voyage);
            if(intertidalzonebiologicalQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("intertidalzonebiologicalCount",null);
            }
            else{
                result.put("intertidalzonebiologicalCount",intertidalzonebiologicalQuantitativeDensityMap.get("density"));
            }

            //浮游动物
            HashMap<String,BigDecimal>  largezooplanktonInetDensityMap=largezooplanktonInetService.queryBiologicalBiomass(year,voyage);
            if(largezooplanktonInetDensityMap.get("result").equals(new BigDecimal(0))){
                result.put("zooplanktonCount",null);
            }
            else{
                result.put("zooplanktonCount",largezooplanktonInetDensityMap.get("density"));
            }

            //鱼卵仔鱼
            result.put("fisheggSmallCount",null);

            //游泳动物
            result.put("swimminganimalCount", null);

            //浮游植物
            result.put("phytoplanktonCount", null);
        }
        return Responses.or(result);
    }



    @ApiOperation(value ="生物种类组成统计信息展示")
    @PostMapping("/statisticTypeFromOneMap")
    public Response<HashMap<String, Integer>> statisticTypeFromOneMap(String year, String voyage, String bioType) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
        switch (bioType) {
            case "macrobenthos"://大型底栖动物
//                List listMacrobenthos = new ArrayList();
//                List listMacrobenthos1 = macrobenthosQuantitativeService.statisticTypeFromOneMap(year, voyage);
//                List listMacrobenthos2 = macrobenthosQualitativeService.statisticTypeFromOneMap(year, voyage);
//                listMacrobenthos.addAll(listMacrobenthos1);
//                listMacrobenthos.addAll(listMacrobenthos2);
//                Set uniqueSetMacrobenthos = new HashSet(listMacrobenthos);
//                for (Object temp:uniqueSetMacrobenthos
//                ) {
//                    resultMap.put(temp.toString(),Collections.frequency(listMacrobenthos, temp));
//                }
                break;
            case "fisheggSmall"://鱼卵仔鱼
//                List listFisheggSmall = new ArrayList();
//                List listfishegg1 = fisheggQuantitativeService.statisticTypeFromOneMap(year, voyage);
//                List listfishegg2 = fisheggQualitativeService.statisticTypeFromOneMap(year, voyage);
//                List listsmallfish1 = smallfishQuantitativeService.statisticTypeFromOneMap(year, voyage);
//                List listsmallfish2 = smallfishQualitativeService.statisticTypeFromOneMap(year, voyage);
//
//                listFisheggSmall.addAll(listfishegg1);
//                listFisheggSmall.addAll(listfishegg2);
//                listFisheggSmall.addAll(listsmallfish1);
//                listFisheggSmall.addAll(listsmallfish2);
//
//                Set uniqueSetFisheggSmall = new HashSet(listFisheggSmall);
//                for (Object temp:uniqueSetFisheggSmall
//                ) {
//                    resultMap.put(temp.toString(),Collections.frequency(listFisheggSmall, temp));
//                }
                break;
            case "swimminganimal"://游泳动物
//                resultMap = swimminganimalIdentificationService.statisticTypeFromOneMap(year, voyage);
                break;
            case "intertidalzonebiological"://潮间带生物
//                resultMap = intertidalzonebiologicalQuantitativeService.statisticTypeFromOneMap(year, voyage);
                break;
            case "phytoplankton"://浮游植物
                resultMap = phytoplanktonService.statisticTypeFromOneMap(year, voyage);
                break;
            case "zooplankton"://浮游动物
                List listZooplankton = new ArrayList();
                List listZooplankton1 = largezooplanktonInetService.statisticTypeFromOneMap(year, voyage);
                List listZooplankton2 = smallzooplanktonIinetService.statisticTypeFromOneMap(year, voyage);
                listZooplankton.addAll(listZooplankton1);
                listZooplankton.addAll(listZooplankton2);
                Set uniqueSetZooplankton = new HashSet(listZooplankton);
                for (Object temp:uniqueSetZooplankton
                ) {
                    resultMap.put(temp.toString(),Collections.frequency(listZooplankton, temp));
                }
                break;
        }
        return Responses.or(resultMap);
    }



    @ApiOperation(value ="站位统计信息展示")
    @PostMapping("/statisticStationOneMap")
    public Response<HashMap<Long,Object>> statisticStationOneMap(String year, String voyage,String statisticType,String bioType){
        HashMap<Long,Object> result=new HashMap<Long, Object>();
        List<MonitorStationInfo> monitorStationInfoList=monitorStationInfoService.queryStationInfo(year,voyage);
        if(statisticType.equals("density")){
            for (int i = 0; i <monitorStationInfoList.size() ; i++) {
                //大型底栖动物
                if(bioType.equals("macrobenthos")){
                    //大型底栖动物定量
                    HashMap<String,BigDecimal> macrobenthosQuantitativeDensityMap= macrobenthosQuantitativeService.queryBiologicalDensityByStation(year,voyage,monitorStationInfoList.get(i).getStationId());
                    //大型底栖动物定性
                    HashMap<String,BigDecimal> macrobenthosQualitativeDensityMap=macrobenthosQualitativeService.queryBiologicalDensityByStation(year,voyage,monitorStationInfoList.get(i).getStationId());
                    BigDecimal density1=new BigDecimal(0);
                    density1=macrobenthosQuantitativeDensityMap.get("density").add(macrobenthosQualitativeDensityMap.get("density"));
                    if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(1))){
                        density1=density1.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
                    }
                    if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),density1);
                    }
                }
                //鱼卵仔鱼
                if(bioType.equals("fisheggSmall")) {
                    //鱼卵定量
                    HashMap<String,BigDecimal> fisheggQuantitativeDensityMap = fisheggQuantitativeService.queryBiologicalDensityByStation(year, voyage, monitorStationInfoList.get(i).getStationId());
                    //仔鱼定量
                    HashMap<String,BigDecimal> smallfishQuantitativeDensityMap = smallfishQuantitativeService.queryBiologicalDensityByStation(year, voyage, monitorStationInfoList.get(i).getStationId());
                    BigDecimal density1=new BigDecimal(0);
                    density1=fisheggQuantitativeDensityMap.get("density").add(smallfishQuantitativeDensityMap.get("density"));
                    if(fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(1))){
                        density1=density1.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
                    }
                    if(fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),density1);
                    }
                }
                //潮间带生物
                if(bioType.equals("intertidalzonebiological")){
                    HashMap<String,BigDecimal> intertidalzonebiologicalQuantitativeDensityMap=intertidalzonebiologicalQuantitativeService.queryBiologicalDensityByStation(year,voyage,monitorStationInfoList.get(i).getStationId());
                    if(intertidalzonebiologicalQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),intertidalzonebiologicalQuantitativeDensityMap.get("density"));
                    }
                }
                //大型浮游动物一型网
                if(bioType.equals("largezooplankton")){
                    HashMap<String,BigDecimal>  largezooplanktonInetDensityMap=largezooplanktonInetService.queryBiologicalDensityByStation(year,voyage,monitorStationInfoList.get(i).getStationId());
                    if(largezooplanktonInetDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),largezooplanktonInetDensityMap.get("density"));
                    }
                }
                //小型浮游动物二型网
                if(bioType.equals("smallzooplankton")){
                    HashMap<String,BigDecimal> smallzooplanktonIinetDensityMap=smallzooplanktonIinetService.queryBiologicalDensityByStation(year,voyage,monitorStationInfoList.get(i).getStationId());
                    if(smallzooplanktonIinetDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),smallzooplanktonIinetDensityMap.get("density"));
                    }
                }
            }
        }
        if (statisticType.equals("biomass")) {
            for (int i = 0; i < monitorStationInfoList.size(); i++) {
                //大型底栖动物
                if (bioType.equals("macrobenthos")) {
                    //大型底栖动物定量
                    HashMap<String,BigDecimal>  macrobenthosQuantitativeBiomassMap = macrobenthosQuantitativeService.queryBiologicalBiomassByStation(year, voyage,monitorStationInfoList.get(i).getStationId());
                    //大型底栖动物定性
                    HashMap<String,BigDecimal>  macrobenthosQualitativeBiomassMap = macrobenthosQualitativeService.queryBiologicalBiomassByStation(year, voyage,monitorStationInfoList.get(i).getStationId());
                    BigDecimal biomass1=new BigDecimal(0);
                    biomass1=macrobenthosQuantitativeBiomassMap.get("density").add(macrobenthosQualitativeBiomassMap.get("density"));
                    if (macrobenthosQuantitativeBiomassMap.get("result").equals(new BigDecimal(1)) && macrobenthosQualitativeBiomassMap.get("result").equals(new BigDecimal(1))) {
                        biomass1 = biomass1.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
                    }
                    if (macrobenthosQuantitativeBiomassMap.get("result").equals(new BigDecimal(0)) && macrobenthosQualitativeBiomassMap.get("result").equals(new BigDecimal(0))) {
                        result.put(monitorStationInfoList.get(i).getStationId(), null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),biomass1);
                    }
                }
                //鱼卵仔鱼

                //游泳动物

                //潮间带生物
                if (bioType.equals("intertidalzonebiological")) {
                    HashMap<String,BigDecimal>  intertidalzonebiologicalQuantitativeDensityMap = intertidalzonebiologicalQuantitativeService.queryBiologicalBiomassByStation(year, voyage,monitorStationInfoList.get(i).getStationId());
                    if(intertidalzonebiologicalQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),intertidalzonebiologicalQuantitativeDensityMap.get("density"));
                    }
                }
                //浮游植物

                //浮游动物
                if (bioType.equals("zooplankton")) {
                    HashMap<String,BigDecimal>  largezooplanktonInetDensityMap = largezooplanktonInetService.queryBiologicalBiomassByStation(year, voyage,monitorStationInfoList.get(i).getStationId());
                    if(largezooplanktonInetDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(monitorStationInfoList.get(i).getStationId(),null);
                    }
                    else{
                        result.put(monitorStationInfoList.get(i).getStationId(),largezooplanktonInetDensityMap.get("density"));
                    }
                }
            }
        }
        return Responses.or(result);
    }


    //统计分析模块

    @ApiOperation(value ="一年内某种类的密度和生物量变化范围")
    @PostMapping("/densityOneYear")
    public  Response<HashMap<String,HashMap<String,BigDecimal>>> densityOneYear(String year, String voyage,String statisticType,String bioType){
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();


        if(statisticType.equals("density")){
            //大型底栖动物
            if(bioType.equals("macrobenthos")) {
                //大型底栖动物定量
                HashMap<String, BigDecimal> macrobenthosQuantitativeDensityMap = macrobenthosQuantitativeService.queryBiologicalDensityOneYear(year, voyage);
                //大型底栖动物定性
                HashMap<String, BigDecimal> macrobenthosQualitativeDensityMap = macrobenthosQualitativeService.queryBiologicalDensityOneYear(year, voyage);
                HashMap<String, BigDecimal> valuemacroMap = new HashMap<>();

                if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(1))){
                    //最大值
                    BigDecimal max1 = macrobenthosQuantitativeDensityMap.get("max");
                    BigDecimal max2 = macrobenthosQualitativeDensityMap.get("max");
                    BigDecimal max = max1;
                    if (max1.compareTo(max2) > -1) {//max1大于等于max2

                    }
                    if (max1.compareTo(max2) < 1) {//max1小于等于max2
                        max = max2;
                    }
                    valuemacroMap.put("max", max);
                    //最小值
                    BigDecimal min1 = macrobenthosQuantitativeDensityMap.get("min");
                    BigDecimal min2 = macrobenthosQualitativeDensityMap.get("min");
                    BigDecimal min = min1;
                    if (min1.compareTo(min2) > -1) {//max1大于等于max2
                        min = min2;
                    }
                    valuemacroMap.put("min", min);
                    //平均值
                    BigDecimal ave1 = macrobenthosQuantitativeDensityMap.get("ave");
                    BigDecimal ave2 = macrobenthosQualitativeDensityMap.get("ave");
                    ave1 = ave1.add(ave2).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);

                    valuemacroMap.put("ave", ave1);
                }
                if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(0))) {
                    //最大值
                    BigDecimal max1 = macrobenthosQuantitativeDensityMap.get("max");
                    valuemacroMap.put("max", max1);
                    //最小值
                    BigDecimal min1 = macrobenthosQuantitativeDensityMap.get("min");
                    valuemacroMap.put("min", min1);
                    //平均值
                    BigDecimal ave1 = macrobenthosQuantitativeDensityMap.get("ave");
                    valuemacroMap.put("ave", ave1);
                }
                if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(1))) {
                    //最大值
                    BigDecimal max2 = macrobenthosQualitativeDensityMap.get("max");
                    valuemacroMap.put("max", max2);
                    //最小值
                    BigDecimal min2 = macrobenthosQualitativeDensityMap.get("min");
                    valuemacroMap.put("min", min2);
                    //平均值
                    BigDecimal ave2 = macrobenthosQualitativeDensityMap.get("ave");
                    valuemacroMap.put("ave", ave2);
                }
                if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(0))) {
                    //最大值
                    valuemacroMap.put("max", null);
                    //最小值
                    valuemacroMap.put("min", null);
                    //平均值
                    valuemacroMap.put("ave", null);
                }

                    resultMap.put("value", valuemacroMap);
            }
            //鱼卵仔鱼
            if(bioType.equals("fisheggSmall")) {
                //鱼卵定量
                HashMap<String, BigDecimal> fisheggQuantitativeDensityMap = fisheggQuantitativeService.queryBiologicalDensityOneYear(year, voyage);
                //仔鱼定量
                HashMap<String, BigDecimal> smallfishQuantitativeDensityMap = smallfishQuantitativeService.queryBiologicalDensityOneYear(year, voyage);
                HashMap<String, BigDecimal> valuefishMap = new HashMap<>();
                if (fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(1)) && smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(1))) {

                    //最大值
                    BigDecimal maxfish1 = fisheggQuantitativeDensityMap.get("max");
                    BigDecimal maxfish2 = smallfishQuantitativeDensityMap.get("max");
                    BigDecimal maxfish = maxfish1;
                    if (maxfish1.compareTo(maxfish2) > -1) {//max1大于等于max2

                    }
                    if (maxfish1.compareTo(maxfish2) < 1) {//max1小于等于max2
                        maxfish = maxfish2;
                    }
                    valuefishMap.put("max", maxfish);
                    //最小值
                    BigDecimal minfish1 = fisheggQuantitativeDensityMap.get("min");
                    BigDecimal minfish2 = smallfishQuantitativeDensityMap.get("min");
                    BigDecimal minfish = minfish1;
                    if (minfish1.compareTo(minfish2) > -1) {//max1大于等于max2
                        minfish = minfish2;
                    }
                    valuefishMap.put("min", minfish);
                    //平均值
                    BigDecimal avefish1 = fisheggQuantitativeDensityMap.get("ave");
                    BigDecimal avefish2 = smallfishQuantitativeDensityMap.get("ave");
                    avefish1 = avefish1.add(avefish2).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);

                    valuefishMap.put("ave", avefish1);
                }
                if (fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(1)) && smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(0))) {

                    //最大值
                    BigDecimal maxfish1 = fisheggQuantitativeDensityMap.get("max");
                    valuefishMap.put("max", maxfish1);
                    //最小值
                    BigDecimal minfish1 = fisheggQuantitativeDensityMap.get("min");
                    valuefishMap.put("min", minfish1);
                    //平均值
                    BigDecimal avefish1 = fisheggQuantitativeDensityMap.get("ave");
                    valuefishMap.put("ave", avefish1);
                }
                if (fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(0)) && smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(1))) {

                    //最大值
                    BigDecimal maxfish2 = smallfishQuantitativeDensityMap.get("max");
                    valuefishMap.put("max", maxfish2);
                    //最小值
                    BigDecimal minfish2 = smallfishQuantitativeDensityMap.get("min");
                    valuefishMap.put("min", minfish2);
                    //平均值
                    BigDecimal avefish2 = smallfishQuantitativeDensityMap.get("ave");
                    valuefishMap.put("ave", avefish2);
                }
                if (fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(0)) && smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(0))) {

                    //最大值
                    valuefishMap.put("max", null);
                    //最小值
                    valuefishMap.put("min", null);
                    //平均值
                    valuefishMap.put("ave", null);
                }
                resultMap.put("value", valuefishMap);
            }
            //游泳动物

            //潮间带生物
            if (bioType.equals("intertidalzonebiological")) {
                HashMap<String, BigDecimal> intertidalzonebiologicalQuantitativeDensity = intertidalzonebiologicalQuantitativeService.queryBiologicalDensityOneYear(year, voyage);
                HashMap<String, BigDecimal> valueinterMap = new HashMap<>();
                valueinterMap.put("max", intertidalzonebiologicalQuantitativeDensity.get("max"));
                valueinterMap.put("min", intertidalzonebiologicalQuantitativeDensity.get("min"));
                valueinterMap.put("ave", intertidalzonebiologicalQuantitativeDensity.get("ave"));
                resultMap.put("value", valueinterMap);
            }
            //浮游植物

            //浮游动物
            //大型浮游动物一型网
            if(bioType.equals("largezooplankton")) {
                HashMap<String, BigDecimal> largezooplanktonInetDensity = largezooplanktonInetService.queryBiologicalDensityOneYear(year, voyage);
                HashMap<String, BigDecimal> valuelargezoopMap = new HashMap<>();

                valuelargezoopMap.put("max", largezooplanktonInetDensity.get("max"));
                valuelargezoopMap.put("min", largezooplanktonInetDensity.get("min"));
                valuelargezoopMap.put("ave", largezooplanktonInetDensity.get("ave"));
                resultMap.put("value", valuelargezoopMap);
            }
            //小型浮游动物二型网smallzooplanktonIinetService
            if(bioType.equals("smallzooplankton")) {
                HashMap<String, BigDecimal> smallzooplanktonIinetDensity = smallzooplanktonIinetService.queryBiologicalDensityOneYear(year, voyage);
                HashMap<String, BigDecimal> valuesmallzoopMap = new HashMap<>();

                valuesmallzoopMap.put("max", smallzooplanktonIinetDensity.get("max"));
                valuesmallzoopMap.put("min", smallzooplanktonIinetDensity.get("min"));
                valuesmallzoopMap.put("ave", smallzooplanktonIinetDensity.get("ave"));
                resultMap.put("value", valuesmallzoopMap);
            }
        }
        if (statisticType.equals("biomass")){
            //大型底栖动物
            if(bioType.equals("macrobenthos")) {
                //大型底栖动物定量
                HashMap<String, BigDecimal> macrobenthosQuantitativeBiomass = macrobenthosQuantitativeService.queryBiologicalBiomassOneYear(year, voyage);
                //大型底栖动物定性
                HashMap<String, BigDecimal> macrobenthosQualitativeBiomass = macrobenthosQualitativeService.queryBiologicalBiomassOneYear(year, voyage);
                HashMap<String, BigDecimal> valuemacroMap = new HashMap<>();

                if (macrobenthosQuantitativeBiomass.get("result").equals(new BigDecimal(1)) && macrobenthosQualitativeBiomass.get("result").equals(new BigDecimal(1))) {

                    //最大值
                    BigDecimal max1 = macrobenthosQuantitativeBiomass.get("max");
                    BigDecimal max2 = macrobenthosQualitativeBiomass.get("max");
                    BigDecimal max = max1;
                    if (max1.compareTo(max2) > -1) {//max1大于等于max2

                    }
                    if (max1.compareTo(max2) < 1) {//max1小于等于max2
                        max = max2;
                    }
                    valuemacroMap.put("max", max);
                    //最小值
                    BigDecimal min1 = macrobenthosQuantitativeBiomass.get("min");
                    BigDecimal min2 = macrobenthosQualitativeBiomass.get("min");
                    BigDecimal min = min1;
                    if (min1.compareTo(min2) > -1) {//max1大于等于max2
                        min = min2;
                    }
                    valuemacroMap.put("min", min);
                    //平均值
                    BigDecimal ave1 = macrobenthosQuantitativeBiomass.get("ave");
                    BigDecimal ave2 = macrobenthosQualitativeBiomass.get("ave");
                    ave1 = ave1.add(ave2).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);

                    valuemacroMap.put("ave", ave1);
                }
                if (macrobenthosQuantitativeBiomass.get("result").equals(new BigDecimal(1)) && macrobenthosQualitativeBiomass.get("result").equals(new BigDecimal(0))) {

                    //最大值
                    BigDecimal max1 = macrobenthosQuantitativeBiomass.get("max");
                    valuemacroMap.put("max", max1);
                    //最小值
                    BigDecimal min1 = macrobenthosQuantitativeBiomass.get("min");
                    valuemacroMap.put("min", min1);
                    //平均值
                    BigDecimal ave1 = macrobenthosQuantitativeBiomass.get("ave");
                    valuemacroMap.put("ave", ave1);
                }
                if (macrobenthosQuantitativeBiomass.get("result").equals(new BigDecimal(0)) && macrobenthosQualitativeBiomass.get("result").equals(new BigDecimal(1))) {

                    //最大值
                    BigDecimal max2 = macrobenthosQualitativeBiomass.get("max");
                    valuemacroMap.put("max", max2);
                    //最小值
                    BigDecimal min2 = macrobenthosQualitativeBiomass.get("min");
                    valuemacroMap.put("min", min2);
                    //平均值
                    BigDecimal ave2 = macrobenthosQualitativeBiomass.get("ave");
                    valuemacroMap.put("ave", ave2);
                }
                if (macrobenthosQuantitativeBiomass.get("result").equals(new BigDecimal(0)) && macrobenthosQualitativeBiomass.get("result").equals(new BigDecimal(0))) {

                    //最大值
                    valuemacroMap.put("max", null);
                    //最小值
                    valuemacroMap.put("min", null);
                    //平均值
                    valuemacroMap.put("ave", null);
                }
                resultMap.put("value", valuemacroMap);
            }
            //鱼卵仔鱼

            //游泳动物

            //潮间带生物
            if (bioType.equals("intertidalzonebiological")) {
                HashMap<String, BigDecimal> intertidalzonebiologicalQuantitativeBiomass = intertidalzonebiologicalQuantitativeService.queryBiologicalBiomassOneYear(year, voyage);
                HashMap<String, BigDecimal> valueinterMap = new HashMap<>();

                valueinterMap.put("max", intertidalzonebiologicalQuantitativeBiomass.get("max"));
                valueinterMap.put("min", intertidalzonebiologicalQuantitativeBiomass.get("min"));
                valueinterMap.put("ave", intertidalzonebiologicalQuantitativeBiomass.get("ave"));
                resultMap.put("value", valueinterMap);
            }
            //浮游植物

            //浮游动物
            if (bioType.equals("zooplankton")) {
                HashMap<String, BigDecimal> largezooplanktonInetBiomass = largezooplanktonInetService.queryBiologicalBiomassOneYear(year, voyage);
                HashMap<String, BigDecimal> valuelargezoopMap = new HashMap<>();

                valuelargezoopMap.put("max", largezooplanktonInetBiomass.get("max"));
                valuelargezoopMap.put("min", largezooplanktonInetBiomass.get("min"));
                valuelargezoopMap.put("ave", largezooplanktonInetBiomass.get("ave"));
                resultMap.put("value", valuelargezoopMap);
            }
        }
        return Responses.or(resultMap);
    }


    @ApiOperation(value ="海洋生物总种类数_多年数据统计")
    @PostMapping("/bioTypeCountMultiYear")
    public Response<HashMap<String,HashMap<String,String>>> bioTypeCountMultiYear(String startYear,String endYear){

        BigDecimal interval=new BigDecimal(0);
        BigDecimal srartYearDec=new BigDecimal(startYear);
        BigDecimal endYearDec=new BigDecimal(endYear);
        interval = endYearDec.subtract(srartYearDec);
        List<String> yearList=new ArrayList<>();
        for (int i = 0; i < interval.intValue()+1; i++) {
            yearList.add(srartYearDec.add(new BigDecimal(i)).toString());
        }
        String yearKey=yearList.stream().collect(Collectors.joining(","));//横坐标

        HashMap<String,HashMap<String,String>> resultMap=new HashMap<>();
        HashMap<String,String> result=new HashMap<String, String>();
        //大型底栖动物
//        String macrobenthosValue="";
//        for (int i = 0; i < yearList.size(); i++) {
//            //大型底栖动物定量
//            List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeService.queryBiologicalType(yearList.get(i), null);
//            //大型底栖动物定性
//            List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeService.queryBiologicalType(yearList.get(i), null);
//            Integer macrobenthosCount = macrobenthosQuantitativeList.size() + macrobenthosQualitativeList.size();
//            macrobenthosValue +=macrobenthosCount.toString()+",";
//        }
//        result.put("macrobenthosCount", macrobenthosValue);


        //鱼卵仔鱼
//        String fisheggSmallValue="";
//        for (int i = 0; i < yearList.size(); i++) {
//            //鱼卵定量
//            List<FisheggQuantitative> fisheggQuantitativeList = fisheggQuantitativeService.queryBiologicalType(yearList.get(i), null);
//            //鱼卵定性
//            List<FisheggQualitative> fisheggQualitativeList = fisheggQualitativeService.queryBiologicalType(yearList.get(i), null);
//            //仔鱼定量
//            List<SmallfishQuantitative> smallfishQuantitativeList = smallfishQuantitativeService.queryBiologicalType(yearList.get(i), null);
//            //仔鱼定性
//            List<SmallfishQualitative> smallfishQualitativeList = smallfishQualitativeService.queryBiologicalType(yearList.get(i), null);
//            Integer fisheggSmallCount = fisheggQuantitativeList.size() + fisheggQualitativeList.size() + smallfishQuantitativeList.size() + smallfishQualitativeList.size();
//            fisheggSmallValue +=fisheggSmallCount.toString()+",";
//        }
//        result.put("fisheggSmallCount", fisheggSmallValue);

        //游泳动物
//        String swimminganimalValue="";
//        for (int i = 0; i < yearList.size(); i++) {
//            List<SwimminganimalIdentification> swimminganimalIdentificationList = swimminganimalIdentificationService.queryBiologicalType(yearList.get(i), null);
//            Integer swimminganimalCount = swimminganimalIdentificationList.size();
//            swimminganimalValue +=swimminganimalCount.toString()+",";
//        }
//        result.put("swimminganimalCount", swimminganimalValue);

//        //潮间带生物
//        String intertidalzonebiologicalValue="";
//        for (int i = 0; i < yearList.size(); i++) {
//            List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList = intertidalzonebiologicalQuantitativeService.queryBiologicalType(yearList.get(i), null);
//            Integer intertidalzonebiologicalCount = intertidalzonebiologicalQuantitativeList.size();
//            intertidalzonebiologicalValue +=intertidalzonebiologicalCount.toString()+",";
//        }
//        result.put("intertidalzonebiologicalCount", intertidalzonebiologicalValue);

        //浮游植物
        String phytoplanktonValue="";
        for (int i = 0; i < yearList.size(); i++) {
            List<Phytoplankton> phytoplanktonList = phytoplanktonService.queryBiologicalType(yearList.get(i), null);
            Integer phytoplanktonCount = phytoplanktonList.size();
            phytoplanktonValue +=phytoplanktonCount.toString()+",";
        }
        result.put("浮游植物", phytoplanktonValue);

        //浮游动物
        String zooplanktonValue="";
        for (int i = 0; i < yearList.size(); i++) {
            //大型浮游动物一型网
            List<LargezooplanktonInet> largezooplanktonInetList = largezooplanktonInetService.queryBiologicalType(yearList.get(i), null);
            //小型浮游动物二型网
            List<SmallzooplanktonIinet> smallzooplanktonIinetList = smallzooplanktonIinetService.queryBiologicalType(yearList.get(i), null);
            Integer zooplanktonCount = largezooplanktonInetList.size() + smallzooplanktonIinetList.size();
            zooplanktonValue +=zooplanktonCount.toString()+",";
        }
        result.put("浮游动物", zooplanktonValue);

        resultMap.put(yearKey,result);
        return Responses.or(resultMap);
    }


    @ApiOperation(value ="密度生物量均值变化_多年数据统计")
    @PostMapping("/bioDensityBiomassCountMultiYear")
    public Response<HashMap<String,BigDecimal>> bioDensityBiomassCountMultiYear(String startYear,String endYear,String statisticType,String bioType){
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

        if(statisticType.equals("density")){
            //大型底栖动物
            if (bioType.equals("macrobenthos")) {
                for (int i = 0; i < yearList.size(); i++) {
                    //大型底栖动物定量
                    HashMap<String,BigDecimal> macrobenthosQuantitativeDensityMap = macrobenthosQuantitativeService.queryBiologicalDensity(yearList.get(i), null);
                    //大型底栖动物定性
                    HashMap<String,BigDecimal> macrobenthosQualitativeDensityMap = macrobenthosQualitativeService.queryBiologicalDensity(yearList.get(i), null);
                    BigDecimal density1=new BigDecimal(0);
                    density1=macrobenthosQuantitativeDensityMap.get("density").add(macrobenthosQualitativeDensityMap.get("density"));
                    if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(1))){
                        density1=density1.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
                    }
                    if(macrobenthosQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&macrobenthosQualitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),density1);
                    }
                }
                resultMap.put("macrobenthosCount", result);
            }
            //潮间带生物
            if(bioType.equals("intertidalzonebiological")) {
                for (int i = 0; i < yearList.size(); i++) {
                    HashMap<String,BigDecimal> intertidalzonebiologicalQuantitativeDensityMap= intertidalzonebiologicalQuantitativeService.queryBiologicalDensity(yearList.get(i), null);
                    if(intertidalzonebiologicalQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),intertidalzonebiologicalQuantitativeDensityMap.get("density"));
                    }
                }
                resultMap.put("intertidalzonebiologicalCount", result);
            }
            //大型浮游动物一型网
            if(bioType.equals("largezooplanktonCount")) {
                for (int i = 0; i < yearList.size(); i++) {
                    //大型浮游动物一型网
                    HashMap<String,BigDecimal>  largezooplanktonInetDensityMap = largezooplanktonInetService.queryBiologicalDensity(yearList.get(i), null);
                    if(largezooplanktonInetDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),largezooplanktonInetDensityMap.get("density"));
                    }
                }
                resultMap.put("largezooplanktonCount", result);
            }
            //小型浮游动物二型网
            if(bioType.equals("smallzooplanktonCount")) {
                for (int i = 0; i < yearList.size(); i++) {
                    //小型浮游动物二型网
                    HashMap<String,BigDecimal>  smallzooplanktonIinetDensityMap = smallzooplanktonIinetService.queryBiologicalDensity(yearList.get(i), null);
                    if(smallzooplanktonIinetDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),smallzooplanktonIinetDensityMap.get("density"));
                    }
                }
                resultMap.put("smallzooplanktonCount", result);
            }
            //鱼卵仔鱼
            if(bioType.equals("fisheggSmall")) {
                for (int i = 0; i < yearList.size(); i++) {
                    //鱼卵定量
                    HashMap<String,BigDecimal>  fisheggQuantitativeDensityMap = fisheggQuantitativeService.queryBiologicalDensity(yearList.get(i), null);
                    //仔鱼定量
                    HashMap<String,BigDecimal>  smallfishQuantitativeDensityMap = smallfishQuantitativeService.queryBiologicalDensity(yearList.get(i), null);
                    BigDecimal density1=new BigDecimal(0);
                    density1=fisheggQuantitativeDensityMap.get("density").add(smallfishQuantitativeDensityMap.get("density"));
                    if(fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(1))&&smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(1))){
                        density1=density1.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
                    }
                    if(fisheggQuantitativeDensityMap.get("result").equals(new BigDecimal(0))&&smallfishQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),density1);
                    }
                }
                resultMap.put("fisheggSmallCount", result);
            }
            //游泳动物
            if(bioType.equals("swimminganimal")) {
                for (int i = 0; i < yearList.size(); i++) {
                    result.put("swimminganimalCount", null);
                }
            }
            //浮游植物

        }
        if(statisticType.equals("biomass")){
            //大型底栖动物
            if(bioType.equals("macrobenthos")) {
                for (int i = 0; i < yearList.size(); i++) {
                    //大型底栖动物定量
                    HashMap<String,BigDecimal>  macrobenthosQuantitativeBiomassMap = macrobenthosQuantitativeService.queryBiologicalBiomass(yearList.get(i), null);
                    //大型底栖动物定性
                    HashMap<String,BigDecimal>  macrobenthosQualitativeBiomassMap = macrobenthosQualitativeService.queryBiologicalBiomass(yearList.get(i), null);
                    BigDecimal biomass1=new BigDecimal(0);
                    biomass1=macrobenthosQuantitativeBiomassMap.get("density").add(macrobenthosQualitativeBiomassMap.get("density"));
                    if (macrobenthosQuantitativeBiomassMap.get("result").equals(new BigDecimal(1)) && macrobenthosQualitativeBiomassMap.get("result").equals(new BigDecimal(1))) {
                        biomass1 = biomass1.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
                    }
                    if (macrobenthosQuantitativeBiomassMap.get("result").equals(new BigDecimal(0)) && macrobenthosQualitativeBiomassMap.get("result").equals(new BigDecimal(0))) {
                        result.put(yearList.get(i), null);
                    }
                    else{
                        result.put(yearList.get(i),biomass1);
                    }
                }
                resultMap.put("macrobenthosCount", result);
            }
            //潮间带生物
            if(bioType.equals("intertidalzonebiological")) {
                for (int i = 0; i < yearList.size(); i++) {
                    HashMap<String,BigDecimal> intertidalzonebiologicalQuantitativeDensityMap = intertidalzonebiologicalQuantitativeService.queryBiologicalBiomass(yearList.get(i), null);
                    if(intertidalzonebiologicalQuantitativeDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),intertidalzonebiologicalQuantitativeDensityMap.get("density"));
                    }
                }
                resultMap.put("intertidalzonebiologicalCount", result);
            }
            //浮游动物
            if(bioType.equals("zooplankton")) {
                for (int i = 0; i < yearList.size(); i++) {
                    HashMap<String,BigDecimal> largezooplanktonInetDensityMap = largezooplanktonInetService.queryBiologicalBiomass(yearList.get(i), null);
                    if(largezooplanktonInetDensityMap.get("result").equals(new BigDecimal(0))){
                        result.put(yearList.get(i),null);
                    }
                    else{
                        result.put(yearList.get(i),largezooplanktonInetDensityMap.get("density"));
                    }
                }
                resultMap.put("zooplanktonCount", result);
            }
            //鱼卵仔鱼
            if(bioType.equals("fisheggSmall")) {
                result.put("fisheggSmallCount", null);
            }
            //游泳动物
            if(bioType.equals("swimminganimal")) {
                result.put("swimminganimalCount", null);
            }
            //浮游植物
            if(bioType.equals("phytoplankton")) {
                result.put("phytoplanktonCount", null);
            }
        }


        return Responses.or(result);
    }
}

