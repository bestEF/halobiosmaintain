package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 水质表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface WaterqualityMapper extends BaseMapper<Waterquality> {

    List<Waterquality>  waterQualitystatisticOneYear(@Param("year") String year, @Param("voyage") String voyage);

    IPage<Waterquality> listWaterquality(IPage page, @Param("stationName")String stationName, @Param("startDate")String startDate, @Param("endDate")String endDate);

    List<Waterquality> waterQualitystatisticStationOneYear(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId")Long stationId);

}
