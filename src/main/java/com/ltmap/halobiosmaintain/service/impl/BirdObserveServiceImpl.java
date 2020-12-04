package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
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


    @Override
    public IPage<BirdObserve> listBirdObserve(Integer current, Integer size, String protecName, String place, String startDate, String endDate){
        IPage<BirdObserve> page=new Page<>(current, size);
        return birdObserveMapper.listBirdObserve(page,protecName,place,startDate,endDate);
    }
}
