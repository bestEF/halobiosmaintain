package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.mapper.work.FisheggQuantitativeMapper;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.IFisheggQuantitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 鱼卵定量表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class FisheggQuantitativeServiceImpl extends ServiceImpl<FisheggQuantitativeMapper, FisheggQuantitative> implements IFisheggQuantitativeService {
    @Resource
    private FisheggQuantitativeMapper fisheggQuantitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<FisheggQuantitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(FisheggQuantitative::getReportId,reportId);
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
    public List<FisheggQuantitative> queryBiologicalType(String year, String voyage){
        List<FisheggQuantitative> fisheggQuantitativeList=fisheggQuantitativeMapper.queryBiologicalType(year,voyage,null);
        //去重
        List<FisheggQuantitative> fisheggQuantitativeListNew= ListDistinctUtils.distinctFisheggQuantitativeByMap(fisheggQuantitativeList);
        return fisheggQuantitativeListNew;
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
        List<FisheggQuantitative> fisheggQuantitativeList=fisheggQuantitativeMapper.queryBiologicalType(year,voyage,null);
        List list = new ArrayList();
        for (int i = 0; i <fisheggQuantitativeList.size() ; i++) {
            list.add(fisheggQuantitativeList.get(i).getBiologicalChineseName());
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
        List<FisheggQuantitative> fisheggQuantitativeList=fisheggQuantitativeMapper.queryBiologicalType(year,voyage,null);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < fisheggQuantitativeList.size(); i++) {
            density = density.add(fisheggQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(fisheggQuantitativeList.size()));
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
        List<FisheggQuantitative> fisheggQuantitativeList=fisheggQuantitativeMapper.queryBiologicalType(year,voyage,null);
        if(fisheggQuantitativeList.size()==0){
            resultMap.put("max",new BigDecimal(0));
            resultMap.put("min",new BigDecimal(0));
            resultMap.put("ave",new BigDecimal(0));
            return resultMap;
        }
        //求最大值
        BigDecimal max = fisheggQuantitativeList.stream().map(FisheggQuantitative::getDensity).max((x1, x2) -> x1.compareTo(x2)).get();
        //求最小值
        BigDecimal min = fisheggQuantitativeList.stream().map(FisheggQuantitative::getDensity).min((x1, x2) -> x1.compareTo(x2)).get();
        //求平均值
        BigDecimal ave = fisheggQuantitativeList.stream().map(FisheggQuantitative::getDensity).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(fisheggQuantitativeList.size()), 2, BigDecimal.ROUND_HALF_UP);
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
        List<FisheggQuantitative> fisheggQuantitativeList=fisheggQuantitativeMapper.queryBiologicalType(year,voyage,stationId);
        BigDecimal density = new BigDecimal(0);
        for (int i = 0; i < fisheggQuantitativeList.size(); i++) {
            density = density.add(fisheggQuantitativeList.get(i).getDensity());
        }
        density=density.subtract(new BigDecimal(fisheggQuantitativeList.size()));
        return density;
    }

}
