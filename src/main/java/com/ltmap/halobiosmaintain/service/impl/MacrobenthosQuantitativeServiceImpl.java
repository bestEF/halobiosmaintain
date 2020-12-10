package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.IMacrobenthosQuantitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 大型底栖动物定量表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class MacrobenthosQuantitativeServiceImpl extends ServiceImpl<MacrobenthosQuantitativeMapper, MacrobenthosQuantitative> implements IMacrobenthosQuantitativeService {

    @Resource
    private MacrobenthosQuantitativeMapper macrobenthosQuantitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<MacrobenthosQuantitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(MacrobenthosQuantitative::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /*
     * @Description:查询生物种种类
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:51
     */
    @Override
    public List<MacrobenthosQuantitative> queryBiologicalType(String year,String voyage){
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList=macrobenthosQuantitativeMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<MacrobenthosQuantitative> macrobenthosQuantitativeListNew=ListDistinctUtils.distinctMacrobenthosQuantitativeByMap(macrobenthosQuantitativeList);
        return macrobenthosQuantitativeListNew;
    }
    /*
     * @Description:平均密度
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 13:57
     */
    @Override
    public BigDecimal queryBiologicalDensity(String year, String voyage) {
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeMapper.queryBiologicalType(year, voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQuantitativeList.size(); i++) {
            density = density.add(macrobenthosQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(macrobenthosQuantitativeList.size()));
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
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeMapper.queryBiologicalType(year, voyage,null);
        macrobenthosQuantitativeList=macrobenthosQuantitativeList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());
        if(macrobenthosQuantitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }

        //求最大值
        BigDecimal max = macrobenthosQuantitativeList.stream().map(MacrobenthosQuantitative::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = macrobenthosQuantitativeList.stream().map(MacrobenthosQuantitative::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = macrobenthosQuantitativeList.stream().map(MacrobenthosQuantitative::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(macrobenthosQuantitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);

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
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeMapper.queryBiologicalType(year, voyage,null);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQuantitativeList.size(); i++) {
            if (macrobenthosQuantitativeList.get(i).getBiomass() != null) {
                biomass = biomass.add(macrobenthosQuantitativeList.get(i).getBiomass());
            }
        }
        biomass=biomass.subtract(new BigDecimal(macrobenthosQuantitativeList.size()));
        return biomass;
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
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeMapper.queryBiologicalType(year, voyage,null);
        macrobenthosQuantitativeList=macrobenthosQuantitativeList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        if(macrobenthosQuantitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = macrobenthosQuantitativeList.stream().map(MacrobenthosQuantitative::getBiomass).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = macrobenthosQuantitativeList.stream().map(MacrobenthosQuantitative::getBiomass).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = macrobenthosQuantitativeList.stream().map(MacrobenthosQuantitative::getBiomass).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(macrobenthosQuantitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
        resultMap.put("max",max);
        resultMap.put("min",min);
        resultMap.put("ave",ave);
        return resultMap;
    }

    /*
     * @Description:生物组成
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 10:02
     */
    @Override
    public List<String> statisticTypeFromOneMap(String year, String voyage){
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList=macrobenthosQuantitativeMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        for (int i = 0; i <macrobenthosQuantitativeList.size() ; i++) {
            list.add(macrobenthosQuantitativeList.get(i).getBiologicalChineseName());
        }
        return list;
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
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeMapper.queryBiologicalType(year, voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQuantitativeList.size(); i++) {
            if (macrobenthosQuantitativeList.get(i).getDensity() != null) {
                density = density.add(macrobenthosQuantitativeList.get(i).getDensity());
            }
        }
        density=density.subtract(new BigDecimal(macrobenthosQuantitativeList.size()));
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
        List<MacrobenthosQuantitative> macrobenthosQuantitativeList = macrobenthosQuantitativeMapper.queryBiologicalType(year, voyage,stationId);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < macrobenthosQuantitativeList.size(); i++) {
            if (macrobenthosQuantitativeList.get(i).getBiomass() != null) {
            biomass = biomass.add(macrobenthosQuantitativeList.get(i).getBiomass());
        }}
        biomass=biomass.subtract(new BigDecimal(macrobenthosQuantitativeList.size()));
        return biomass;
    }


    /*
     * @Description:大型底栖动物定量数据查询
     * @Param macrobenthosQuantitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:42
     */
    @Override
    public IPage<MacrobenthosQuantitative> listMacrobenthosQuantitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return macrobenthosQuantitativeMapper.listMacrobenthosQuantitative(page,stationName,  biologicalChineseName,  startDate,  endDate, reportId);
    }

}
