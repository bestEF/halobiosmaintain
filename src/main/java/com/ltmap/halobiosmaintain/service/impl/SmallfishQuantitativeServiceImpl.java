package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.SmallfishQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.ISmallfishQuantitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 仔鱼定量表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class SmallfishQuantitativeServiceImpl extends ServiceImpl<SmallfishQuantitativeMapper, SmallfishQuantitative> implements ISmallfishQuantitativeService {

    @Resource
    private SmallfishQuantitativeMapper smallfishQuantitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<SmallfishQuantitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(SmallfishQuantitative::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /*
     * @Description:查询生物种种类
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:51
     */
    @Override
    public List<SmallfishQuantitative> queryBiologicalType(String year, String voyage){
        List<SmallfishQuantitative> smallfishQuantitativeList=smallfishQuantitativeMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<SmallfishQuantitative> smallfishQuantitativeListNew= ListDistinctUtils.distinctSmallfishQuantitativeByMap(smallfishQuantitativeList);
        return smallfishQuantitativeListNew;
    }

    /*
     * @Description:生物组成
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 10:14
     */
    @Override
    public List<String> statisticTypeFromOneMap(String year, String voyage){
        List<SmallfishQuantitative> smallfishQuantitativeList=smallfishQuantitativeMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        for (int i = 0; i <smallfishQuantitativeList.size() ; i++) {
            list.add(smallfishQuantitativeList.get(i).getBiologicalChineseName());
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
        List<SmallfishQuantitative> smallfishQuantitativeList=smallfishQuantitativeMapper.queryBiologicalType(year,voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < smallfishQuantitativeList.size(); i++) {
            density = density.add(smallfishQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(smallfishQuantitativeList.size()));
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
        List<SmallfishQuantitative> smallfishQuantitativeList=smallfishQuantitativeMapper.queryBiologicalType(year,voyage,null);
        if(smallfishQuantitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = smallfishQuantitativeList.stream().map(SmallfishQuantitative::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = smallfishQuantitativeList.stream().map(SmallfishQuantitative::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = smallfishQuantitativeList.stream().map(SmallfishQuantitative::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(smallfishQuantitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<SmallfishQuantitative> smallfishQuantitativeList=smallfishQuantitativeMapper.queryBiologicalType(year,voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < smallfishQuantitativeList.size(); i++) {
            density = density.add(smallfishQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(smallfishQuantitativeList.size()));
        return density;
    }

    /*
     * @Description:仔鱼定量数据
     * @Param smallfishQuantitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:51
     */
    @Override
    public IPage<SmallfishQuantitative> listSmallfishQuantitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return smallfishQuantitativeMapper.listSmallfishQuantitative(page,stationName,  biologicalChineseName,  startDate,  endDate);
    }
}
