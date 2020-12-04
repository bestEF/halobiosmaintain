package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
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
        for (int i = 0; i <fisheggQualitativeList.size() ; i++) {
            list.add(fisheggQualitativeList.get(i).getBiologicalChineseName());
        }
        return list;
    }


}
