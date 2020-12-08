package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.FisheggQualitative;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.mapper.work.FisheggQualitativeMapper;
import com.ltmap.halobiosmaintain.mapper.work.FisheggQuantitativeMapper;
import com.ltmap.halobiosmaintain.service.IFisheggQualitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 鱼卵定性表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class FisheggQualitativeServiceImpl extends ServiceImpl<FisheggQualitativeMapper, FisheggQualitative> implements IFisheggQualitativeService {

    @Resource
    private FisheggQualitativeMapper fisheggQualitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<FisheggQualitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(FisheggQualitative::getReportId,reportId);
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
    public List<FisheggQualitative> queryBiologicalType(String year, String voyage){
        List<FisheggQualitative> fisheggQualitativeList=fisheggQualitativeMapper.queryBiologicalType(year,voyage);
        //去重
        List<FisheggQualitative> fisheggQualitativeListNew= ListDistinctUtils.distinctFisheggQualitativeByMap(fisheggQualitativeList);
        return fisheggQualitativeListNew;
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
        List<FisheggQualitative> fisheggQualitativeList=fisheggQualitativeMapper.queryBiologicalType(year,voyage);
        List list = new ArrayList();
        for (int i = 0; i < fisheggQualitativeList.size(); i++) {
            list.add(fisheggQualitativeList.get(i).getBiologicalChineseName());
        }
        return list;
    }

    /*
     * @Description:鱼卵定性数据查询_数据管理
     * @Param fisheggQualitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:44
     */
    @Override
    public IPage<FisheggQualitative> listFisheggQualitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<FisheggQualitative> page=new Page<>(current, size);
        return fisheggQualitativeMapper.listFisheggQualitative(page,stationName,biologicalChineseName,startDate,endDate);
    }

}
