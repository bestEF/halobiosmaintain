package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import com.ltmap.halobiosmaintain.mapper.work.SedimentMapper;
import com.ltmap.halobiosmaintain.service.ISedimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 沉积物表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class SedimentServiceImpl extends ServiceImpl<SedimentMapper, Sediment> implements ISedimentService {

    @Resource
    private SedimentMapper sedimentMapper;

    public HashMap<String,HashMap<String, BigDecimal>>  sedimentstatisticOneYear(String year, String voyage, String element){
        List<Sediment> sediments= sedimentMapper.sedimentstatisticOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
        if(sediments.size()==0){
            valueMap0.put("max",new BigDecimal(0));
            valueMap0.put("min",new BigDecimal(0));
            valueMap0.put("ave",new BigDecimal(0));
            resultMap.put("value",valueMap0);
            return resultMap;
        }
        switch (element) {
            case "cd"://镉
                HashMap<String, BigDecimal> cDvalueMap = new HashMap<>();
                //求最大值
                BigDecimal cDmax = sediments.stream().map(Sediment::getCd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cDmin = sediments.stream().map(Sediment::getCd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cDave = sediments.stream().map(Sediment::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                cDvalueMap.put("max", cDmax);
                cDvalueMap.put("min", cDmin);
                cDvalueMap.put("ave", cDave);
                resultMap.put("value",cDvalueMap);
                break;
            case "lhw"://硫化物
                HashMap<String, BigDecimal> codvalueMap = new HashMap<>();
                //求最大值
                BigDecimal codmax = sediments.stream().map(Sediment::getLhw).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal codmin = sediments.stream().map(Sediment::getLhw).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal codave = sediments.stream().map(Sediment::getLhw).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                codvalueMap.put("max", codmax);
                codvalueMap.put("min", codmin);
                codvalueMap.put("ave", codave);
                resultMap.put("value",codvalueMap);
                break;
            case "tcr"://总铬
                HashMap<String, BigDecimal> crvalueMap = new HashMap<>();
                //求最大值
                BigDecimal crmax = sediments.stream().map(Sediment::getTcr).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal crmin = sediments.stream().map(Sediment::getTcr).min((x1, x2) -> x1.compareTo(x2)).get();
                //Sediment
                BigDecimal crave = sediments.stream().map(Sediment::getTcr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                crvalueMap.put("max", crmax);
                crvalueMap.put("min", crmin);
                crvalueMap.put("ave", crave);
                resultMap.put("value",crvalueMap);
                break;
            case "cu"://铜
                HashMap<String, BigDecimal> cuvalueMap = new HashMap<>();
                //求最大值
                BigDecimal cumax = sediments.stream().map(Sediment::getCu).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cumin = sediments.stream().map(Sediment::getCu).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cuave = sediments.stream().map(Sediment::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                cuvalueMap.put("max", cumax);
                cuvalueMap.put("min", cumin);
                cuvalueMap.put("ave", cuave);
                resultMap.put("value",cuvalueMap);
                break;
            case "yjt"://有机碳
                HashMap<String, BigDecimal> gsyvalueMap = new HashMap<>();
                //求最大值
                BigDecimal gsymax = sediments.stream().map(Sediment::getYjt).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal gsymin = sediments.stream().map(Sediment::getYjt).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal gsyave = sediments.stream().map(Sediment::getYjt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                gsyvalueMap.put("max", gsymax);
                gsyvalueMap.put("min", gsymin);
                gsyvalueMap.put("ave", gsyave);
                resultMap.put("value",gsyvalueMap);
                break;
            case "hg"://汞
                HashMap<String, BigDecimal> hgvalueMap = new HashMap<>();
                //求最大值
                BigDecimal hgmax = sediments.stream().map(Sediment::getHg).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hgmin = sediments.stream().map(Sediment::getHg).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hgave = sediments.stream().map(Sediment::getHg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                hgvalueMap.put("max", hgmax);
                hgvalueMap.put("min", hgmin);
                hgvalueMap.put("ave", hgave);
                resultMap.put("value",hgvalueMap);
                break;
            case "pb"://铅
                HashMap<String, BigDecimal> pbvalueMap = new HashMap<>();
                //求最大值
                BigDecimal pbmax = sediments.stream().map(Sediment::getPb).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pbmin = sediments.stream().map(Sediment::getPb).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pbave = sediments.stream().map(Sediment::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                pbvalueMap.put("max", pbmax);
                pbvalueMap.put("min", pbmin);
                pbvalueMap.put("ave", pbave);
                resultMap.put("value",pbvalueMap);
                break;
            case "syl"://石油类
                HashMap<String, BigDecimal> sylvalueMap = new HashMap<>();
                //求最大值
                BigDecimal sylmax = sediments.stream().map(Sediment::getSyl).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal sylmin = sediments.stream().map(Sediment::getSyl).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal sylave = sediments.stream().map(Sediment::getSyl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                sylvalueMap.put("max", sylmax);
                sylvalueMap.put("min", sylmin);
                sylvalueMap.put("ave", sylave);
                resultMap.put("value",sylvalueMap);
                break;
            case "tn"://总氮
                HashMap<String, BigDecimal> tnvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tnmax = sediments.stream().map(Sediment::getTn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tnmin = sediments.stream().map(Sediment::getTn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tnave = sediments.stream().map(Sediment::getTn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                tnvalueMap.put("max", tnmax);
                tnvalueMap.put("min", tnmin);
                tnvalueMap.put("ave", tnave);
                resultMap.put("value",tnvalueMap);
                break;
            case "tp"://总磷
                HashMap<String, BigDecimal> tpvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tpmax = sediments.stream().map(Sediment::getTp).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tpmin = sediments.stream().map(Sediment::getTp).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tpave = sediments.stream().map(Sediment::getTp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                tpvalueMap.put("max", tpmax);
                tpvalueMap.put("min", tpmin);
                tpvalueMap.put("ave", tpave);
                resultMap.put("value",tpvalueMap);
                break;

            case "eh"://Eh
                HashMap<String, BigDecimal> yxsydvalueMap = new HashMap<>();
                //求最大值
                BigDecimal yxsydmax = sediments.stream().map(Sediment::getEh).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yxsydmin = sediments.stream().map(Sediment::getEh).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yxsydave = sediments.stream().map(Sediment::getEh).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                yxsydvalueMap.put("max", yxsydmax);
                yxsydvalueMap.put("min", yxsydmin);
                yxsydvalueMap.put("ave", yxsydave);
                resultMap.put("value",yxsydvalueMap);
                break;
            case "ass"://砷
                HashMap<String, BigDecimal> aSSvalueMap = new HashMap<>();
                //求最大值
                BigDecimal aSSmax = sediments.stream().map(Sediment::getAss).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aSSmin = sediments.stream().map(Sediment::getAss).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aSSave = sediments.stream().map(Sediment::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                aSSvalueMap.put("max", aSSmax);
                aSSvalueMap.put("min", aSSmin);
                aSSvalueMap.put("ave", aSSave);
                resultMap.put("value",aSSvalueMap);
                break;
            case "zn"://锌
                HashMap<String, BigDecimal> znvalueMap = new HashMap<>();
                //求最大值
                BigDecimal znmax = sediments.stream().map(Sediment::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal znmin = sediments.stream().map(Sediment::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal znave = sediments.stream().map(Sediment::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments.size()), 2, BigDecimal.ROUND_HALF_UP);
                znvalueMap.put("max", znmax);
                znvalueMap.put("min", znmin);
                znvalueMap.put("ave", znave);
                resultMap.put("value",znvalueMap);
                break;
        }
        return resultMap;
    }
}
