package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.Sedimentgrain;
import com.ltmap.halobiosmaintain.mapper.work.SedimentgrainMapper;
import com.ltmap.halobiosmaintain.service.ISedimentgrainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 沉积物粒度表 服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class SedimentgrainServiceImpl extends ServiceImpl<SedimentgrainMapper, Sedimentgrain> implements ISedimentgrainService {

    @Resource
    private SedimentgrainMapper sedimentgrainMapper;

    //根据填报id删除对应所有数据
    public Boolean deleteByReportId(Long reportId){
        LambdaQueryWrapper<Sedimentgrain> lqw = Wrappers.lambdaQuery();
        lqw.eq(Sedimentgrain::getReportId,reportId);
        boolean removeFlag = remove(lqw);
        return false;
    }

    /*
     * @Description:沉积物粒度数据
     * @Param sediment:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/4 8:49
     */
    @Override
    public IPage<Sedimentgrain> listSedimentgrain(Integer current,Integer size,String stationName, String startDate, String endDate,Long reportId){
        IPage<BiologicalQuality> page=new Page<>(current, size);
        return sedimentgrainMapper.listSedimentgrain(page,stationName,  startDate,  endDate,reportId);
    }
}
