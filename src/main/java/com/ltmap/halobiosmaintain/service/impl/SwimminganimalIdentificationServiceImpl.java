package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;
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


}
