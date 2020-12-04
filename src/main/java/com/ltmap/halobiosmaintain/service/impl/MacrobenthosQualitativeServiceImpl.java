package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQualitativeMapper;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.IMacrobenthosQualitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public BigDecimal queryBiologicalDensity(String year, String voyage) {
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
            density = density.add(macrobenthosQualitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(macrobenthosQualitativeList.size()));
        return density;
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
        if(macrobenthosQualitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
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
    public BigDecimal queryBiologicalBiomass(String year, String voyage) {
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
            density = density.add(macrobenthosQualitativeList.get(i).getBiomass());
        }
        density=density.subtract(new BigDecimal(macrobenthosQualitativeList.size()));
        return density;
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
        if(macrobenthosQualitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
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
    public BigDecimal queryBiologicalDensityByStation(String year, String voyage,Long stationId){
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
            density = density.add(macrobenthosQualitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(macrobenthosQualitativeList.size()));
        return density;
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
    public BigDecimal queryBiologicalBiomassByStation(String year, String voyage,Long stationId) {
        List<MacrobenthosQualitative> macrobenthosQualitativeList = macrobenthosQualitativeMapper.queryBiologicalType(year, voyage,stationId);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQualitativeList.size(); i++) {
            biomass = biomass.add(macrobenthosQualitativeList.get(i).getBiomass());
        }
        biomass=biomass.subtract(new BigDecimal(macrobenthosQualitativeList.size()));
        return biomass;
    }
}
