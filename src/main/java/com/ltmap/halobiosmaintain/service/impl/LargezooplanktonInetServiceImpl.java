package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.IntertidalzonebiologicalQuantitative;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.mapper.work.LargezooplanktonInetMapper;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQualitativeMapper;
import com.ltmap.halobiosmaintain.service.ILargezooplanktonInetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 大型浮游动物_I型网_表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class LargezooplanktonInetServiceImpl extends ServiceImpl<LargezooplanktonInetMapper, LargezooplanktonInet> implements ILargezooplanktonInetService {

    @Resource
    private LargezooplanktonInetMapper largezooplanktonInetMapper;

    /*
     * @Description:询生物种种类
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:51
     */
    @Override
    public List<LargezooplanktonInet> queryBiologicalType(String year, String voyage){
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<LargezooplanktonInet> largezooplanktonInetListNew= ListDistinctUtils.distinctLargezooplanktonInetByMap(largezooplanktonInetList);
        return largezooplanktonInetListNew;
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        for (int i = 0; i <largezooplanktonInetList.size() ; i++) {
            list.add(largezooplanktonInetList.get(i).getCategory());
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < largezooplanktonInetList.size(); i++) {
            density = density.add(largezooplanktonInetList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(largezooplanktonInetList.size()));
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        if(largezooplanktonInetList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = largezooplanktonInetList.stream().map(LargezooplanktonInet::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = largezooplanktonInetList.stream().map(LargezooplanktonInet::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = largezooplanktonInetList.stream().map(LargezooplanktonInet::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(largezooplanktonInetList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < largezooplanktonInetList.size(); i++) {
            biomass = biomass.add(largezooplanktonInetList.get(i).getTotalBiomass());
        }
        biomass=biomass.subtract(new BigDecimal(largezooplanktonInetList.size()));
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        if(largezooplanktonInetList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = largezooplanktonInetList.stream().map(LargezooplanktonInet::getTotalBiomass).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = largezooplanktonInetList.stream().map(LargezooplanktonInet::getTotalBiomass).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = largezooplanktonInetList.stream().map(LargezooplanktonInet::getTotalBiomass).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(largezooplanktonInetList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < largezooplanktonInetList.size(); i++) {
            density = density.add(largezooplanktonInetList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(largezooplanktonInetList.size()));
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year, voyage,stationId);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < largezooplanktonInetList.size(); i++) {
            biomass = biomass.add(largezooplanktonInetList.get(i).getTotalBiomass());
        }
        biomass=biomass.subtract(new BigDecimal(largezooplanktonInetList.size()));
        return biomass;
    }
}
