package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.entity.work.BirdObserveRecord;
import com.ltmap.halobiosmaintain.mapper.work.BirdObserveMapper;
import com.ltmap.halobiosmaintain.mapper.work.BirdObserveRecordMapper;
import com.ltmap.halobiosmaintain.service.IBirdObserveRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 鸟类观测记录子表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Service
public class BirdObserveRecordServiceImpl extends ServiceImpl<BirdObserveRecordMapper, BirdObserveRecord> implements IBirdObserveRecordService {

    @Resource
    private BirdObserveRecordMapper birdObserveRecordMapper;


    @Override
    public IPage<BirdObserveRecord> listBirdObserveRecord(Integer current, Integer size, String chineseName,Long id){
        LambdaQueryWrapper<BirdObserveRecord> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(!Strings.isNullOrEmpty(chineseName),BirdObserveRecord::getChineseName,chineseName)
        .eq(id!=null,BirdObserveRecord::getId,id);
        IPage<BirdObserveRecord> page=new Page<>(current, size);
        return birdObserveRecordMapper.selectPage(page,queryWrapper);
    }
}
