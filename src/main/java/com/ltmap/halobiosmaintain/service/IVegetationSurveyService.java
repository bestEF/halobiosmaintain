package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurvey;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 植被调查记录主表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
public interface IVegetationSurveyService extends IService<VegetationSurvey> {

    IPage<VegetationSurvey> listVegetationSurvey(Integer current, Integer size, String protecName, String place, String startDate, String endDate);

    /**
     * 保存植被主表
     * @param vegetationSurvey
     * @return
     */
    Long addBirdObserve(VegetationSurvey vegetationSurvey);
}
