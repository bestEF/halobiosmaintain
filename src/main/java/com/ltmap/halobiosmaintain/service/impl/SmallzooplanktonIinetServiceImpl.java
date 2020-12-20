package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.ltmap.halobiosmaintain.entity.work.SmallzooplanktonIinet;
import com.ltmap.halobiosmaintain.mapper.work.LargezooplanktonInetMapper;
import com.ltmap.halobiosmaintain.mapper.work.SmallzooplanktonIinetMapper;
import com.ltmap.halobiosmaintain.service.ISmallzooplanktonIinetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<SmallzooplanktonIinet> lqw = Wrappers.lambdaQuery();
        lqw.eq(SmallzooplanktonIinet::getReportId,reportId);
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
            smallzooplanktonIinetList=smallzooplanktonIinetList.stream().filter(x->x.getCategory()!=null).collect(Collectors.toList());
            if(smallzooplanktonIinetList.size()!=0){
                list.add(smallzooplanktonIinetList.get(i).getCategory());
            }
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
    public  HashMap<String,BigDecimal> queryBiologicalDensity(String year, String voyage) {
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,null);
        smallzooplanktonIinetList=smallzooplanktonIinetList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(smallzooplanktonIinetList.size()==0){
            result.put("result",new BigDecimal(0));
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < smallzooplanktonIinetList.size(); i++) {
                if (smallzooplanktonIinetList.get(i).getDensity() != null) {
                    density = density.add(smallzooplanktonIinetList.get(i).getDensity());
                }
            }
            if (smallzooplanktonIinetList.size() != 0) {
                density = density.divide(new BigDecimal(smallzooplanktonIinetList.size()), 2, RoundingMode.HALF_UP);
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
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,null);
        smallzooplanktonIinetList=smallzooplanktonIinetList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());
        if(smallzooplanktonIinetList.size()==0){
            resultMap.put("max",null);
            resultMap.put("min",null);
            resultMap.put("ave",null);
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
    public HashMap<String,BigDecimal> queryBiologicalDensityByStation(String year, String voyage,Long stationId){
        List<SmallzooplanktonIinet> smallzooplanktonIinetList=smallzooplanktonIinetMapper.queryBiologicalType(year,voyage,stationId);
        smallzooplanktonIinetList=smallzooplanktonIinetList.stream().filter(x->x.getDensity()!=null).collect(Collectors.toList());

        HashMap<String,BigDecimal> result=new HashMap<>();
        if(smallzooplanktonIinetList.size()==0){
            result.put("result",new BigDecimal(0));//0代表无值的情况
            result.put("density",new BigDecimal(0));
        }else {
            BigDecimal density = new BigDecimal(0);
            for (int i = 0; i < smallzooplanktonIinetList.size(); i++) {
                if (smallzooplanktonIinetList.get(i).getDensity() != null) {
                    density = density.add(smallzooplanktonIinetList.get(i).getDensity());
                }
            }
            if (smallzooplanktonIinetList.size() != 0) {
                density = density.divide(new BigDecimal(smallzooplanktonIinetList.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            result.put("result",new BigDecimal(1));
            result.put("density",density);
        }
        return result;
    }

    /*
     * @Description:小型浮游动物数据
     * @Param smallzooplanktonIinet:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:52
     */
    @Override
    public IPage<SmallzooplanktonIinet> listSmallzooplanktonIinet(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return smallzooplanktonIinetMapper.listSmallzooplanktonIinet(page,stationName,  biologicalChineseName,  startDate,  endDate,reportId);
    }
}
