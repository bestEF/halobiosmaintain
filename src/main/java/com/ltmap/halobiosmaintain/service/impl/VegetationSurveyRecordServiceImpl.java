package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.work.BirdObserveRecord;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurveyRecord;
import com.ltmap.halobiosmaintain.mapper.work.VegetationSurveyRecordMapper;
import com.ltmap.halobiosmaintain.service.IVegetationSurveyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 植被调查记录子表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Service
public class VegetationSurveyRecordServiceImpl extends ServiceImpl<VegetationSurveyRecordMapper, VegetationSurveyRecord> implements IVegetationSurveyRecordService {

    @Resource
    private VegetationSurveyRecordMapper vegetationSurveyRecordMapper;

    @Override
    public IPage<VegetationSurveyRecord> listVegetationSurveyRecord(Integer current, Integer size, String chineseName,Long id){
        LambdaQueryWrapper<VegetationSurveyRecord> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(!Strings.isNullOrEmpty(chineseName),VegetationSurveyRecord::getChineseName,chineseName)
        .eq(id!=null,VegetationSurveyRecord::getId,id);
        IPage<VegetationSurveyRecord> page=new Page<>(current, size);
        return vegetationSurveyRecordMapper.selectPage(page,queryWrapper);
    }
}
