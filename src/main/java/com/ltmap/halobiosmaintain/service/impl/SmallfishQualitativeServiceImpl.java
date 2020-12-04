package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
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
}
