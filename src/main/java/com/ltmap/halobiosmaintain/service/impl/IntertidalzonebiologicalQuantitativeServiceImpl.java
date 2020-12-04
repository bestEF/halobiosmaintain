package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import java.util.*;

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
    public BigDecimal queryBiologicalDensity(String year, String voyage) {
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
            density = density.add(intertidalzonebiologicalQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()));
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
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
    public BigDecimal queryBiologicalBiomass(String year, String voyage) {
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
            biomass = biomass.add(intertidalzonebiologicalQuantitativeList.get(i).getBiomass());
        }
        biomass=biomass.subtract(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()));
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,null);
        if(intertidalzonebiologicalQuantitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year,voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
            density = density.add(intertidalzonebiologicalQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()));
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
        List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList=intertidalzonebiologicalQuantitativeMapper.queryBiologicalType(year, voyage,stationId);
        BigDecimal biomass = new BigDecimal(0);
        for (int i = 0; i < intertidalzonebiologicalQuantitativeList.size(); i++) {
            biomass = biomass.add(intertidalzonebiologicalQuantitativeList.get(i).getBiomass());
        }
        biomass=biomass.subtract(new BigDecimal(intertidalzonebiologicalQuantitativeList.size()));
        return biomass;
    }

    /*
     * @Description:潮间带生物数据
     * @Param intertidalzonebiologicalQuantitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:46
     */
    @Override
    public IPage<IntertidalzonebiologicalQuantitative> listIntertidalzonebiologicalQuantitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return intertidalzonebiologicalQuantitativeMapper.listIntertidalzonebiologicalQuantitative(page,stationName,  biologicalChineseName,  startDate,  endDate);
    }
}
