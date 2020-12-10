package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
     * 保存植被主表
     * @param vegetationSurvey
     * @return
     */
    public Long addBirdObserve(VegetationSurvey vegetationSurvey){
        LambdaQueryWrapper<VegetationSurvey> lqw = Wrappers.lambdaQuery();
        lqw.eq(VegetationSurvey::getProtecName,vegetationSurvey.getProtecName());
        lqw.eq(VegetationSurvey::getSurveyDate,vegetationSurvey.getSurveyDate());
        lqw.eq(VegetationSurvey::getWeather,vegetationSurvey.getWeather());
        lqw.eq(VegetationSurvey::getPlace,vegetationSurvey.getPlace());
        lqw.eq(VegetationSurvey::getNum,vegetationSurvey.getNum());
        lqw.eq(VegetationSurvey::getArea,vegetationSurvey.getArea());
        lqw.eq(VegetationSurvey::getHigh,vegetationSurvey.getHigh());
        lqw.eq(VegetationSurvey::getLon,vegetationSurvey.getLon());
        lqw.eq(VegetationSurvey::getLat,vegetationSurvey.getLat());
        lqw.eq(VegetationSurvey::getHabitatType,vegetationSurvey.getHabitatType());
        lqw.eq(VegetationSurvey::getHumanType,vegetationSurvey.getHumanType());
        lqw.eq(VegetationSurvey::getHumanIntensity,vegetationSurvey.getHumanIntensity());
        lqw.eq(VegetationSurvey::getThreatenedFactors,vegetationSurvey.getThreatenedFactors());
        lqw.eq(VegetationSurvey::getVegetationType,vegetationSurvey.getVegetationType());
        lqw.eq(VegetationSurvey::getDominantSpecies,vegetationSurvey.getDominantSpecies());
        lqw.eq(VegetationSurvey::getTotalCoverage,vegetationSurvey.getTotalCoverage());
        lqw.eq(VegetationSurvey::getSurveyName,vegetationSurvey.getSurveyName());

        VegetationSurvey VegetationSurveyRe = vegetationSurveyMapper.selectOne(lqw);

        if(ObjectUtils.isEmpty(VegetationSurveyRe)){
            boolean saveFlag = save(vegetationSurvey);
            return vegetationSurvey.getId();
        }else {
            return VegetationSurveyRe.getId();
        }
    }

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
