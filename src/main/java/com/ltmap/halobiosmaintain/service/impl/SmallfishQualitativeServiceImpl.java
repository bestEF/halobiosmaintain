package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQualitative;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.ltmap.halobiosmaintain.mapper.work.SmallfishQualitativeMapper;
import com.ltmap.halobiosmaintain.service.ISmallfishQualitativeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.service.ISmallfishQuantitativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 仔鱼定性表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class SmallfishQualitativeServiceImpl extends ServiceImpl<SmallfishQualitativeMapper, SmallfishQualitative> implements ISmallfishQualitativeService {
    @Resource
    private SmallfishQualitativeMapper smallfishQualitativeMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<SmallfishQualitative> lqw = Wrappers.lambdaQuery();
        lqw.eq(SmallfishQualitative::getReportId,reportId);
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
    public List<SmallfishQualitative> queryBiologicalType(String year, String voyage){
        List<SmallfishQualitative> smallfishQualitativeList=smallfishQualitativeMapper.queryBiologicalType(year,voyage);
        //去重
        List<SmallfishQualitative> smallfishQualitativeListNew= ListDistinctUtils.distinctSmallfishQualitativeByMap(smallfishQualitativeList);
        return smallfishQualitativeListNew;
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
        List<SmallfishQualitative> smallfishQualitativeList=smallfishQualitativeMapper.queryBiologicalType(year,voyage);
        List list = new ArrayList();
        for (int i = 0; i <smallfishQualitativeList.size() ; i++) {
            list.add(smallfishQualitativeList.get(i).getBiologicalChineseName());
        }
        return list;
    }

    /*
     * @Description:仔鱼定性数据
     * @Param smallfishQualitative:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:50
     */
    @Override
    public IPage<SmallfishQualitative> listSmallfishQualitative(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return smallfishQualitativeMapper.listSmallfishQualitative(page,stationName,  biologicalChineseName,  startDate,  endDate, reportId);
    }

}
