package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.ltmap.halobiosmaintain.mapper.work.MacrobenthosQuantitativeMapper;
import com.ltmap.halobiosmaintain.mapper.work.PhytoplanktonMapper;
import com.ltmap.halobiosmaintain.service.IPhytoplanktonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 浮游植物表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class PhytoplanktonServiceImpl extends ServiceImpl<PhytoplanktonMapper, Phytoplankton> implements IPhytoplanktonService {

    @Resource
    private PhytoplanktonMapper phytoplanktonMapper;

    /*
     * @Description:查询生物种种类
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:51
     */
    @Override
    public List<Phytoplankton> queryBiologicalType(String year, String voyage){
        List<Phytoplankton> phytoplanktonList=phytoplanktonMapper.queryBiologicalType(year,voyage);
        //去重
        List<Phytoplankton> phytoplanktonListNew= ListDistinctUtils.distinctPhytoplanktonByMap(phytoplanktonList);
        return phytoplanktonListNew;
    }


    /*
     * @Description:生物组成
     * @Param year:
     * @Param voyage:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/2 10:21
     */
    @Override
    public HashMap<String,Integer> statisticTypeFromOneMap(String year, String voyage){
        List<Phytoplankton> phytoplanktonList=phytoplanktonMapper.queryBiologicalType(year,voyage);
        List list = new ArrayList();
        HashMap<String,Integer> phytoplanktonMap=new HashMap<String,Integer>();
        for (int i = 0; i <phytoplanktonList.size() ; i++) {
            list.add(phytoplanktonList.get(i).getCategory());
        }
        Set uniqueSet = new HashSet(list);
        for (Object temp:uniqueSet
             ) {
            phytoplanktonMap.put(temp.toString(),Collections.frequency(list, temp));
        }
        return phytoplanktonMap;
    }
}
