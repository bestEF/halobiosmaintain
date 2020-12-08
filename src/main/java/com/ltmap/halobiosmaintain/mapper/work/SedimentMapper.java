package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.Sedimentgrain;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 沉积物表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SedimentMapper extends BaseMapper<Sediment> {

    List<Sediment> sedimentstatisticOneYear(@Param("year") String year, @Param("voyage") String voyage);

    IPage<Sediment> listSediment(IPage page, @Param("stationName")String stationName, @Param("startDate")String startDate, @Param("endDate")String endDate);

    List<Sediment> sedimentStationOneYear(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId")Long stationId);
}
