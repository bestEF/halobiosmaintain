package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.mapper.work.BirdObserveMapper;
import com.ltmap.halobiosmaintain.service.IBirdObserveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 鸟类观测记录主表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Service
public class BirdObserveServiceImpl extends ServiceImpl<BirdObserveMapper, BirdObserve> implements IBirdObserveService {
    @Resource
    private BirdObserveMapper birdObserveMapper;

    /**
     * 保存鸟类主表
     * @param birdObserve
     * @return
     */
    public Long addBirdObserve(BirdObserve birdObserve){
        LambdaQueryWrapper<BirdObserve> lqw = Wrappers.lambdaQuery();
        lqw.eq(BirdObserve::getProtecName,birdObserve.getProtecName());
        lqw.eq(BirdObserve::getObserveDate,birdObserve.getObserveDate());
        lqw.eq(BirdObserve::getWeather,birdObserve.getWeather());
        lqw.eq(BirdObserve::getTem,birdObserve.getTem());
        lqw.eq(BirdObserve::getPlace,birdObserve.getPlace());
        lqw.eq(BirdObserve::getHigh,birdObserve.getHigh());
        lqw.eq(BirdObserve::getObserveName,birdObserve.getObserveName());
        lqw.eq(BirdObserve::getRecordName,birdObserve.getRecordName());
        lqw.eq(BirdObserve::getSplineNumber,birdObserve.getSplineNumber());
        lqw.eq(BirdObserve::getSplineLength,birdObserve.getSplineLength());
        lqw.eq(BirdObserve::getStartTime,birdObserve.getStartTime());
        lqw.eq(BirdObserve::getEndTime,birdObserve.getEndTime());
        lqw.eq(BirdObserve::getStartLon,birdObserve.getStartLon());
        lqw.eq(BirdObserve::getEndLon,birdObserve.getEndLon());
        lqw.eq(BirdObserve::getStartLat,birdObserve.getStartLat());
        lqw.eq(BirdObserve::getEndLat,birdObserve.getEndLat());
        lqw.eq(BirdObserve::getTotalSpecies,birdObserve.getTotalSpecies());
        lqw.eq(BirdObserve::getHabitatType,birdObserve.getHabitatType());
        lqw.eq(BirdObserve::getHumanType,birdObserve.getHumanType());
        lqw.eq(BirdObserve::getHumanIntensity,birdObserve.getHumanIntensity());
        lqw.eq(BirdObserve::getThreatenedFactors,birdObserve.getThreatenedFactors());

        BirdObserve birdObserveResp = birdObserveMapper.selectOne(lqw);

        if(ObjectUtils.isEmpty(birdObserveResp)){
            boolean saveFlag = save(birdObserve);
            return birdObserve.getId();
        }else {
            return birdObserveResp.getId();
        }

    }

    @Override
    public IPage<BirdObserve> listBirdObserve(Integer current, Integer size, String protecName, String place, String startDate, String endDate){
        IPage<BirdObserve> page=new Page<>(current, size);
        return birdObserveMapper.listBirdObserve(page,protecName,place,startDate,endDate);
    }
}
