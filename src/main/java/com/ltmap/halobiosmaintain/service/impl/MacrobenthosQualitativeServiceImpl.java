package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQualitativeMapper;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.IMacrobenthosQualitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 大型底栖动物定性表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class MacrobenthosQualitativeServiceImpl extends ServiceImpl<MacrobenthosQualitativeMapper, MacrobenthosQualitative> implements IMacrobenthosQualitativeService {

    @Resource
    private MacrobenthosQualitativeMapper macrobenthosQualitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<MacrobenthosQualitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(MacrobenthosQualitative::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /*
     * @Description:询生物种种类
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:51
     */
    @Override
    public List<MacrobenthosQualitative> queryBiologicalType(String year, String voyage){
        List<MacrobenthosQualitative> macrobenthosQualitativeList=macrobenthosQualitativeMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<MacrobenthosQualitative> macrobenthosQualitativeListNew= ListDistinctUtils.distinctMacrobenthosQualitativeByMap(macrobenthosQualitativeList);
        return macrobenthosQualitativeListNew;
    }
    /*
     * @Description:生物组成
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 10:03
     */
    @Override
    public List<String> statisticTypeFromOneMap(String year, String voyage){
        List<MacrobenthosQualitative> macrobenthosQualitativeList=macrobenthosQualitativeMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        for (int i = 0; i <macrobenthosQualitativeList.size() ; i++) {
            list.add(macrobenthosQualitativeList.get(i).getBiologicalChineseName());
        }
        return list;
    }
    /*
     * @Description:密度
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 13:57
     */
    @Override
    public HashMap<String,BigDecimal> queryBiologicalDensity(String year, String voyage) {
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,null);
        macrobenthosQualitativeList=macrobenthosQualitativeList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(macrobenthosQualitativeList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
                if (macrobenthosQualitativeList.get(i).getDensity() != null) {
                    density = density.add(macrobenthosQualitativeList.get(i).getDensity());
                }
            }
            if (macrobenthosQualitativeList.size() != 0) {
                density = density.divide(new BigDecimal(macrobenthosQualitativeList.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",density);
        }
        return result;
    }

    /*
     * @Description:密度-一年内统计分析
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 13:57
     */
    @Override
    public HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage) {
        HashMap<String,BigDecimal> resultMap=new HashMap<>();
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,null);
        macrobenthosQualitativeList=macrobenthosQualitativeList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        if(macrobenthosQualitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            resultMap.put("result",new BigDecimal(0));
            return resultMap;
        }

        //求最大值
        BigDecimal max = macrobenthosQualitativeList.stream().map(MacrobenthosQualitative::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = macrobenthosQualitativeList.stream().map(MacrobenthosQualitative::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = macrobenthosQualitativeList.stream().map(MacrobenthosQualitative::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(macrobenthosQualitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
        resultMap.put("max",max);
        resultMap.put("min",min);
        resultMap.put("ave",ave);
        resultMap.put("result",new BigDecimal(1));
        return resultMap;
    }
    /*
     * @Description:生物量
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 13:57
     */
    @Override
    public HashMap<String,BigDecimal> queryBiologicalBiomass(String year, String voyage) {
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,null);
        HashMap<String,BigDecimal> result=new HashMap<>();
        macrobenthosQualitativeList=macrobenthosQualitativeList.stream().filter(x->x.getBiomass()!=null).collect(Collectors.toList());
        if(macrobenthosQualitativeList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal biomass = new BigDecimal(0);
            for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
                if (macrobenthosQualitativeList.get(i).getBiomass() != null) {
                    biomass = biomass.add(macrobenthosQualitativeList.get(i).getBiomass());
                }
            }
            if (macrobenthosQualitativeList.size() != 0) {
                biomass = biomass.divide(new BigDecimal(macrobenthosQualitativeList.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",biomass);
        }
        return result;
    }

    /*
     * @Description:生物量-一年内统计分析
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 13:57
     */
    @Override
    public HashMap<String,BigDecimal> queryBiologicalBiomassOneYear(String year, String voyage) {
        HashMap<String,BigDecimal> resultMap=new HashMap<>();
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,null);
        macrobenthosQualitativeList=macrobenthosQualitativeList.stream().filter(x->x.getBiomass()!=null).collect(Collectors.toList());

        if(macrobenthosQualitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            resultMap.put("result",new BigDecimal(0));
            return resultMap;
        }

        //求最大值
        BigDecimal max = macrobenthosQualitativeList.stream().map(MacrobenthosQualitative::getBiomass).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = macrobenthosQualitativeList.stream().map(MacrobenthosQualitative::getBiomass).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = macrobenthosQualitativeList.stream().map(MacrobenthosQualitative::getBiomass).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(macrobenthosQualitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
        resultMap.put("max",max);
        resultMap.put("min",min);
        resultMap.put("ave",ave);
        resultMap.put("result",new BigDecimal(1));
        return resultMap;
    }

    /*
     * @Description:站位的生物密度
     * @Param year:
     * @Param voyage:
     * @Param stationId:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 16:04
     */
    @Override
    public HashMap<String,BigDecimal> queryBiologicalDensityByStation(String year, String voyage,Long stationId){
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,stationId);
        macrobenthosQualitativeList=macrobenthosQualitativeList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(macrobenthosQualitativeList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else{
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
                if (macrobenthosQualitativeList.get(i).getDensity() != null) {
                    density = density.add(macrobenthosQualitativeList.get(i).getDensity());
                }
            }
            if(macrobenthosQualitativeList.size()!=0) {
                density = density.divide(new BigDecimal(macrobenthosQualitativeList.size()),2, RoundingMode.HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",density);
        }

        return result;
    }

    /*
     * @Description:站位的生物密量
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 13:57
     */
    @Override
    public  HashMap<String,BigDecimal>  queryBiologicalBiomassByStation(String year, String voyage,Long stationId) {
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,stationId);
        HashMap<String,BigDecimal> result=new HashMap<>();
        if(macrobenthosQualitativeList.size()==0){
            result.put("result",new BigDecimal(0));//0代表无值的情况
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal biomass = new BigDecimal(0);
            for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
                if (macrobenthosQualitativeList.get(i).getBiomass() != null) {
                    biomass = biomass.add(macrobenthosQualitativeList.get(i).getBiomass());
                }
            }
            if (macrobenthosQualitativeList.size() != 0) {
                biomass = biomass.divide(new BigDecimal(macrobenthosQualitativeList.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",biomass);
        }
        return result;
    }

    /*
     * @Description:大型底栖动物定性数据查询_数据管理
     * @Param macrobenthosQualitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:48
     */
    @Override
    public IPage<MacrobenthosQualitative> listMacrobenthosQualitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return macrobenthosQualitativeMapper.listMacrobenthosQualitative(page,stationName,  biologicalChineseName,  startDate,  endDate, reportId);
    }
}
