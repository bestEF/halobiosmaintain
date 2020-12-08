package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 生物质量表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface BiologicalQualityMapper extends BaseMapper<BiologicalQuality> {

    List<BiologicalQuality> biologicalQualityRangeOneYear(@Param("year") String year, @Param("voyage") String voyage);

    IPage<BiologicalQuality> listBiologicalQuality(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate);

    List<BiologicalQuality> biologicalQualityStationOneYear(@Param("year") String year, @Param("voyage") String voyage, @Param("stationId")Long stationId);


}
