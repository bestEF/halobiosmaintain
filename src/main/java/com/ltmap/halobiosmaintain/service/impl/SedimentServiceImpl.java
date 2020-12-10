package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.MonitorStationInfoMapper;
import com.ltmap.halobiosmaintain.mapper.work.SedimentMapper;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.service.ISedimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<Sediment> lqw = Wrappers.lambdaQuery();
        lqw.eq(Sediment::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /**
     * @Description:沉积物变化范围
     * @Param year:
     * @Param voyage:
     * @Param element:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/8 17:13
     */
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
                sediments = sediments.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getLhw()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getTcr()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getYjt()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getHg()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getSyl()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getTn()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getTp()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getEh()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getAss()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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
                sediments = sediments.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
                if(sediments.size()==0){
                    valueMap0.put("max",new BigDecimal(0));
                    valueMap0.put("min",new BigDecimal(0));
                    valueMap0.put("ave",new BigDecimal(0));
                    resultMap.put("value",valueMap0);
                    return resultMap;
                }
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


    /**
     * @Description:沉积物评价标准等级
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/7 16:06
     */
    @Override
    public HashMap<String,HashMap<String,BigDecimal>>  sedimentOrder(String year, String voyage){
        List<Sediment> sediments= sedimentMapper.sedimentstatisticOneYear(year,voyage);
        HashMap<String,HashMap<String,BigDecimal>> resultMap=new HashMap<>();
        BigDecimal order1=new BigDecimal(0);
        BigDecimal order2=new BigDecimal(0);
        BigDecimal order3=new BigDecimal(0);
        if(sediments.size()==0){
            HashMap<String, BigDecimal> valueMap0 = new HashMap<>();
            valueMap0.put("第一类", order1);
            valueMap0.put("第二类", order2);
            valueMap0.put("第三类", order3);
            resultMap.put("value",valueMap0);
            return resultMap;
        }
        //水质标准类别占比
        List<MonitorStationInfo> monitorStationInfos = monitorStationInfoService.queryStationInfobyDataType(year, voyage,"沉积物");
        for (int i = 0; i < monitorStationInfos.size(); i++) {
            List<Sediment> sediments1 = sedimentMapper.sedimentStationOneYear(year, voyage, monitorStationInfos.get(i).getStationId());

            //求平均值
            //汞
            sediments1 = sediments1.stream().filter(x -> x.getHg()!=null).collect(Collectors.toList());
            BigDecimal hg=new BigDecimal(0);
            if(sediments1.size()!=0){
                hg = sediments1.stream().map(Sediment::getHg).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //镉
            sediments1 = sediments1.stream().filter(x -> x.getCd()!=null).collect(Collectors.toList());
            BigDecimal cd=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 cd = sediments1.stream().map(Sediment::getCd).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //铬
            sediments1 = sediments1.stream().filter(x -> x.getTcr()!=null).collect(Collectors.toList());
            BigDecimal cr=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 cr = sediments1.stream().map(Sediment::getTcr).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //铅
            sediments1 = sediments1.stream().filter(x -> x.getPb()!=null).collect(Collectors.toList());
            BigDecimal pb=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 pb = sediments1.stream().map(Sediment::getPb).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //砷
            sediments1 = sediments1.stream().filter(x -> x.getAss()!=null).collect(Collectors.toList());
            BigDecimal ass=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 ass = sediments1.stream().map(Sediment::getAss).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //铜
            sediments1 = sediments1.stream().filter(x -> x.getCu()!=null).collect(Collectors.toList());
            BigDecimal cu=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 cu = sediments1.stream().map(Sediment::getCu).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //锌
            sediments1 = sediments1.stream().filter(x -> x.getZn()!=null).collect(Collectors.toList());
            BigDecimal zn=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 zn = sediments1.stream().map(Sediment::getZn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //有机碳
            sediments1 = sediments1.stream().filter(x -> x.getYjt()!=null).collect(Collectors.toList());
            BigDecimal yjt=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 yjt = sediments1.stream().map(Sediment::getYjt).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //硫化物
            sediments1 = sediments1.stream().filter(x -> x.getLhw()!=null).collect(Collectors.toList());
            BigDecimal lhw=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 lhw = sediments1.stream().map(Sediment::getLhw).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //石油类
            sediments1 = sediments1.stream().filter(x -> x.getSyl()!=null).collect(Collectors.toList());
            BigDecimal syl=new BigDecimal(0);
            if(sediments1.size()!=0) {
                 syl = sediments1.stream().map(Sediment::getSyl).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(sediments1.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            //按海水水质标准计算等级
            //汞
            int hgint1 = hg.compareTo(new BigDecimal(0.2));
            int hgint2 = hg.compareTo(new BigDecimal(0.5));
            int hgint3 = hg.compareTo(new BigDecimal(1));
            //镉
            int cdint1 = cd.compareTo(new BigDecimal(0.5));
            int cdint2 = cd.compareTo(new BigDecimal(1.5));
            int cdint3 = cd.compareTo(new BigDecimal(5));
            //铬
            int crint1 = cr.compareTo(new BigDecimal(80));
            int crint2 = cr.compareTo(new BigDecimal(150));
            int crint3 = cr.compareTo(new BigDecimal(270));
            //铅
            int pbint1 = pb.compareTo(new BigDecimal(60));
            int pbint2 = pb.compareTo(new BigDecimal(130));
            int pbint3 = pb.compareTo(new BigDecimal(250));
            //砷
            int assint1 = ass.compareTo(new BigDecimal(20));
            int assint2 = ass.compareTo(new BigDecimal(65));
            int assint3 = ass.compareTo(new BigDecimal(93));
            //铜
            int cuint1 = cu.compareTo(new BigDecimal(35));
            int cuint2 = cu.compareTo(new BigDecimal(100));
            int cuint3 = cu.compareTo(new BigDecimal(200));
            //锌
            int znint1 = zn.compareTo(new BigDecimal(150));
            int znint2 = zn.compareTo(new BigDecimal(350));
            int znint3 = zn.compareTo(new BigDecimal(600));
            //有机碳
            int yjtint1 = yjt.compareTo(new BigDecimal(2));
            int yjtint2 = yjt.compareTo(new BigDecimal(3));
            int yjtint3 = yjt.compareTo(new BigDecimal(4));
            //硫化物
            int lhwint1 = lhw.compareTo(new BigDecimal(300));
            int lhwint2 = lhw.compareTo(new BigDecimal(500));
            int lhwint3 = lhw.compareTo(new BigDecimal(600));
            //石油类
            int sylint1 = syl.compareTo(new BigDecimal(500));
            int sylint2 = syl.compareTo(new BigDecimal(1000));
            int sylint3 = syl.compareTo(new BigDecimal(1500));

            //第一类
            if((yjtint1<=0)  && (lhwint1<=0)&&(sylint1<=0)&&(hgint1<=0)&&(cdint1<=0)&&(pbint1<=0)&&(crint1<=0)&&(assint1<=0)&&(cuint1<=0)&&(znint1<=0)){
                order1 = order1.add(new BigDecimal(1));
            }
            //第二类
            else if((yjtint2<=0)  && (lhwint2<=0)&&(sylint2<=0)&&(hgint2<=0)&&(cdint2<=0)&&(pbint2<=0)&&(crint2<=0)&&(assint2<=0)&&(cuint2<=0)&&(znint2<=0)){
                order2 = order2.add(new BigDecimal(1));
            }
            //第三类
            else if((yjtint3<=0)  && (lhwint3<=0)&&(sylint3<=0)&&(hgint3<=0)&&(cdint3<=0)&&(pbint3<=0)&&(crint3<=0)&&(assint3<=0)&&(cuint3<=0)&&(znint3<=0)){
                order3 = order3.add(new BigDecimal(1));
            }
        }
        HashMap<String, BigDecimal> valueMap1 = new HashMap<>();
        valueMap1.put("第一类", order1);
        valueMap1.put("第二类", order2);
        valueMap1.put("第三类", order3);
        resultMap.put("value", valueMap1);
        return resultMap;
    }


    /*
     * @Description:沉积物数据
     * @Param sediment:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:49
     */
    @Override
    public IPage<Sediment> listSediment(Integer current,Integer size,String stationName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return sedimentMapper.listSediment(page,stationName,  startDate,  endDate,reportId);
    }
}
