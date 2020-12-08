package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurvey;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurveyRecord;
import com.ltmap.halobiosmaintain.mapper.work.VegetationSurveyMapper;
import com.ltmap.halobiosmaintain.service.IVegetationSurveyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 植被调查记录主表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
@Service
public class VegetationSurveyServiceImpl extends ServiceImpl<VegetationSurveyMapper, VegetationSurvey> implements IVegetationSurveyService {

    @Resource
    private VegetationSurveyMapper vegetationSurveyMapper;



    /**
     * @Description:植被调查记录主表查询_数据管理
     * @Param current:
     * @Param size:
     * @Param protecName:
     * @Param place:
     * @Param startDate:
     * @Param endDate:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/8 17:15
     */
    @Override
    public IPage<VegetationSurvey> listVegetationSurvey(Integer current, Integer size, String protecName, String place, String startDate, String endDate){
        IPage<VegetationSurvey> page=new Page<>(current, size);
        return vegetationSurveyMapper.listVegetationSurvey(page,protecName,place,startDate,endDate);
    }
}
