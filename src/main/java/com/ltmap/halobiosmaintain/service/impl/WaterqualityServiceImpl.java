package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import com.ltmap.halobiosmaintain.mapper.work.WaterqualityMapper;
import com.ltmap.halobiosmaintain.service.IWaterqualityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

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

    public HashMap<String,HashMap<String,BigDecimal>>  waterQualitystatisticOneYear(String year, String voyage,String element){
        List<Waterquality> waterqualities= waterqualityMapper.waterQualitystatisticOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();

        HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
        if(waterqualities.size()==0){
            valueMap0.put("max",new BigDecimal(0));
            valueMap0.put("min",new BigDecimal(0));
            valueMap0.put("ave",new BigDecimal(0));
            resultMap.put("value",valueMap0);
            return resultMap;
        }
        switch (element) {
            case "ad"://氨-氮
                HashMap<String, BigDecimal> aDvalueMap = new HashMap<>();
                //求最大值
                BigDecimal aDmax = waterqualities.stream().map(Waterquality::getaD).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aDmin = waterqualities.stream().map(Waterquality::getaD).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aDave = waterqualities.stream().map(Waterquality::getaD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                aDvalueMap.put("max", aDmax);
                aDvalueMap.put("min", aDmin);
                aDvalueMap.put("ave", aDave);
                resultMap.put("value",aDvalueMap);
                break;
            case "ass"://砷
                HashMap<String, BigDecimal> aSSvalueMap = new HashMap<>();
                //求最大值
                BigDecimal aSSmax = waterqualities.stream().map(Waterquality::getAss).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aSSmin = waterqualities.stream().map(Waterquality::getAss).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aSSave = waterqualities.stream().map(Waterquality::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                aSSvalueMap.put("max", aSSmax);
                aSSvalueMap.put("min", aSSmin);
                aSSvalueMap.put("ave", aSSave);
                resultMap.put("value",aSSvalueMap);
                break;
            case "cd"://镉
                HashMap<String, BigDecimal> cDvalueMap = new HashMap<>();
                //求最大值
                BigDecimal cDmax = waterqualities.stream().map(Waterquality::getCd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cDmin = waterqualities.stream().map(Waterquality::getCd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cDave = waterqualities.stream().map(Waterquality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                cDvalueMap.put("max", cDmax);
                cDvalueMap.put("min", cDmin);
                cDvalueMap.put("ave", cDave);
                resultMap.put("value",cDvalueMap);
                break;
            case "cod"://化学需氧量
                HashMap<String, BigDecimal> codvalueMap = new HashMap<>();
                //求最大值
                BigDecimal codmax = waterqualities.stream().map(Waterquality::getCod).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal codmin = waterqualities.stream().map(Waterquality::getCod).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal codave = waterqualities.stream().map(Waterquality::getCod).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                codvalueMap.put("max", codmax);
                codvalueMap.put("min", codmin);
                codvalueMap.put("ave", codave);
                resultMap.put("value",codvalueMap);
                break;
            case "cr"://铬
                HashMap<String, BigDecimal> crvalueMap = new HashMap<>();
                //求最大值
                BigDecimal crmax = waterqualities.stream().map(Waterquality::getCr).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal crmin = waterqualities.stream().map(Waterquality::getCr).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal crave = waterqualities.stream().map(Waterquality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                crvalueMap.put("max", crmax);
                crvalueMap.put("min", crmin);
                crvalueMap.put("ave", crave);
                resultMap.put("value",crvalueMap);
                break;
            case "cu"://铜
                HashMap<String, BigDecimal> cuvalueMap = new HashMap<>();
                //求最大值
                BigDecimal cumax = waterqualities.stream().map(Waterquality::getCu).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cumin = waterqualities.stream().map(Waterquality::getCu).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cuave = waterqualities.stream().map(Waterquality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                cuvalueMap.put("max", cumax);
                cuvalueMap.put("min", cumin);
                cuvalueMap.put("ave", cuave);
                resultMap.put("value",cuvalueMap);
                break;
            case "gsy"://硅酸盐
                HashMap<String, BigDecimal> gsyvalueMap = new HashMap<>();
                //求最大值
                BigDecimal gsymax = waterqualities.stream().map(Waterquality::getGsy).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal gsymin = waterqualities.stream().map(Waterquality::getGsy).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal gsyave = waterqualities.stream().map(Waterquality::getGsy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                gsyvalueMap.put("max", gsymax);
                gsyvalueMap.put("min", gsymin);
                gsyvalueMap.put("ave", gsyave);
                resultMap.put("value",gsyvalueMap);
                break;
            case "hg"://汞
                HashMap<String, BigDecimal> hgvalueMap = new HashMap<>();
                //求最大值
                BigDecimal hgmax = waterqualities.stream().map(Waterquality::getHg).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hgmin = waterqualities.stream().map(Waterquality::getHg).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hgave = waterqualities.stream().map(Waterquality::getHg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                hgvalueMap.put("max", hgmax);
                hgvalueMap.put("min", hgmin);
                hgvalueMap.put("ave", hgave);
                resultMap.put("value",hgvalueMap);
                break;
            case "dorjy"://溶解氧
                HashMap<String, BigDecimal> dorjyvalueMap = new HashMap<>();
                //求最大值
                BigDecimal dorjymax = waterqualities.stream().map(Waterquality::getDorjy).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal dorjymin = waterqualities.stream().map(Waterquality::getDorjy).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal dorjyave = waterqualities.stream().map(Waterquality::getDorjy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                dorjyvalueMap.put("max", dorjymax);
                dorjyvalueMap.put("min", dorjymin);
                dorjyvalueMap.put("ave", dorjyave);
                resultMap.put("value",dorjyvalueMap);
                break;
            case "hxlsy"://活性磷酸盐
                HashMap<String, BigDecimal> hxlsyvalueMap = new HashMap<>();
                //求最大值
                BigDecimal hxlsymax = waterqualities.stream().map(Waterquality::getHxlsy).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hxlsymin = waterqualities.stream().map(Waterquality::getHxlsy).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hxlsyave = waterqualities.stream().map(Waterquality::getHxlsy).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                hxlsyvalueMap.put("max", hxlsymax);
                hxlsyvalueMap.put("min", hxlsymin);
                hxlsyvalueMap.put("ave", hxlsyave);
                resultMap.put("value",hxlsyvalueMap);
                break;
            case "pb"://铅
                HashMap<String, BigDecimal> pbvalueMap = new HashMap<>();
                //求最大值
                BigDecimal pbmax = waterqualities.stream().map(Waterquality::getPb).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pbmin = waterqualities.stream().map(Waterquality::getPb).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pbave = waterqualities.stream().map(Waterquality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                pbvalueMap.put("max", pbmax);
                pbvalueMap.put("min", pbmin);
                pbvalueMap.put("ave", pbave);
                resultMap.put("value",pbvalueMap);
                break;
            case "ph"://ph
                HashMap<String, BigDecimal> phvalueMap = new HashMap<>();
                //求最大值
                BigDecimal phmax = waterqualities.stream().map(Waterquality::getPh).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal phmin = waterqualities.stream().map(Waterquality::getPh).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal phave = waterqualities.stream().map(Waterquality::getPh).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                phvalueMap.put("max", phmax);
                phvalueMap.put("min", phmin);
                phvalueMap.put("ave", phave);
                resultMap.put("value",phvalueMap);
                break;
            case "syl"://石油类
                HashMap<String, BigDecimal> sylvalueMap = new HashMap<>();
                //求最大值
                BigDecimal sylmax = waterqualities.stream().map(Waterquality::getSyl).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal sylmin = waterqualities.stream().map(Waterquality::getSyl).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal sylave = waterqualities.stream().map(Waterquality::getSyl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                sylvalueMap.put("max", sylmax);
                sylvalueMap.put("min", sylmin);
                sylvalueMap.put("ave", sylave);
                resultMap.put("value",sylvalueMap);
                break;
            case "tn"://总氮
                HashMap<String, BigDecimal> tnvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tnmax = waterqualities.stream().map(Waterquality::getTn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tnmin = waterqualities.stream().map(Waterquality::getTn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tnave = waterqualities.stream().map(Waterquality::getTn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                tnvalueMap.put("max", tnmax);
                tnvalueMap.put("min", tnmin);
                tnvalueMap.put("ave", tnave);
                resultMap.put("value",tnvalueMap);
                break;
            case "toc"://总有机碳
                HashMap<String, BigDecimal> tocvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tocmax = waterqualities.stream().map(Waterquality::getToc).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tocmin = waterqualities.stream().map(Waterquality::getToc).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tocave = waterqualities.stream().map(Waterquality::getToc).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                tocvalueMap.put("max", tocmax);
                tocvalueMap.put("min", tocmin);
                tocvalueMap.put("ave", tocave);
                resultMap.put("value",tocvalueMap);
                break;
            case "tp"://总磷
                HashMap<String, BigDecimal> tpvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tpmax = waterqualities.stream().map(Waterquality::getTp).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tpmin = waterqualities.stream().map(Waterquality::getTp).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tpave = waterqualities.stream().map(Waterquality::getTp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                tpvalueMap.put("max", tpmax);
                tpvalueMap.put("min", tpmin);
                tpvalueMap.put("ave", tpave);
                resultMap.put("value",tpvalueMap);
                break;
            case "xfw"://悬浮物
                HashMap<String, BigDecimal> xfwvalueMap = new HashMap<>();
                //求最大值
                BigDecimal xfwmax = waterqualities.stream().map(Waterquality::getXfw).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal xfwmin = waterqualities.stream().map(Waterquality::getXfw).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal xfwave = waterqualities.stream().map(Waterquality::getXfw).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                xfwvalueMap.put("max", xfwmax);
                xfwvalueMap.put("min", xfwmin);
                xfwvalueMap.put("ave", xfwave);
                resultMap.put("value",xfwvalueMap);
                break;
            case "xsyd"://硝酸盐-氮
                HashMap<String, BigDecimal> xsydvalueMap = new HashMap<>();
                //求最大值
                BigDecimal xsydmax = waterqualities.stream().map(Waterquality::getXsyD).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal xsydmin = waterqualities.stream().map(Waterquality::getXsyD).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal xsydave = waterqualities.stream().map(Waterquality::getXsyD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                xsydvalueMap.put("max", xsydmax);
                xsydvalueMap.put("min", xsydmin);
                xsydvalueMap.put("ave", xsydave);
                resultMap.put("value",xsydvalueMap);
                break;
            case "yd"://盐度
                HashMap<String, BigDecimal> ydvalueMap = new HashMap<>();
                //求最大值
                BigDecimal ydmax = waterqualities.stream().map(Waterquality::getYd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal ydmin = waterqualities.stream().map(Waterquality::getYd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal ydave = waterqualities.stream().map(Waterquality::getYd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                ydvalueMap.put("max", ydmax);
                ydvalueMap.put("min", ydmin);
                ydvalueMap.put("ave", ydave);
                resultMap.put("value",ydvalueMap);
                break;
            case "yjl"://有机磷
                HashMap<String, BigDecimal> yjlvalueMap = new HashMap<>();
                //求最大值
                BigDecimal yjlmax = waterqualities.stream().map(Waterquality::getYjl).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yjlmin = waterqualities.stream().map(Waterquality::getYjl).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yjlave = waterqualities.stream().map(Waterquality::getYjl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                yjlvalueMap.put("max", yjlmax);
                yjlvalueMap.put("min", yjlmin);
                yjlvalueMap.put("ave", yjlave);
                resultMap.put("value",yjlvalueMap);
                break;
            case "ylsa"://叶绿素-a
                HashMap<String, BigDecimal> ylsavalueMap = new HashMap<>();
                //求最大值
                BigDecimal ylsamax = waterqualities.stream().map(Waterquality::getYlsA).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal ylsamin = waterqualities.stream().map(Waterquality::getYlsA).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal ylsaave = waterqualities.stream().map(Waterquality::getYlsA).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                ylsavalueMap.put("max", ylsamax);
                ylsavalueMap.put("min", ylsamin);
                ylsavalueMap.put("ave", ylsaave);
                resultMap.put("value",ylsavalueMap);
                break;
            case "yxsyd"://亚硝酸盐-氮
                HashMap<String, BigDecimal> yxsydvalueMap = new HashMap<>();
                //求最大值
                BigDecimal yxsydmax = waterqualities.stream().map(Waterquality::getYxsyD).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yxsydmin = waterqualities.stream().map(Waterquality::getYxsyD).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yxsydave = waterqualities.stream().map(Waterquality::getYxsyD).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                yxsydvalueMap.put("max", yxsydmax);
                yxsydvalueMap.put("min", yxsydmin);
                yxsydvalueMap.put("ave", yxsydave);
                resultMap.put("value",yxsydvalueMap);
                break;
            case "zn"://锌
                HashMap<String, BigDecimal> znvalueMap = new HashMap<>();
                //求最大值
                BigDecimal znmax = waterqualities.stream().map(Waterquality::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal znmin = waterqualities.stream().map(Waterquality::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal znave = waterqualities.stream().map(Waterquality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(waterqualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                znvalueMap.put("max", znmax);
                znvalueMap.put("min", znmin);
                znvalueMap.put("ave", znave);
                resultMap.put("value",znvalueMap);
                break;
        }

        return resultMap;
    }
}
