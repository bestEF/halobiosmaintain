package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.WaterqualityMapper;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.IWaterqualityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.And;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 水质表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class WaterqualityServiceImpl extends ServiceImpl<WaterqualityMapper, Waterquality> implements IWaterqualityService {

    @Resource
    private WaterqualityMapper waterqualityMapper;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;


    //根据填报id删除对应所有数据
    @Override
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<Waterquality> lqw = Wrappers.lambdaQuery();
        lqw.eq(Waterquality::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /**
     * @Description:水质变化范围_一年内
     * @Param year:
     * @Param voyage:
     * @Param element:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/8 17:15
     */
    @Override
    public HashMap<String,HashMap<String,BigDecimal>>  waterQualitystatisticOneYear(String year, String voyage,String element){
        List<Waterquality> waterqualities= waterqualityMapper.waterQualitystatisticOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();

        if(waterqualities.size()==0){
            HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
            valueMap0.put("max",null);
            valueMap0.put("min",null);
            valueMap0.put("ave",null);
            resultMap.put("value",valueMap0);
            return resultMap;
        }
        switch (element) {
            case "ad"://氨-氮
                HashMap<String, BigDecimal> aDvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getaD()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal aDmax = waterqualities.stream().map(Waterquality::getaD).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aDmin = waterqualities.stream().map(Waterquality::getaD).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aDave = waterqualities.stream().map(Waterquality::getaD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                aDave = aDave.round(new MathContext(3, RoundingMode.HALF_UP));
                aDvalueMap.put("max", aDmax);
                aDvalueMap.put("min", aDmin);
                aDvalueMap.put("ave", aDave);
                resultMap.put("value",aDvalueMap);
                break;
            case "ass"://砷
                HashMap<String, BigDecimal> aSSvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getAss()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal aSSmax = waterqualities.stream().map(Waterquality::getAss).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aSSmin = waterqualities.stream().map(Waterquality::getAss).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aSSave = waterqualities.stream().map(Waterquality::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                aSSave = aSSave.round(new MathContext(3, RoundingMode.HALF_UP));
                aSSvalueMap.put("max", aSSmax);
                aSSvalueMap.put("min", aSSmin);
                aSSvalueMap.put("ave", aSSave);
                resultMap.put("value",aSSvalueMap);
                break;
            case "cd"://镉
                HashMap<String, BigDecimal> cDvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal cDmax = waterqualities.stream().map(Waterquality::getCd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cDmin = waterqualities.stream().map(Waterquality::getCd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cDave = waterqualities.stream().map(Waterquality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                cDave = cDave.round(new MathContext(3, RoundingMode.HALF_UP));

                cDvalueMap.put("max", cDmax);
                cDvalueMap.put("min", cDmin);
                cDvalueMap.put("ave", cDave);
                resultMap.put("value",cDvalueMap);
                break;
            case "cod"://化学需氧量
                HashMap<String, BigDecimal> codvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getCod()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal codmax = waterqualities.stream().map(Waterquality::getCod).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal codmin = waterqualities.stream().map(Waterquality::getCod).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal codave = waterqualities.stream().map(Waterquality::getCod).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                codave = codave.round(new MathContext(3, RoundingMode.HALF_UP));

                codvalueMap.put("max", codmax);
                codvalueMap.put("min", codmin);
                codvalueMap.put("ave", codave);
                resultMap.put("value",codvalueMap);
                break;
            case "cr"://铬
                HashMap<String, BigDecimal> crvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getCr()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal crmax = waterqualities.stream().map(Waterquality::getCr).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal crmin = waterqualities.stream().map(Waterquality::getCr).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal crave = waterqualities.stream().map(Waterquality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                crave = crave.round(new MathContext(3, RoundingMode.HALF_UP));

                crvalueMap.put("max", crmax);
                crvalueMap.put("min", crmin);
                crvalueMap.put("ave", crave);
                resultMap.put("value",crvalueMap);
                break;
            case "cu"://铜
                HashMap<String, BigDecimal> cuvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal cumax = waterqualities.stream().map(Waterquality::getCu).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cumin = waterqualities.stream().map(Waterquality::getCu).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cuave = waterqualities.stream().map(Waterquality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                cuave = cuave.round(new MathContext(3, RoundingMode.HALF_UP));

                cuvalueMap.put("max", cumax);
                cuvalueMap.put("min", cumin);
                cuvalueMap.put("ave", cuave);
                resultMap.put("value",cuvalueMap);
                break;
            case "gsy"://硅酸盐
                HashMap<String, BigDecimal> gsyvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getGsy()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal gsymax = waterqualities.stream().map(Waterquality::getGsy).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal gsymin = waterqualities.stream().map(Waterquality::getGsy).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal gsyave = waterqualities.stream().map(Waterquality::getGsy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                gsyave = gsyave.round(new MathContext(3, RoundingMode.HALF_UP));

                gsyvalueMap.put("max", gsymax);
                gsyvalueMap.put("min", gsymin);
                gsyvalueMap.put("ave", gsyave);
                resultMap.put("value",gsyvalueMap);
                break;
            case "hg"://汞
                HashMap<String, BigDecimal> hgvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getHg()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal hgmax = waterqualities.stream().map(Waterquality::getHg).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hgmin = waterqualities.stream().map(Waterquality::getHg).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hgave = waterqualities.stream().map(Waterquality::getHg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                hgave = hgave.round(new MathContext(3, RoundingMode.HALF_UP));

                hgvalueMap.put("max", hgmax);
                hgvalueMap.put("min", hgmin);
                hgvalueMap.put("ave", hgave);
                resultMap.put("value",hgvalueMap);
                break;
            case "dorjy"://溶解氧
                HashMap<String, BigDecimal> dorjyvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getDorjy()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal dorjymax = waterqualities.stream().map(Waterquality::getDorjy).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal dorjymin = waterqualities.stream().map(Waterquality::getDorjy).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal dorjyave = waterqualities.stream().map(Waterquality::getDorjy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                dorjyave = dorjyave.round(new MathContext(3, RoundingMode.HALF_UP));

                dorjyvalueMap.put("max", dorjymax);
                dorjyvalueMap.put("min", dorjymin);
                dorjyvalueMap.put("ave", dorjyave);
                resultMap.put("value",dorjyvalueMap);
                break;
            case "hxlsy"://活性磷酸盐
                HashMap<String, BigDecimal> hxlsyvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getHxlsy()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal hxlsymax = waterqualities.stream().map(Waterquality::getHxlsy).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hxlsymin = waterqualities.stream().map(Waterquality::getHxlsy).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hxlsyave = waterqualities.stream().map(Waterquality::getHxlsy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                hxlsyave = hxlsyave.round(new MathContext(3, RoundingMode.HALF_UP));

                hxlsyvalueMap.put("max", hxlsymax);
                hxlsyvalueMap.put("min", hxlsymin);
                hxlsyvalueMap.put("ave", hxlsyave);
                resultMap.put("value",hxlsyvalueMap);
                break;
            case "pb"://铅
                HashMap<String, BigDecimal> pbvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal pbmax = waterqualities.stream().map(Waterquality::getPb).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pbmin = waterqualities.stream().map(Waterquality::getPb).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pbave = waterqualities.stream().map(Waterquality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                pbave = pbave.round(new MathContext(3, RoundingMode.HALF_UP));

                pbvalueMap.put("max", pbmax);
                pbvalueMap.put("min", pbmin);
                pbvalueMap.put("ave", pbave);
                resultMap.put("value",pbvalueMap);
                break;
            case "ph"://ph
                HashMap<String, BigDecimal> phvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getPh()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal phmax = waterqualities.stream().map(Waterquality::getPh).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal phmin = waterqualities.stream().map(Waterquality::getPh).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal phave = waterqualities.stream().map(Waterquality::getPh).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                phave = phave.round(new MathContext(3, RoundingMode.HALF_UP));

                phvalueMap.put("max", phmax);
                phvalueMap.put("min", phmin);
                phvalueMap.put("ave", phave);
                resultMap.put("value",phvalueMap);
                break;
            case "syl"://石油类
                HashMap<String, BigDecimal> sylvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getSyl()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal sylmax = waterqualities.stream().map(Waterquality::getSyl).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal sylmin = waterqualities.stream().map(Waterquality::getSyl).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal sylave = waterqualities.stream().map(Waterquality::getSyl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                sylave = sylave.round(new MathContext(3, RoundingMode.HALF_UP));

                sylvalueMap.put("max", sylmax);
                sylvalueMap.put("min", sylmin);
                sylvalueMap.put("ave", sylave);
                resultMap.put("value",sylvalueMap);
                break;
            case "tn"://总氮
                HashMap<String, BigDecimal> tnvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getTn()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal tnmax = waterqualities.stream().map(Waterquality::getTn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tnmin = waterqualities.stream().map(Waterquality::getTn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tnave = waterqualities.stream().map(Waterquality::getTn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                tnave = tnave.round(new MathContext(3, RoundingMode.HALF_UP));

                tnvalueMap.put("max", tnmax);
                tnvalueMap.put("min", tnmin);
                tnvalueMap.put("ave", tnave);
                resultMap.put("value",tnvalueMap);
                break;
            case "toc"://总有机碳
                HashMap<String, BigDecimal> tocvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getToc()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal tocmax = waterqualities.stream().map(Waterquality::getToc).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tocmin = waterqualities.stream().map(Waterquality::getToc).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tocave = waterqualities.stream().map(Waterquality::getToc).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                tocave = tocave.round(new MathContext(3, RoundingMode.HALF_UP));

                tocvalueMap.put("max", tocmax);
                tocvalueMap.put("min", tocmin);
                tocvalueMap.put("ave", tocave);
                resultMap.put("value",tocvalueMap);
                break;
            case "tp"://总磷
                HashMap<String, BigDecimal> tpvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getTp()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal tpmax = waterqualities.stream().map(Waterquality::getTp).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tpmin = waterqualities.stream().map(Waterquality::getTp).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tpave = waterqualities.stream().map(Waterquality::getTp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                tpave = tpave.round(new MathContext(3, RoundingMode.HALF_UP));

                tpvalueMap.put("max", tpmax);
                tpvalueMap.put("min", tpmin);
                tpvalueMap.put("ave", tpave);
                resultMap.put("value",tpvalueMap);
                break;
            case "xfw"://悬浮物
                HashMap<String, BigDecimal> xfwvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getXfw()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal xfwmax = waterqualities.stream().map(Waterquality::getXfw).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal xfwmin = waterqualities.stream().map(Waterquality::getXfw).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal xfwave = waterqualities.stream().map(Waterquality::getXfw).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 3, BigDecimal.ROUND_HALF_UP);
                xfwvalueMap.put("max", xfwmax);
                xfwvalueMap.put("min", xfwmin);
                xfwvalueMap.put("ave", xfwave);
                resultMap.put("value",xfwvalueMap);
                break;
            case "xsyd"://硝酸盐-氮
                HashMap<String, BigDecimal> xsydvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getXsyD()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal xsydmax = waterqualities.stream().map(Waterquality::getXsyD).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal xsydmin = waterqualities.stream().map(Waterquality::getXsyD).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal xsydave = waterqualities.stream().map(Waterquality::getXsyD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                xsydave = xsydave.round(new MathContext(3, RoundingMode.HALF_UP));

                xsydvalueMap.put("max", xsydmax);
                xsydvalueMap.put("min", xsydmin);
                xsydvalueMap.put("ave", xsydave);
                resultMap.put("value",xsydvalueMap);
                break;
            case "yd"://盐度
                HashMap<String, BigDecimal> ydvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getYd()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal ydmax = waterqualities.stream().map(Waterquality::getYd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal ydmin = waterqualities.stream().map(Waterquality::getYd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal ydave = waterqualities.stream().map(Waterquality::getYd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 3, BigDecimal.ROUND_HALF_UP);
                ydvalueMap.put("max", ydmax);
                ydvalueMap.put("min", ydmin);
                ydvalueMap.put("ave", ydave);
                resultMap.put("value",ydvalueMap);
                break;
            case "yjl"://有机磷
                HashMap<String, BigDecimal> yjlvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getYjl()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal yjlmax = waterqualities.stream().map(Waterquality::getYjl).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yjlmin = waterqualities.stream().map(Waterquality::getYjl).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yjlave = waterqualities.stream().map(Waterquality::getYjl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                yjlave = yjlave.round(new MathContext(3, RoundingMode.HALF_UP));

                yjlvalueMap.put("max", yjlmax);
                yjlvalueMap.put("min", yjlmin);
                yjlvalueMap.put("ave", yjlave);
                resultMap.put("value",yjlvalueMap);
                break;
            case "ylsa"://叶绿素-a
                HashMap<String, BigDecimal> ylsavalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getYlsA()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal ylsamax = waterqualities.stream().map(Waterquality::getYlsA).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal ylsamin = waterqualities.stream().map(Waterquality::getYlsA).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal ylsaave = waterqualities.stream().map(Waterquality::getYlsA).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                ylsaave = ylsaave.round(new MathContext(3, RoundingMode.HALF_UP));

                ylsavalueMap.put("max", ylsamax);
                ylsavalueMap.put("min", ylsamin);
                ylsavalueMap.put("ave", ylsaave);
                resultMap.put("value",ylsavalueMap);
                break;
            case "yxsyd"://亚硝酸盐-氮
                HashMap<String, BigDecimal> yxsydvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getYxsyD()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal yxsydmax = waterqualities.stream().map(Waterquality::getYxsyD).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yxsydmin = waterqualities.stream().map(Waterquality::getYxsyD).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yxsydave = waterqualities.stream().map(Waterquality::getYxsyD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                yxsydave = yxsydave.round(new MathContext(3, RoundingMode.HALF_UP));

                yxsydvalueMap.put("max", yxsydmax);
                yxsydvalueMap.put("min", yxsydmin);
                yxsydvalueMap.put("ave", yxsydave);
                resultMap.put("value",yxsydvalueMap);
                break;
            case "zn"://锌
                HashMap<String, BigDecimal> znvalueMap = new HashMap<>();
                waterqualities = waterqualities.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
                if(waterqualities.size()==0){
                    HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal znmax = waterqualities.stream().map(Waterquality::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal znmin = waterqualities.stream().map(Waterquality::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal znave = waterqualities.stream().map(Waterquality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 10, BigDecimal.ROUND_HALF_UP);
                znave = znave.round(new MathContext(3, RoundingMode.HALF_UP));

                znvalueMap.put("max", znmax);
                znvalueMap.put("min", znmin);
                znvalueMap.put("ave", znave);
                resultMap.put("value",znvalueMap);
                break;
            default:
                HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
                valueMap0.put("max",null);
                valueMap0.put("min",null);
                valueMap0.put("ave",null);
                resultMap.put("value",valueMap0);
                return resultMap;
        }

        return resultMap;
    }



    /**
     * @Description:水质评价标准等级统计
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/7 15:43
     */
    @Override
    public HashMap<String,HashMap<String,BigDecimal>>  waterQualityOrder(String year, String voyage){
        List<Waterquality> waterqualities= waterqualityMapper.waterQualitystatisticOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        BigDecimal order1=new BigDecimal(0);
        BigDecimal order2=new BigDecimal(0);
        BigDecimal order3=new BigDecimal(0);
        BigDecimal order4=new BigDecimal(0);
        if(waterqualities.size()==0){
            HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
            valueMap0.put("第一类", null);
            valueMap0.put("第二类", null);
            valueMap0.put("第三类", null);
            valueMap0.put("第四类", null);
            resultMap.put("value",valueMap0);
            return resultMap;
        }
        //水质标准类别占比
        List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfobyDataType(year, voyage,"水质");

        for (int i = 0; i < monitorStationInfos.size(); i++) {
            List<Waterquality> waterqualities1 = waterqualityMapper.waterQualitystatisticStationOneYear(year, voyage, monitorStationInfos.get(i).getStationId());

            //求平均值
            //悬浮物
            waterqualities1 = waterqualities1.stream().filter(x -> x.getXfw()!=null).collect(Collectors.toList());
            BigDecimal xfw=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 xfw = waterqualities1.stream().map(Waterquality::getXfw).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 3, BigDecimal.ROUND_HALF_UP);
            }
            //ph
            waterqualities1 = waterqualities1.stream().filter(x -> x.getPh()!=null).collect(Collectors.toList());
            BigDecimal ph=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 ph = waterqualities1.stream().map(Waterquality::getPh).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                ph = ph.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //溶解氧
            waterqualities1 = waterqualities1.stream().filter(x -> x.getDorjy()!=null).collect(Collectors.toList());
            BigDecimal dorjy=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 dorjy = waterqualities1.stream().map(Waterquality::getDorjy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                dorjy = dorjy.round(new MathContext(3, RoundingMode.HALF_UP));
            }
            //化学需氧量
            waterqualities1 = waterqualities1.stream().filter(x -> x.getCod()!=null).collect(Collectors.toList());
            BigDecimal cod=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 cod = waterqualities1.stream().map(Waterquality::getCod).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                cod = cod.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //氨氮
            waterqualities1 = waterqualities1.stream().filter(x -> x.getaD()!=null).collect(Collectors.toList());
            BigDecimal ad=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 ad = waterqualities1.stream().map(Waterquality::getaD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                ad = ad.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //硝酸盐氮
            waterqualities1 = waterqualities1.stream().filter(x -> x.getXsyD()!=null).collect(Collectors.toList());
            BigDecimal xsyd=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 xsyd = waterqualities1.stream().map(Waterquality::getXsyD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                xsyd = xsyd.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //亚硝酸盐氮
            waterqualities1 = waterqualities1.stream().filter(x -> x.getYxsyD()!=null).collect(Collectors.toList());
            BigDecimal yxsyd=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 yxsyd = waterqualities1.stream().map(Waterquality::getYxsyD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                yxsyd = yxsyd.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //无机氮=氨氮+硝酸盐氮+亚硝酸盐氮
            BigDecimal wjd=ad.add(xsyd).add(yxsyd);
            wjd = wjd.round(new MathContext(3, RoundingMode.HALF_UP));

            //活性磷酸盐
            waterqualities1 = waterqualities1.stream().filter(x -> x.getHxlsy()!=null).collect(Collectors.toList());
            BigDecimal hxlsy=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 hxlsy = waterqualities1.stream().map(Waterquality::getHxlsy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                hxlsy = hxlsy.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //汞
            waterqualities1 = waterqualities1.stream().filter(x -> x.getHg()!=null).collect(Collectors.toList());
            BigDecimal hg=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 hg = waterqualities1.stream().map(Waterquality::getHg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                hg = hg.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //镉
            waterqualities1 = waterqualities1.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
            BigDecimal cd=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 cd = waterqualities1.stream().map(Waterquality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                cd = cd.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //铬
            waterqualities1 = waterqualities1.stream().filter(x -> x.getCr()!=null).collect(Collectors.toList());
            BigDecimal cr=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 cr = waterqualities1.stream().map(Waterquality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                cr = cr.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //铅
            waterqualities1 = waterqualities1.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
            BigDecimal pb=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 pb = waterqualities1.stream().map(Waterquality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                pb = pb.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //砷
            waterqualities1 = waterqualities1.stream().filter(x -> x.getAss()!=null).collect(Collectors.toList());
            BigDecimal ass=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 ass = waterqualities1.stream().map(Waterquality::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                ass = ass.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //铜
            waterqualities1 = waterqualities1.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
            BigDecimal cu=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 cu = waterqualities1.stream().map(Waterquality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
            }
            //锌
            waterqualities1 = waterqualities1.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
            BigDecimal zn=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 zn = waterqualities1.stream().map(Waterquality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                zn = zn.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //石油类
            waterqualities1 = waterqualities1.stream().filter(x -> x.getSyl()!=null).collect(Collectors.toList());
            BigDecimal syl=new BigDecimal(0);
            if(waterqualities1.size()!=0) {
                 syl = waterqualities1.stream().map(Waterquality::getSyl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities1.size()), 10, BigDecimal.ROUND_HALF_UP);
                syl = syl.round(new MathContext(3, RoundingMode.HALF_UP));

            }
            //按海水水质标准计算等级
            //悬浮物
            int xfwint1 = xfw.compareTo(new BigDecimal(10));
            int xfwint2 = xfw.compareTo(new BigDecimal(100));
            int xfwint3 = xfw.compareTo(new BigDecimal(150));
            //ph
            int phint1 = ph.compareTo(new BigDecimal(7.8));
            int phint2 = ph.compareTo(new BigDecimal(8.5));
            int phint3 = ph.compareTo(new BigDecimal(6.8));
            int phint4 = ph.compareTo(new BigDecimal(8.8));
            //溶解氧
            int dorjyint1 = dorjy.compareTo(new BigDecimal(6));
            int dorjyint2 = dorjy.compareTo(new BigDecimal(5));
            int dorjyint3 = dorjy.compareTo(new BigDecimal(4));
            int dorjyint4 = dorjy.compareTo(new BigDecimal(3));
            //化学需氧量
            int codint1 = cod.compareTo(new BigDecimal(2));
            int codint2 = cod.compareTo(new BigDecimal(3));
            int codint3 = cod.compareTo(new BigDecimal(4));
            int codint4 = cod.compareTo(new BigDecimal(5));
            //无机氮
            int wjdint1 = wjd.compareTo(new BigDecimal(0.2));
            int wjdint2 = wjd.compareTo(new BigDecimal(0.3));
            int wjdint3 = wjd.compareTo(new BigDecimal(0.4));
            int wjdint4 = wjd.compareTo(new BigDecimal(0.5));
            //活性磷酸盐
            int hxlsyint1 = hxlsy.compareTo(new BigDecimal(0.015));
            int hxlsyint2 = hxlsy.compareTo(new BigDecimal(0.03));
            int hxlsyint3 = hxlsy.compareTo(new BigDecimal(0.045));
            //汞
            int hgint1 = hg.compareTo(new BigDecimal(0.00005));
            int hgint2 = hg.compareTo(new BigDecimal(0.0002));
            int hgint3 = hg.compareTo(new BigDecimal(0.0005));
            //镉
            int cdint1 = cd.compareTo(new BigDecimal(0.001));
            int cdint2 = cd.compareTo(new BigDecimal(0.005));
            int cdint3 = cd.compareTo(new BigDecimal(0.01));
            //铬
            int crint1 = cr.compareTo(new BigDecimal(0.05));
            int crint2 = cr.compareTo(new BigDecimal(0.1));
            int crint3 = cr.compareTo(new BigDecimal(0.2));
            int crint4 = cr.compareTo(new BigDecimal(0.5));
            //铅
            int pbint1 = pb.compareTo(new BigDecimal(0.001));
            int pbint2 = pb.compareTo(new BigDecimal(0.005));
            int pbint3 = pb.compareTo(new BigDecimal(0.01));
            int pbint4 = pb.compareTo(new BigDecimal(0.05));
            //砷
            int assint1 = ass.compareTo(new BigDecimal(0.02));
            int assint2 = ass.compareTo(new BigDecimal(0.03));
            int assint3 = ass.compareTo(new BigDecimal(0.05));
            //铜
            int cuint1 = cu.compareTo(new BigDecimal(0.005));
            int cuint2 = cu.compareTo(new BigDecimal(0.01));
            int cuint3 = cu.compareTo(new BigDecimal(0.05));
            //锌
            int znint1 = zn.compareTo(new BigDecimal(0.02));
            int znint2 = zn.compareTo(new BigDecimal(0.05));
            int znint3 = zn.compareTo(new BigDecimal(0.1));
            int znint4 = zn.compareTo(new BigDecimal(0.5));
            //石油类
            int sylint1 = zn.compareTo(new BigDecimal(0.05));
            int sylint2 = zn.compareTo(new BigDecimal(0.3));
            int sylint3 = zn.compareTo(new BigDecimal(0.5));

            //第一类
            if ((xfwint1 <= 0) && (phint1 >= 0) && (phint2 <= 0) && (dorjyint1 > 0) && (codint1 <= 0) && (wjdint1 <= 0) && (hxlsyint1 <= 0) && (hgint1 <= 0) && (cdint1 <= 0) && (pbint1 <= 0) && (crint1 <= 0) && (assint1 <= 0) && (cuint1 <= 0) && (znint1 <= 0) && (sylint1 <= 0)) {
                order1 = order1.add(new BigDecimal(1));
            }
            //第二类
            else if ((xfwint1 <= 0) && (phint1 >= 0) && (phint2 <= 0) && (dorjyint2 > 0) && (codint2 <= 0) && (wjdint2 <= 0) && (hxlsyint2 <= 0) && (hgint2 <= 0) && (cdint2 <= 0) && (pbint2 <= 0) && (crint2 <= 0) && (assint2 <= 0) && (cuint2 <= 0) && (znint2 <= 0) && (sylint1 <= 0)) {
                order2 = order2.add(new BigDecimal(1));
            }
            //第三类
            else if ((xfwint2 <= 0) && (phint3 >= 0) && (phint4 <= 0) && (dorjyint3 > 0) && (codint3 <= 0) && (wjdint3 <= 0) && (hxlsyint2 <= 0) && (hgint2 <= 0) && (cdint3 <= 0) && (pbint3 <= 0) && (crint3 <= 0) && (assint3 <= 0) && (cuint3 <= 0) && (znint3 <= 0) && (sylint2 <= 0)) {
                order3 = order3.add(new BigDecimal(1));
            }
            //第四类
            else if ((xfwint3 <= 0) && (phint3 >= 0) && (phint4 <= 0) && (dorjyint4 > 0) && (codint4 <= 0) && (wjdint4 <= 0) && (hxlsyint3 <= 0) && (hgint3 <= 0) && (cdint3 <= 0) && (pbint4 <= 0) && (crint4 <= 0) && (assint3 <= 0) && (cuint3 <= 0) && (znint4 <= 0) && (sylint3 <= 0)) {
                order4 = order4.add(new BigDecimal(1));
            } else {
//                HashMap<String, BigDecimal> valueMap1 = new HashMap<>();
//                resultMap.put("value",valueMap1);
            }
        }
        HashMap<String, BigDecimal> valueMap1 = new HashMap<>();
        valueMap1.put("第一类", order1);
        valueMap1.put("第二类", order2);
        valueMap1.put("第三类", order3);
        valueMap1.put("第四类", order4);
        resultMap.put("value", valueMap1);

        return resultMap;
    }

    /*
     * @Description:水质数据查询_数据管理
     * @Param waterquality:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:53
     */
    @Override
    public IPage<Waterquality> listWaterquality(Integer current,Integer size,String stationName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return waterqualityMapper.listWaterquality(page,stationName,  startDate,  endDate, reportId);
    }
}
