package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurveyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 植被调查记录子表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
public interface IVegetationSurveyRecordService extends IService<VegetationSurveyRecord> {

    IPage<VegetationSurveyRecord> listVegetationSurveyRecord(Integer current, Integer size, String chineseName,Long id);
}
