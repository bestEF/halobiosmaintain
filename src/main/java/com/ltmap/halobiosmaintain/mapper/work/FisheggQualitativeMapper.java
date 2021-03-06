package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.FisheggQualitative;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 鱼卵定性表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface FisheggQualitativeMapper extends BaseMapper<FisheggQualitative> {

    List<FisheggQualitative> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage);

    IPage<FisheggQualitative> listFisheggQualitative(IPage page,@Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
