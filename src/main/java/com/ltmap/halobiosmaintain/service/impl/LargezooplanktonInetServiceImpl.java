package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.LargezooplanktonInetMapper;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQualitativeMapper;
import com.ltmap.halobiosmaintain.service.ILargezooplanktonInetService;
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

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<LargezooplanktonInet> lqw = Wrappers.lambdaQuery();
        lqw.eq(LargezooplanktonInet::getReportId,reportId);
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
    public List<LargezooplanktonInet> queryBiologicalType(String year, String voyage){
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        //去重
        largezooplanktonInetList=largezooplanktonInetList.stream().filter(x->x.getCategory()!=null).collect(Collectors.toList());

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
            largezooplanktonInetList = largezooplanktonInetList.stream().filter(x -> x.getCategory() != null).collect(Collectors.toList());
            if (largezooplanktonInetList.size() != 0) {
                list.add(largezooplanktonInetList.get(i).getCategory());
            }
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        largezooplanktonInetList=largezooplanktonInetList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(largezooplanktonInetList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < largezooplanktonInetList.size(); i++) {
                if (largezooplanktonInetList.get(i).getDensity() != null) {
                    density = density.add(largezooplanktonInetList.get(i).getDensity());
                }
            }
            if (largezooplanktonInetList.size() != 0) {
                density = density.divide(new BigDecimal(largezooplanktonInetList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        largezooplanktonInetList=largezooplanktonInetList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());
        if(largezooplanktonInetList.size()==0){
            resultMap.put("max",null);
            resultMap.put("min",null);
            resultMap.put("ave",null);
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
    public  HashMap<String,BigDecimal> queryBiologicalBiomass(String year, String voyage) {
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        largezooplanktonInetList=largezooplanktonInetList.stream().filter(x->x.getTotalBiomass()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(largezooplanktonInetList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal biomass = new BigDecimal(0);
            for (int i = 0; i < largezooplanktonInetList.size(); i++) {
                if (largezooplanktonInetList.get(i).getTotalBiomass() != null) {
                    biomass = biomass.add(largezooplanktonInetList.get(i).getTotalBiomass());
                }
            }
            if (largezooplanktonInetList.size() != 0) {
                biomass = biomass.divide(new BigDecimal(largezooplanktonInetList.size()), 2, RoundingMode.HALF_UP);
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
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,null);
        largezooplanktonInetList=largezooplanktonInetList.stream().filter(x->x.getTotalBiomass()!=null).collect(Collectors.toList());
        if(largezooplanktonInetList.size()==0){
            resultMap.put("max",null);
            resultMap.put("min",null);
            resultMap.put("ave",null);
            resultMap.put("result",new BigDecimal(0));
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
    public  HashMap<String,BigDecimal> queryBiologicalDensityByStation(String year, String voyage,Long stationId){
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year,voyage,stationId);
        largezooplanktonInetList=largezooplanktonInetList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(largezooplanktonInetList.size()==0){
            result.put("result",new BigDecimal(0));//0代表无值的情况
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < largezooplanktonInetList.size(); i++) {
                if (largezooplanktonInetList.get(i).getDensity() != null) {
                    density = density.add(largezooplanktonInetList.get(i).getDensity());
                }
            }
            if (largezooplanktonInetList.size() != 0) {
                density = density.divide(new BigDecimal(largezooplanktonInetList.size()), 2, RoundingMode.HALF_UP);
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
    public  HashMap<String,BigDecimal> queryBiologicalBiomassByStation(String year, String voyage,Long stationId) {
        List<LargezooplanktonInet> largezooplanktonInetList=largezooplanktonInetMapper.queryBiologicalType(year, voyage,stationId);
        HashMap<String,BigDecimal> result=new HashMap<>();
        if(largezooplanktonInetList.size()==0){
            result.put("result",new BigDecimal(0));//0代表无值的情况
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal biomass = new BigDecimal(0);
            for (int i = 0; i < largezooplanktonInetList.size(); i++) {
                if (largezooplanktonInetList.get(i).getTotalBiomass() != null) {
                    biomass = biomass.add(largezooplanktonInetList.get(i).getTotalBiomass());
                }
            }
            if (largezooplanktonInetList.size() != 0) {
                biomass = biomass.divide(new BigDecimal(largezooplanktonInetList.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",biomass);
        }
        return result;
    }

    /*
     * @Description:大型浮游动物（I型网）数据查询_数据管理
     * @Param largezooplanktonInet:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:47
     */
    @Override
    public IPage<LargezooplanktonInet> listLargezooplanktonInet(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return largezooplanktonInetMapper.listLargezooplanktonInet(page,stationName,  biologicalChineseName,  startDate,  endDate,reportId);
    }
}
