package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.SmallzooplanktonIinet;
import com.ltmap.halobiosmaintain.mapper.work.LargezooplanktonInetMapper;
import com.ltmap.halobiosmaintain.mapper.work.SmallzooplanktonIinetMapper;
import com.ltmap.halobiosmaintain.service.ISmallzooplanktonIinetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 小型浮游动物（II型网）表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class SmallzooplanktonIinetServiceImpl extends ServiceImpl<SmallzooplanktonIinetMapper, SmallzooplanktonIinet> implements ISmallzooplanktonIinetService {

    @Resource
    private SmallzooplanktonIinetMapper smallzooplanktonIinetMapper;

    /*
     * @Description:询生物种种类
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:51
     */
    @Override
    public List<SmallzooplanktonIinet> queryBiologicalType(String year, String voyage){
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<SmallzooplanktonIinet> smallzooplanktonIinetListNew= ListDistinctUtils.distinctSmallzooplanktonIinetByMap(smallzooplanktonIinetList);
        return smallzooplanktonIinetListNew;
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
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        for (int i = 0; i <smallzooplanktonIinetList.size() ; i++) {
            list.add(smallzooplanktonIinetList.get(i).getCategory());
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
    public BigDecimal queryBiologicalDensity(String year, String voyage) {
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < smallzooplanktonIinetList.size(); i++) {
            density = density.add(smallzooplanktonIinetList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(smallzooplanktonIinetList.size()));
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
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,null);
        if(smallzooplanktonIinetList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = smallzooplanktonIinetList.stream().map(SmallzooplanktonIinet::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = smallzooplanktonIinetList.stream().map(SmallzooplanktonIinet::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = smallzooplanktonIinetList.stream().map(SmallzooplanktonIinet::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(smallzooplanktonIinetList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < smallzooplanktonIinetList.size(); i++) {
            density = density.add(smallzooplanktonIinetList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(smallzooplanktonIinetList.size()));
        return density;
    }
}
