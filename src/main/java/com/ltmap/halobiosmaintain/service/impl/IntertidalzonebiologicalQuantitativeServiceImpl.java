package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.FisheggQuantitativeMapper;
import com.ltmap.halobiosmaintain.mapper.work.IntertidalzonebiologicalQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.IIntertidalzonebiologicalQuantitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 潮间带生物定量表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class IntertidalzonebiologicalQuantitativeServiceImpl extends ServiceImpl<IntertidalzonebiologicalQuantitativeMapper, IntertidalzonebiologicalQuantitative> implements IIntertidalzonebiologicalQuantitativeService {

    @Resource
    private IntertidalzonebiologicalQuantitativeMapper intertidalzonebiologicalQuantitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<IntertidalzonebiologicalQuantitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(IntertidalzonebiologicalQuantitative::getReportId,reportId);
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
    public List<IntertidalzonebiologicalQuantitative> queryBiologicalType(String year, String voyage){
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeListNew= ListDistinctUtils.distinctIntertidalzonebiologicalQuantitativeByMap(intertidalzonebiologicalQuantitativeList);
        return intertidalzonebiologicalQuantitativeListNew;
    }
    /*
     * @Description:生物组成
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 10:20
     */
    @Override
    public HashMap<String,Integer> statisticTypeFromOneMap(String year, String voyage){
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        HashMap<String,Integer> intertidalzonebiologicalQuantitativeMap=new HashMap<String,Integer>();
        for (int i = 0; i <intertidalzonebiologicalQuantitativeList.size() ; i++) {
            list.add(intertidalzonebiologicalQuantitativeList.get(i).getBiologicalChineseName());
        }
        Set uniqueSet = new HashSet(list);
        for (Object temp:uniqueSet
        ) {
            intertidalzonebiologicalQuantitativeMap.put(temp.toString(),Collections.frequency(list, temp));
        }
        return intertidalzonebiologicalQuantitativeMap;
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
    public HashMap<String,BigDecimal>  queryBiologicalDensity(String year, String voyage) {
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        HashMap<String,BigDecimal> result=new HashMap<>();
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
                if (intertidalzonebiologicalQuantitativeList.get(i).getDensity() != null) {
                    density = density.add(intertidalzonebiologicalQuantitativeList.get(i).getDensity());
                }
            }
            if (intertidalzonebiologicalQuantitativeList.size() != 0) {
                density = density.divide(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()), 2, RoundingMode.HALF_UP);
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            resultMap.put("max",null);
            resultMap.put("min",null);
            resultMap.put("ave",null);
            return resultMap;
        }
        //求最大值
        BigDecimal max = intertidalzonebiologicalQuantitativeList.stream().map(IntertidalzonebiologicalQuantitative::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = intertidalzonebiologicalQuantitativeList.stream().map(IntertidalzonebiologicalQuantitative::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = intertidalzonebiologicalQuantitativeList.stream().map(IntertidalzonebiologicalQuantitative::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(intertidalzonebiologicalQuantitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
    public  HashMap<String,BigDecimal>  queryBiologicalBiomass(String year, String voyage) {
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        HashMap<String,BigDecimal> result=new HashMap<>();
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal biomass = new BigDecimal(0);
            for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
                if (intertidalzonebiologicalQuantitativeList.get(i).getBiomass() != null) {
                    biomass = biomass.add(intertidalzonebiologicalQuantitativeList.get(i).getBiomass());
                }
            }
            if (intertidalzonebiologicalQuantitativeList.size() != 0) {
                biomass = biomass.divide(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()), 2, RoundingMode.HALF_UP);
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeList.stream().filter(x->x.getBiomass()!=null).collect(Collectors.toList());
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            resultMap.put("max", null);
            resultMap.put("min",null);
            resultMap.put("ave",null);
            resultMap.put("result",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = intertidalzonebiologicalQuantitativeList.stream().map(IntertidalzonebiologicalQuantitative::getBiomass).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = intertidalzonebiologicalQuantitativeList.stream().map(IntertidalzonebiologicalQuantitative::getBiomass).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = intertidalzonebiologicalQuantitativeList.stream().map(IntertidalzonebiologicalQuantitative::getBiomass).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(intertidalzonebiologicalQuantitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,stationId);
        HashMap<String,BigDecimal> result=new HashMap<>();
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            result.put("result",new BigDecimal(0));//0代表无值的情况
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
                if (intertidalzonebiologicalQuantitativeList.get(i).getDensity() != null) {
                    density = density.add(intertidalzonebiologicalQuantitativeList.get(i).getDensity());
                }
            }
            if (intertidalzonebiologicalQuantitativeList.size() != 0) {
                density = density.divide(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()), 2, RoundingMode.HALF_UP);
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
    public HashMap<String,BigDecimal> queryBiologicalBiomassByStation(String year, String voyage,Long stationId) {
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year, voyage,stationId);
        HashMap<String,BigDecimal> result=new HashMap<>();
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            result.put("result",new BigDecimal(0));//0代表无值的情况
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal biomass = new BigDecimal(0);
            for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
                if (intertidalzonebiologicalQuantitativeList.get(i).getBiomass() != null) {
                    biomass = biomass.add(intertidalzonebiologicalQuantitativeList.get(i).getBiomass());
                }
            }
            if (intertidalzonebiologicalQuantitativeList.size() != 0) {
                biomass = biomass.divide(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()), 2, RoundingMode.HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",biomass);
        }
        return result;
    }

    /*
     * @Description:潮间带生物数据查询_数据管理
     * @Param intertidalzonebiologicalQuantitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:46
     */
    @Override
    public IPage<IntertidalzonebiologicalQuantitative> listIntertidalzonebiologicalQuantitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return intertidalzonebiologicalQuantitativeMapper.listIntertidalzonebiologicalQuantitative(page,stationName,  biologicalChineseName,  startDate,  endDate,reportId);
    }
}
