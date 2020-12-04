package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.mapper.work.BiologicalQualityMapper;
import com.ltmap.halobiosmaintain.service.IBiologicalQualityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 生物质量表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class BiologicalQualityServiceImpl extends ServiceImpl<BiologicalQualityMapper, BiologicalQuality> implements IBiologicalQualityService {

    @Resource
    private BiologicalQualityMapper biologicalQualityMapper;

    @Override
    public HashMap<String, HashMap<String, BigDecimal>> biologicalQualityRangeOneYear(String year, String voyage, String element){

        List<BiologicalQuality> biologicalQualities= biologicalQualityMapper.biologicalQualityRangeOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
        if(biologicalQualities.size()==0){
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
                BigDecimal cDmax = biologicalQualities.stream().map(BiologicalQuality::getCd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cDmin = biologicalQualities.stream().map(BiologicalQuality::getCd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cDave = biologicalQualities.stream().map(BiologicalQuality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                cDvalueMap.put("max", cDmax);
                cDvalueMap.put("min", cDmin);
                cDvalueMap.put("ave", cDave);
                resultMap.put("value",cDvalueMap);
                break;
            case "sixsixsix"://六六六
                HashMap<String, BigDecimal> codvalueMap = new HashMap<>();
                //求最大值
                BigDecimal codmax = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal codmin = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal codave = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                codvalueMap.put("max", codmax);
                codvalueMap.put("min", codmin);
                codvalueMap.put("ave", codave);
                resultMap.put("value",codvalueMap);
                break;
            case "cr"://铬
                HashMap<String, BigDecimal> crvalueMap = new HashMap<>();
                //求最大值
                BigDecimal crmax = biologicalQualities.stream().map(BiologicalQuality::getCr).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal crmin = biologicalQualities.stream().map(BiologicalQuality::getCr).min((x1, x2) -> x1.compareTo(x2)).get();
                //Sediment
                BigDecimal crave = biologicalQualities.stream().map(BiologicalQuality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                crvalueMap.put("max", crmax);
                crvalueMap.put("min", crmin);
                crvalueMap.put("ave", crave);
                resultMap.put("value",crvalueMap);
                break;
            case "cu"://铜
                HashMap<String, BigDecimal> cuvalueMap = new HashMap<>();
                //求最大值
                BigDecimal cumax = biologicalQualities.stream().map(BiologicalQuality::getCu).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cumin = biologicalQualities.stream().map(BiologicalQuality::getCu).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cuave = biologicalQualities.stream().map(BiologicalQuality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                cuvalueMap.put("max", cumax);
                cuvalueMap.put("min", cumin);
                cuvalueMap.put("ave", cuave);
                resultMap.put("value",cuvalueMap);
                break;
            case "ddt"://滴滴涕
                HashMap<String, BigDecimal> gsyvalueMap = new HashMap<>();
                //求最大值
                BigDecimal gsymax = biologicalQualities.stream().map(BiologicalQuality::getDdt).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal gsymin = biologicalQualities.stream().map(BiologicalQuality::getDdt).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal gsyave = biologicalQualities.stream().map(BiologicalQuality::getDdt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                gsyvalueMap.put("max", gsymax);
                gsyvalueMap.put("min", gsymin);
                gsyvalueMap.put("ave", gsyave);
                resultMap.put("value",gsyvalueMap);
                break;
            case "thg"://汞
                HashMap<String, BigDecimal> hgvalueMap = new HashMap<>();
                //求最大值
                BigDecimal hgmax = biologicalQualities.stream().map(BiologicalQuality::getThg).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hgmin = biologicalQualities.stream().map(BiologicalQuality::getThg).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hgave = biologicalQualities.stream().map(BiologicalQuality::getThg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                hgvalueMap.put("max", hgmax);
                hgvalueMap.put("min", hgmin);
                hgvalueMap.put("ave", hgave);
                resultMap.put("value",hgvalueMap);
                break;
            case "pb"://铅
                HashMap<String, BigDecimal> pbvalueMap = new HashMap<>();
                //求最大值
                BigDecimal pbmax = biologicalQualities.stream().map(BiologicalQuality::getPb).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pbmin = biologicalQualities.stream().map(BiologicalQuality::getPb).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pbave = biologicalQualities.stream().map(BiologicalQuality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                pbvalueMap.put("max", pbmax);
                pbvalueMap.put("min", pbmin);
                pbvalueMap.put("ave", pbave);
                resultMap.put("value",pbvalueMap);
                break;
            case "pcbs"://多氯联苯
                HashMap<String, BigDecimal> sylvalueMap = new HashMap<>();
                //求最大值
                BigDecimal sylmax = biologicalQualities.stream().map(BiologicalQuality::getPcbs).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal sylmin = biologicalQualities.stream().map(BiologicalQuality::getPcbs).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal sylave = biologicalQualities.stream().map(BiologicalQuality::getPcbs).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                sylvalueMap.put("max", sylmax);
                sylvalueMap.put("min", sylmin);
                sylvalueMap.put("ave", sylave);
                resultMap.put("value",sylvalueMap);
                break;
            case "syt"://石油烃
                HashMap<String, BigDecimal> tnvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tnmax = biologicalQualities.stream().map(BiologicalQuality::getSyt).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tnmin = biologicalQualities.stream().map(BiologicalQuality::getSyt).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tnave = biologicalQualities.stream().map(BiologicalQuality::getSyt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                tnvalueMap.put("max", tnmax);
                tnvalueMap.put("min", tnmin);
                tnvalueMap.put("ave", tnave);
                resultMap.put("value",tnvalueMap);
                break;
            case "fdcjq"://粪大肠菌群
                HashMap<String, BigDecimal> tpvalueMap = new HashMap<>();
                //求最大值
                BigDecimal tpmax = biologicalQualities.stream().map(BiologicalQuality::getFdcjq).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tpmin = biologicalQualities.stream().map(BiologicalQuality::getFdcjq).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tpave = biologicalQualities.stream().map(BiologicalQuality::getFdcjq).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                tpvalueMap.put("max", tpmax);
                tpvalueMap.put("min", tpmin);
                tpvalueMap.put("ave", tpave);
                resultMap.put("value",tpvalueMap);
                break;

            case "lms"://氯霉素
                HashMap<String, BigDecimal> yxsydvalueMap = new HashMap<>();
                //求最大值
                BigDecimal yxsydmax = biologicalQualities.stream().map(BiologicalQuality::getLms).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yxsydmin = biologicalQualities.stream().map(BiologicalQuality::getLms).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yxsydave = biologicalQualities.stream().map(BiologicalQuality::getLms).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                yxsydvalueMap.put("max", yxsydmax);
                yxsydvalueMap.put("min", yxsydmin);
                yxsydvalueMap.put("ave", yxsydave);
                resultMap.put("value",yxsydvalueMap);
                break;
            case "kss"://抗生素
                HashMap<String, BigDecimal> aSSvalueMap = new HashMap<>();
                //求最大值
                BigDecimal aSSmax = biologicalQualities.stream().map(BiologicalQuality::getKss).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aSSmin = biologicalQualities.stream().map(BiologicalQuality::getKss).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aSSave = biologicalQualities.stream().map(BiologicalQuality::getKss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                aSSvalueMap.put("max", aSSmax);
                aSSvalueMap.put("min", aSSmin);
                aSSvalueMap.put("ave", aSSave);
                resultMap.put("value",aSSvalueMap);
                break;
            case "zn"://锌
                HashMap<String, BigDecimal> znvalueMap = new HashMap<>();
                //求最大值
                BigDecimal znmax = biologicalQualities.stream().map(BiologicalQuality::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal znmin = biologicalQualities.stream().map(BiologicalQuality::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal znave = biologicalQualities.stream().map(BiologicalQuality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                znvalueMap.put("max", znmax);
                znvalueMap.put("min", znmin);
                znvalueMap.put("ave", znave);
                resultMap.put("value",znvalueMap);
                break;
            case "dsp"://腹泻性贝毒
                HashMap<String, BigDecimal> dspvalueMap = new HashMap<>();
                //求最大值
                BigDecimal dspmax = biologicalQualities.stream().map(BiologicalQuality::getDsp).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal dspmin = biologicalQualities.stream().map(BiologicalQuality::getDsp).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal dspave = biologicalQualities.stream().map(BiologicalQuality::getDsp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                dspvalueMap.put("max", dspmax);
                dspvalueMap.put("min", dspmin);
                dspvalueMap.put("ave", dspave);
                resultMap.put("value",dspvalueMap);
                break;
            case "psp"://麻痹性贝毒
                HashMap<String, BigDecimal> pspvalueMap = new HashMap<>();
                //求最大值
                BigDecimal pspmax = biologicalQualities.stream().map(BiologicalQuality::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pspmin = biologicalQualities.stream().map(BiologicalQuality::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pspave = biologicalQualities.stream().map(BiologicalQuality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()), 2, BigDecimal.ROUND_HALF_UP);
                pspvalueMap.put("max", pspmax);
                pspvalueMap.put("min", pspmin);
                pspvalueMap.put("ave", pspave);
                resultMap.put("value",pspvalueMap);
                break;
        }
        return resultMap;
    }
}
