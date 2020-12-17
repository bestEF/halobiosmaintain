package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.BiologicalQualityMapper;
import com.ltmap.halobiosmaintain.service.IBiologicalQualityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;


    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<BiologicalQuality> lqw = Wrappers.lambdaQuery();
        lqw.eq(BiologicalQuality::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /**
     * @Description:一年内生物质量变化范围
     * @Param year:
     * @Param voyage:
     * @Param element:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/8 17:06
     */
    @Override
    public HashMap<String, HashMap<String, BigDecimal>> biologicalQualityRangeOneYear(String year, String voyage, String element){

        List<BiologicalQuality> biologicalQualities= biologicalQualityMapper.biologicalQualityRangeOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
        if(biologicalQualities.size()==0){
            valueMap0.put("max",null);
            valueMap0.put("min",null);
            valueMap0.put("ave",null);
            resultMap.put("value",valueMap0);
            return resultMap;
        }
        switch (element) {
            case "cd"://镉
                HashMap<String, BigDecimal> cDvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal cDmax = biologicalQualities.stream().map(BiologicalQuality::getCd).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cDmin = biologicalQualities.stream().map(BiologicalQuality::getCd).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cDave = biologicalQualities.stream().map(BiologicalQuality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                cDvalueMap.put("max", cDmax);
                cDvalueMap.put("min", cDmin);
                cDvalueMap.put("ave", cDave);
                resultMap.put("value",cDvalueMap);
                break;
            case "sixsixsix"://六六六
                HashMap<String, BigDecimal> codvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getSixsixsix()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal codmax = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal codmin = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal codave = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                codvalueMap.put("max", codmax);
                codvalueMap.put("min", codmin);
                codvalueMap.put("ave", codave);
                resultMap.put("value",codvalueMap);
                break;
            case "cr"://铬
                HashMap<String, BigDecimal> crvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getCr()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal crmax = biologicalQualities.stream().map(BiologicalQuality::getCr).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal crmin = biologicalQualities.stream().map(BiologicalQuality::getCr).min((x1, x2) -> x1.compareTo(x2)).get();
                //Sediment
                BigDecimal crave = biologicalQualities.stream().map(BiologicalQuality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                crvalueMap.put("max", crmax);
                crvalueMap.put("min", crmin);
                crvalueMap.put("ave", crave);
                resultMap.put("value",crvalueMap);
                break;
            case "cu"://铜
                HashMap<String, BigDecimal> cuvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal cumax = biologicalQualities.stream().map(BiologicalQuality::getCu).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal cumin = biologicalQualities.stream().map(BiologicalQuality::getCu).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal cuave = biologicalQualities.stream().map(BiologicalQuality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                cuvalueMap.put("max", cumax);
                cuvalueMap.put("min", cumin);
                cuvalueMap.put("ave", cuave);
                resultMap.put("value",cuvalueMap);
                break;
            case "ddt"://滴滴涕
                HashMap<String, BigDecimal> gsyvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getDdt()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal gsymax = biologicalQualities.stream().map(BiologicalQuality::getDdt).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal gsymin = biologicalQualities.stream().map(BiologicalQuality::getDdt).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal gsyave = biologicalQualities.stream().map(BiologicalQuality::getDdt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                gsyvalueMap.put("max", gsymax);
                gsyvalueMap.put("min", gsymin);
                gsyvalueMap.put("ave", gsyave);
                resultMap.put("value",gsyvalueMap);
                break;
            case "thg"://汞
                HashMap<String, BigDecimal> hgvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getThg()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal hgmax = biologicalQualities.stream().map(BiologicalQuality::getThg).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal hgmin = biologicalQualities.stream().map(BiologicalQuality::getThg).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal hgave = biologicalQualities.stream().map(BiologicalQuality::getThg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                hgvalueMap.put("max", hgmax);
                hgvalueMap.put("min", hgmin);
                hgvalueMap.put("ave", hgave);
                resultMap.put("value",hgvalueMap);
                break;
            case "pb"://铅
                HashMap<String, BigDecimal> pbvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal pbmax = biologicalQualities.stream().map(BiologicalQuality::getPb).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pbmin = biologicalQualities.stream().map(BiologicalQuality::getPb).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pbave = biologicalQualities.stream().map(BiologicalQuality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                pbvalueMap.put("max", pbmax);
                pbvalueMap.put("min", pbmin);
                pbvalueMap.put("ave", pbave);
                resultMap.put("value",pbvalueMap);
                break;
            case "pcbs"://多氯联苯
                HashMap<String, BigDecimal> sylvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getPcbs()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal sylmax = biologicalQualities.stream().map(BiologicalQuality::getPcbs).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal sylmin = biologicalQualities.stream().map(BiologicalQuality::getPcbs).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal sylave = biologicalQualities.stream().map(BiologicalQuality::getPcbs).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                sylvalueMap.put("max", sylmax);
                sylvalueMap.put("min", sylmin);
                sylvalueMap.put("ave", sylave);
                resultMap.put("value",sylvalueMap);
                break;
            case "syt"://石油烃
                HashMap<String, BigDecimal> tnvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getSyt()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal tnmax = biologicalQualities.stream().map(BiologicalQuality::getSyt).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tnmin = biologicalQualities.stream().map(BiologicalQuality::getSyt).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tnave = biologicalQualities.stream().map(BiologicalQuality::getSyt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                tnvalueMap.put("max", tnmax);
                tnvalueMap.put("min", tnmin);
                tnvalueMap.put("ave", tnave);
                resultMap.put("value",tnvalueMap);
                break;
            case "fdcjq"://粪大肠菌群
                HashMap<String, BigDecimal> tpvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getFdcjq()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal tpmax = biologicalQualities.stream().map(BiologicalQuality::getFdcjq).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal tpmin = biologicalQualities.stream().map(BiologicalQuality::getFdcjq).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal tpave = biologicalQualities.stream().map(BiologicalQuality::getFdcjq).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                tpvalueMap.put("max", tpmax);
                tpvalueMap.put("min", tpmin);
                tpvalueMap.put("ave", tpave);
                resultMap.put("value",tpvalueMap);
                break;

            case "lms"://氯霉素
                HashMap<String, BigDecimal> yxsydvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getLms()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal yxsydmax = biologicalQualities.stream().map(BiologicalQuality::getLms).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal yxsydmin = biologicalQualities.stream().map(BiologicalQuality::getLms).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal yxsydave = biologicalQualities.stream().map(BiologicalQuality::getLms).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                yxsydvalueMap.put("max", yxsydmax);
                yxsydvalueMap.put("min", yxsydmin);
                yxsydvalueMap.put("ave", yxsydave);
                resultMap.put("value",yxsydvalueMap);
                break;
            case "kss"://抗生素
                HashMap<String, BigDecimal> aSSvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getKss()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal aSSmax = biologicalQualities.stream().map(BiologicalQuality::getKss).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal aSSmin = biologicalQualities.stream().map(BiologicalQuality::getKss).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal aSSave = biologicalQualities.stream().map(BiologicalQuality::getKss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                aSSvalueMap.put("max", aSSmax);
                aSSvalueMap.put("min", aSSmin);
                aSSvalueMap.put("ave", aSSave);
                resultMap.put("value",aSSvalueMap);
                break;
            case "zn"://锌
                HashMap<String, BigDecimal> znvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal znmax = biologicalQualities.stream().map(BiologicalQuality::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal znmin = biologicalQualities.stream().map(BiologicalQuality::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal znave = biologicalQualities.stream().map(BiologicalQuality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                znvalueMap.put("max", znmax);
                znvalueMap.put("min", znmin);
                znvalueMap.put("ave", znave);
                resultMap.put("value",znvalueMap);
                break;
            case "dsp"://腹泻性贝毒
                HashMap<String, BigDecimal> dspvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getDsp()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal dspmax = biologicalQualities.stream().map(BiologicalQuality::getDsp).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal dspmin = biologicalQualities.stream().map(BiologicalQuality::getDsp).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal dspave = biologicalQualities.stream().map(BiologicalQuality::getDsp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                dspvalueMap.put("max", dspmax);
                dspvalueMap.put("min", dspmin);
                dspvalueMap.put("ave", dspave);
                resultMap.put("value",dspvalueMap);
                break;
            case "psp"://麻痹性贝毒
                HashMap<String, BigDecimal> pspvalueMap = new HashMap<>();
                biologicalQualities = biologicalQualities.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
                if(biologicalQualities.size()==0){
                    valueMap0.put("max",null);
                    valueMap0.put("min",null);
                    valueMap0.put("ave",null);
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
                //求最大值
                BigDecimal pspmax = biologicalQualities.stream().map(BiologicalQuality::getZn).max((x1, x2) -> x1.compareTo(x2)).get();
                //求最小值
                BigDecimal pspmin = biologicalQualities.stream().map(BiologicalQuality::getZn).min((x1, x2) -> x1.compareTo(x2)).get();
                //求平均值
                BigDecimal pspave = biologicalQualities.stream().map(BiologicalQuality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
                pspvalueMap.put("max", pspmax);
                pspvalueMap.put("min", pspmin);
                pspvalueMap.put("ave", pspave);
                resultMap.put("value",pspvalueMap);
                break;
            default:
                valueMap0.put("max",null);
                valueMap0.put("min",null);
                valueMap0.put("ave",null);
                resultMap.put("value",valueMap0);
                return resultMap;
        }
        return resultMap;
    }



    /**
     * @Description:生物质量评价标准等级描述_一年内
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/7 15:43
     */
    @Override
    public HashMap<String,HashMap<String,String>>  biologicalQualityOrder(String year, String voyage){
        List<BiologicalQuality> biologicalQualities= biologicalQualityMapper.biologicalQualityRangeOneYear(year,voyage);
        HashMap<String,HashMap<String,String>> resultMap=new HashMap<>();

        if(biologicalQualities.size()==0){
            HashMap<String, String> valueMap0 = new HashMap<>();
            String resultStr="超第一类生物质量标准的项目有：无";
            valueMap0.put("resultStr", resultStr);
            resultMap.put("value",valueMap0);

            return resultMap;
        }else{//求平均值
            //麻痹性贝毒
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getPsp()!=null).collect(Collectors.toList());
            BigDecimal psp=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 psp = biologicalQualities.stream().map(BiologicalQuality::getPsp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //石油烃
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getSyt()!=null).collect(Collectors.toList());
            BigDecimal syt=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 syt = biologicalQualities.stream().map(BiologicalQuality::getSyt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //六六六
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getSixsixsix()!=null).collect(Collectors.toList());
            BigDecimal six=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 six = biologicalQualities.stream().map(BiologicalQuality::getSixsixsix).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //滴滴涕
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getDdt()!=null).collect(Collectors.toList());
            BigDecimal ddt=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 ddt = biologicalQualities.stream().map(BiologicalQuality::getDdt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //汞
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getThg()!=null).collect(Collectors.toList());
            BigDecimal hg=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 hg = biologicalQualities.stream().map(BiologicalQuality::getThg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //镉
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
            BigDecimal cd=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 cd = biologicalQualities.stream().map(BiologicalQuality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //铬
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getCr()!=null).collect(Collectors.toList());
            BigDecimal cr=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 cr = biologicalQualities.stream().map(BiologicalQuality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //铅
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
            BigDecimal pb=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 pb = biologicalQualities.stream().map(BiologicalQuality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //砷
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getAss()!=null).collect(Collectors.toList());
            BigDecimal ass=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 ass = biologicalQualities.stream().map(BiologicalQuality::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //铜
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
            BigDecimal cu=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 cu = biologicalQualities.stream().map(BiologicalQuality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //锌
            biologicalQualities = biologicalQualities.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
            BigDecimal zn=new BigDecimal(0);
            if(biologicalQualities.size()!=0) {
                 zn = biologicalQualities.stream().map(BiologicalQuality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities.size()),10, BigDecimal.ROUND_HALF_UP);
            }
            //按海水水质标准计算等级
            //麻痹性贝毒
            int pspint1 = psp.compareTo(new BigDecimal(0.8));
            int pspint2 = psp.compareTo(new BigDecimal(0.8));
            int pspint3 = psp.compareTo(new BigDecimal(0.8));
            //石油烃
            int sytint1 = syt.compareTo(new BigDecimal(15));
            int sytint2 = syt.compareTo(new BigDecimal(50));
            int sytint3 = syt.compareTo(new BigDecimal(80));
            //六六六
            int sixint1 = six.compareTo(new BigDecimal(0.02));
            int sixint2 = six.compareTo(new BigDecimal(0.15));
            int sixint3 = six.compareTo(new BigDecimal(0.5));
            //滴滴涕
            int ddtint1 = ddt.compareTo(new BigDecimal(0.01));
            int ddtint2 = ddt.compareTo(new BigDecimal(0.1));
            int ddtint3 = ddt.compareTo(new BigDecimal(0.5));
            //汞
            int hgint1 = hg.compareTo(new BigDecimal(0.05));
            int hgint2 = hg.compareTo(new BigDecimal(0.1));
            int hgint3 = hg.compareTo(new BigDecimal(0.3));
            //镉
            int cdint1 = cd.compareTo(new BigDecimal(0.2));
            int cdint2 = cd.compareTo(new BigDecimal(2));
            int cdint3 = cd.compareTo(new BigDecimal(5));
            //铬
            int crint1 = cr.compareTo(new BigDecimal(0.5));
            int crint2 = cr.compareTo(new BigDecimal(2));
            int crint3 = cr.compareTo(new BigDecimal(6));
            //铅
            int pbint1 = pb.compareTo(new BigDecimal(0.1));
            int pbint2 = pb.compareTo(new BigDecimal(2));
            int pbint3 = pb.compareTo(new BigDecimal(6));
            //砷
            int assint1 = ass.compareTo(new BigDecimal(1));
            int assint2 = ass.compareTo(new BigDecimal(5));
            int assint3 = ass.compareTo(new BigDecimal(8));
            //铜
            int cuint1 = cu.compareTo(new BigDecimal(10));
            int cuint2 = cu.compareTo(new BigDecimal(25));
            int cuint3 = cu.compareTo(new BigDecimal(50));
            //锌
            int znint1 = zn.compareTo(new BigDecimal(20));
            int znint2 = zn.compareTo(new BigDecimal(50));
            int znint3 = zn.compareTo(new BigDecimal(100));


            String resultStr="超第一类生物质量标准的项目有：";
            //第一类
            if ((sytint1 <= 0)) {
                resultStr+="石油烃"+"、";
            }
            if ((sixint1 <= 0)) {
                resultStr+="六六六"+"、";
            }
            if ((ddtint1 <= 0)) {
                resultStr+="滴滴涕"+"、";
            }
            if ((hgint1 <= 0)) {
                resultStr+="汞"+"、";
            }
            if ((cdint1 <= 0)) {
                resultStr+="镉"+"、";
            }
            if ((pbint1 <= 0)) {
                resultStr+="铅"+"、";
            }
            if ((crint1 <= 0)) {
                resultStr+="铬"+"、";
            }
            if ((assint1 <= 0)) {
                resultStr+="砷"+"、";
            }
            if ((cuint1 <= 0)) {
                resultStr+="铜"+"、";
            }
            if ((znint1 <= 0)) {
                resultStr+="锌"+"、";
            }
            String result=resultStr.substring(0,resultStr.length()-1);
            HashMap<String, String> valueMap1 = new HashMap<>();
            valueMap1.put("resultStr", result);
            resultMap.put("value", valueMap1);
        }
//        //水质标准类别占比
//        List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfo(year, voyage);
//        for (int i = 0; i < monitorStationInfos.size(); i++) {
//            List<BiologicalQuality> biologicalQualities1 = biologicalQualityMapper.biologicalQualityStationOneYear(year, voyage, monitorStationInfos.get(i).getStationId());
//            if(biologicalQualities1.size()>0){
//
//            //求平均值
//            //麻痹性贝毒
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getPsp()!=null).collect(Collectors.toList());
//            BigDecimal psp = biologicalQualities1.stream().map(BiologicalQuality::getPsp).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//            //石油烃
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getSyt()!=null).collect(Collectors.toList());
//            BigDecimal syt = biologicalQualities1.stream().map(BiologicalQuality::getSyt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//            //六六六
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getSixsixsix()!=null).collect(Collectors.toList());
//            BigDecimal six = biologicalQualities1.stream().map(BiologicalQuality::getSixsixsix).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//            //滴滴涕
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getDdt()!=null).collect(Collectors.toList());
//            BigDecimal ddt = biologicalQualities1.stream().map(BiologicalQuality::getDdt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//            //汞
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getThg()!=null).collect(Collectors.toList());
//            BigDecimal hg = biologicalQualities1.stream().map(BiologicalQuality::getThg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//            //镉
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
//            BigDecimal cd = biologicalQualities1.stream().map(BiologicalQuality::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//
//            //铬
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getCr()!=null).collect(Collectors.toList());
//            BigDecimal cr = biologicalQualities1.stream().map(BiologicalQuality::getCr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//
//            //铅
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
//            BigDecimal pb = biologicalQualities1.stream().map(BiologicalQuality::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//
//            //砷
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getAss()!=null).collect(Collectors.toList());
//            BigDecimal ass = biologicalQualities1.stream().map(BiologicalQuality::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//
//            //铜
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
//            BigDecimal cu = biologicalQualities1.stream().map(BiologicalQuality::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//
//            //锌
//            biologicalQualities1 = biologicalQualities1.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
//            BigDecimal zn = biologicalQualities1.stream().map(BiologicalQuality::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(biologicalQualities1.size()), 2, BigDecimal.ROUND_HALF_UP);
//
//            //按海水水质标准计算等级
//            //麻痹性贝毒
//            int pspint1 = psp.compareTo(new BigDecimal(0.8));
//            int pspint2 = psp.compareTo(new BigDecimal(0.8));
//            int pspint3 = psp.compareTo(new BigDecimal(0.8));
//            //石油烃
//            int sytint1 = syt.compareTo(new BigDecimal(15));
//            int sytint2 = syt.compareTo(new BigDecimal(50));
//            int sytint3 = syt.compareTo(new BigDecimal(80));
//            //六六六
//            int sixint1 = six.compareTo(new BigDecimal(0.02));
//            int sixint2 = six.compareTo(new BigDecimal(0.15));
//            int sixint3 = six.compareTo(new BigDecimal(0.5));
//            //滴滴涕
//            int ddtint1 = ddt.compareTo(new BigDecimal(0.01));
//            int ddtint2 = ddt.compareTo(new BigDecimal(0.1));
//            int ddtint3 = ddt.compareTo(new BigDecimal(0.5));
//            //汞
//            int hgint1 = hg.compareTo(new BigDecimal(0.05));
//            int hgint2 = hg.compareTo(new BigDecimal(0.1));
//            int hgint3 = hg.compareTo(new BigDecimal(0.3));
//            //镉
//            int cdint1 = cd.compareTo(new BigDecimal(0.2));
//            int cdint2 = cd.compareTo(new BigDecimal(2));
//            int cdint3 = cd.compareTo(new BigDecimal(5));
//            //铬
//            int crint1 = cr.compareTo(new BigDecimal(0.5));
//            int crint2 = cr.compareTo(new BigDecimal(2));
//            int crint3 = cr.compareTo(new BigDecimal(6));
//            //铅
//            int pbint1 = pb.compareTo(new BigDecimal(0.1));
//            int pbint2 = pb.compareTo(new BigDecimal(2));
//            int pbint3 = pb.compareTo(new BigDecimal(6));
//            //砷
//            int assint1 = ass.compareTo(new BigDecimal(1));
//            int assint2 = ass.compareTo(new BigDecimal(5));
//            int assint3 = ass.compareTo(new BigDecimal(8));
//            //铜
//            int cuint1 = cu.compareTo(new BigDecimal(10));
//            int cuint2 = cu.compareTo(new BigDecimal(25));
//            int cuint3 = cu.compareTo(new BigDecimal(50));
//            //锌
//            int znint1 = zn.compareTo(new BigDecimal(20));
//            int znint2 = zn.compareTo(new BigDecimal(50));
//            int znint3 = zn.compareTo(new BigDecimal(100));
//
//
//            //第一类
//            if ((pspint1 <= 0) && (sytint1 <= 0) && (sixint1 <= 0) && (ddtint1 <= 0) && (hgint1 <= 0) && (cdint1 <= 0) && (pbint1 <= 0) && (crint1 <= 0) && (assint1 <= 0) && (cuint1 <= 0) && (znint1 <= 0)) {
//
//                HashMap<String, String> valueMap1 = new HashMap<>();
//                valueMap1.put(monitorStationInfos.get(i).getStationName(), "第一类");
//                resultMap.put("value", valueMap1);
//            }
//            //第二类
//            else if ((pspint2 <= 0) && (sytint2 <= 0) && (sixint2 <= 0) && (ddtint2 <= 0) && (hgint2 <= 0) && (cdint2 <= 0) && (pbint2 <= 0) && (crint2 <= 0) && (assint2 <= 0) && (cuint2 <= 0) && (znint2 <= 0)) {
//
//                HashMap<String, String> valueMap1 = new HashMap<>();
//                valueMap1.put(monitorStationInfos.get(i).getStationName(), "第二类");
//                resultMap.put("value", valueMap1);
//            }
//            //第三类
//            else if ((pspint3 <= 0) && (sytint3 <= 0) && (sixint3 <= 0) && (ddtint3 <= 0) && (hgint3 <= 0) && (cdint3 <= 0) && (pbint3 <= 0) && (crint3 <= 0) && (assint3 <= 0) && (cuint3 <= 0) && (znint3 <= 0)) {
//
//                HashMap<String, String> valueMap1 = new HashMap<>();
//                valueMap1.put(monitorStationInfos.get(i).getStationName(), "第三类");
//                resultMap.put("value", valueMap1);
//            }
//            else{
//                HashMap<String, String> valueMap1 = new HashMap<>();
//                resultMap.put("value",valueMap1);
//            }
//
//            String resultStr="超第一类生物质量标准的项目有：";
//            //第一类
//            if ((sytint1 <= 0)) {
//                resultStr+="石油烃"+"、";
//            }
//            if ((sixint1 <= 0)) {
//                resultStr+="六六六"+"、";
//            }
//            if ((ddtint1 <= 0)) {
//                resultStr+="滴滴涕"+"、";
//            }
//            if ((hgint1 <= 0)) {
//                resultStr+="汞"+"、";
//            }
//            if ((cdint1 <= 0)) {
//                resultStr+="镉"+"、";
//            }
//            if ((pbint1 <= 0)) {
//                resultStr+="铅"+"、";
//            }
//            if ((crint1 <= 0)) {
//                resultStr+="铬"+"、";
//            }
//            if ((assint1 <= 0)) {
//                resultStr+="砷"+"、";
//            }
//            if ((cuint1 <= 0)) {
//                resultStr+="铜"+"、";
//            }
//            if ((znint1 <= 0)) {
//                resultStr+="锌"+"、";
//            }
//            String result=resultStr.substring(0,resultStr.length()-1);
//            HashMap<String, String> valueMap1 = new HashMap<>();
//            valueMap1.put("resultStr", result);
//            resultMap.put("resultStr", valueMap1);
//            }
//        }
        return resultMap;
    }

    /*
     * @Description:生物质量数据查询_数据管理
     * @Param biologicalQuality:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:43
     */
    @Override
    public IPage<BiologicalQuality> listBiologicalQuality(Integer current,Integer size,String stationName,String biologicalChineseName,String startDate,String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return biologicalQualityMapper.listBiologicalQuality(page,stationName,biologicalChineseName,startDate,endDate,reportId);
    }


}
