package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.common.utils.ListDistinctUtils;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
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

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<Phytoplankton> lqw = Wrappers.lambdaQuery();
        lqw.eq(Phytoplankton::getReportId,reportId);
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

    /*
     * @Description:浮游植物数据
     * @Param phytoplankton:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:49
     */
    @Override
    public IPage<Phytoplankton> listPhytoplankton(Integer current,Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return phytoplanktonMapper.listPhytoplankton(page,stationName,  biologicalChineseName,  startDate,  endDate, reportId);
    }
}
