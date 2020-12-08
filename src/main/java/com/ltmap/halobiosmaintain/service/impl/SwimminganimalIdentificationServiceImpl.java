package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.mapper.work.SwimminganimalIdentificationMapper;
import com.ltmap.halobiosmaintain.service.ISmallfishQuantitativeService;
import com.ltmap.halobiosmaintain.service.ISwimminganimalIdentificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 游泳动物生物鉴定表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class SwimminganimalIdentificationServiceImpl extends ServiceImpl<SwimminganimalIdentificationMapper, SwimminganimalIdentification> implements ISwimminganimalIdentificationService {

    @Resource
    private SwimminganimalIdentificationMapper swimminganimalIdentificationMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<SwimminganimalIdentification> lqw = Wrappers.lambdaQuery();
        lqw.eq(SwimminganimalIdentification::getReportId,reportId);
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
    public List<SwimminganimalIdentification> queryBiologicalType(String year, String voyage){
        List<SwimminganimalIdentification> swimminganimalIdentificationList=swimminganimalIdentificationMapper.queryBiologicalType(year,voyage);
        //去重
        List<SwimminganimalIdentification> swimminganimalIdentificationListNew= ListDistinctUtils.distinctSwimminganimalIdentificationByMap(swimminganimalIdentificationList);
        return swimminganimalIdentificationListNew;
    }

    /*
     * @Description:生物组成
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 10:20
     */
    @Override
    public HashMap<String,Integer> statisticTypeFromOneMap(String year, String voyage){
        List<SwimminganimalIdentification> swimminganimalIdentificationList=swimminganimalIdentificationMapper.queryBiologicalType(year,voyage);
        List list = new ArrayList();
        HashMap<String,Integer> swimminganimalIdentificationMap=new HashMap<String,Integer>();
        for (int i = 0; i <swimminganimalIdentificationList.size() ; i++) {
            list.add(swimminganimalIdentificationList.get(i).getSpecificName());
        }
        Set uniqueSet = new HashSet(list);
        for (Object temp:uniqueSet
        ) {
            swimminganimalIdentificationMap.put(temp.toString(),Collections.frequency(list, temp));
        }
        return swimminganimalIdentificationMap;
    }

    /*
     * @Description:游泳动物数据
     * @Param swimminganimalIdentification:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:52
     */
    @Override
    public IPage<SwimminganimalIdentification> listSwimminganimalIdentification(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return swimminganimalIdentificationMapper.listSwimminganimalIdentification(page,stationName,  biologicalChineseName,  startDate,  endDate);
    }

}
