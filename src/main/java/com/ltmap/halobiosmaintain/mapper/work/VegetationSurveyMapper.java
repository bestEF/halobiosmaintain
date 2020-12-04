package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.entity.work.VegetationSurvey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 植被调查记录主表 Mapper 接口
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
public interface VegetationSurveyMapper extends BaseMapper<VegetationSurvey> {

    IPage<VegetationSurvey> listVegetationSurvey(IPage page, @Param("protecName")String protecName, @Param("place")String place, @Param("startDate")String startDate, @Param("endDate")String endDate);

}
