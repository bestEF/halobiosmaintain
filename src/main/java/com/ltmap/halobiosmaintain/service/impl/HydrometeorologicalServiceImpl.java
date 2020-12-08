package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.entity.work.Hydrometeorological;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import com.ltmap.halobiosmaintain.mapper.work.HydrometeorologicalMapper;
import com.ltmap.halobiosmaintain.service.IHydrometeorologicalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 水文气象表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class HydrometeorologicalServiceImpl extends ServiceImpl<HydrometeorologicalMapper, Hydrometeorological> implements IHydrometeorologicalService {

    @Resource
    private HydrometeorologicalMapper hydrometeorologicalMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<Hydrometeorological> lqw = Wrappers.lambdaQuery();
        lqw.eq(Hydrometeorological::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /*
     * @Description:水文气象变化范围_一年内
     * @Param year:
     * @Param voyage:
     * @Param element:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/3 15:01
     */
    @Override
    public HashMap<String, HashMap<String, BigDecimal>> hydrometeorologicalRangeOneYear(String year, String voyage, String element){
       List<Hydrometeorological> hydrometeorologicals = hydrometeorologicalMapper.hydrometeorologicalRangeOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
        if(Objects.equals(hydrometeorologicals.size(),0)){
            valueMap0.put("max",new BigDecimal(0));
            valueMap0.put("min",new BigDecimal(0));
            valueMap0.put("ave", new BigDecimal(0));
            resultMap.put("value", valueMap0);
            return resultMap;
        } else {
            switch (element) {
                case "watertem"://水温
                    HashMap<String, BigDecimal> tempvalueMap = new HashMap<>();
                    hydrometeorologicals = hydrometeorologicals.stream().filter(x -> x.getWatertemperature()!=null).collect(Collectors.toList());
                    if(Objects.equals(hydrometeorologicals.size(),0)){
                        valueMap0.put("max",new BigDecimal(0));
                        valueMap0.put("min",new BigDecimal(0));
                        valueMap0.put("ave", new BigDecimal(0));
                        resultMap.put("value", valueMap0);
                        return resultMap;
                    }
                    //求最大值
                    BigDecimal tempmax = hydrometeorologicals.stream().map(Hydrometeorological::getWatertemperature).max((x1, x2) -> x1.compareTo(x2)).get();
                    //求最小值
                    BigDecimal tempmin = hydrometeorologicals.stream().map(Hydrometeorological::getWatertemperature).min((x1, x2) -> x1.compareTo(x2)).get();
                    //求平均值
                    BigDecimal tempave = hydrometeorologicals.stream().map(Hydrometeorological::getWatertemperature).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(hydrometeorologicals.size()), 2, BigDecimal.ROUND_HALF_UP);
                    tempvalueMap.put("max", tempmax);
                    tempvalueMap.put("min", tempmin);
                    tempvalueMap.put("ave", tempave);
                    resultMap.put("value", tempvalueMap);
                    break;
                case "pellucidity"://透明度
                    HashMap<String, BigDecimal> pellucidityvalueMap = new HashMap<>();
                    hydrometeorologicals = hydrometeorologicals.stream().filter(x -> x.getPellucidity()!=null).collect(Collectors.toList());
                    if(Objects.equals(hydrometeorologicals.size(),0)){
                        valueMap0.put("max",new BigDecimal(0));
                        valueMap0.put("min",new BigDecimal(0));
                        valueMap0.put("ave", new BigDecimal(0));
                        resultMap.put("value", valueMap0);
                        return resultMap;
                    }
                    //求最大值
                    BigDecimal pelluciditymax = hydrometeorologicals.stream().map(Hydrometeorological::getPellucidity).max((x1, x2) -> x1.compareTo(x2)).get();
                    //求最小值
                    BigDecimal pelluciditymin = hydrometeorologicals.stream().map(Hydrometeorological::getPellucidity).min((x1, x2) -> x1.compareTo(x2)).get();
                    //求平均值
                    BigDecimal pellucidityave = hydrometeorologicals.stream().map(Hydrometeorological::getPellucidity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(hydrometeorologicals.size()), 2, BigDecimal.ROUND_HALF_UP);
                    pellucidityvalueMap.put("max", pelluciditymax);
                    pellucidityvalueMap.put("min", pelluciditymin);
                    pellucidityvalueMap.put("ave", pellucidityave);
                    resultMap.put("value", pellucidityvalueMap);
                    break;
            }
            return resultMap;
        }
    }

    /*
     * @Description:水文气象数据查询_数据管理
     * @Param hydrometeorological:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:46
     */
    @Override
    public IPage<Hydrometeorological> listHydrometeorological(Integer current,Integer size,String stationName, String startDate, String endDate){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return hydrometeorologicalMapper.listHydrometeorological(page,stationName,startDate,endDate);
    }
}
