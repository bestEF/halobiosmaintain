package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.ltmap.halobiosmaintain.entity.work.SmallzooplanktonIinet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小型浮游动物（II型网）表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SmallzooplanktonIinetMapper extends BaseMapper<SmallzooplanktonIinet> {

    List<SmallzooplanktonIinet> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId") Long stationId);

    IPage<SmallzooplanktonIinet> listSmallzooplanktonIinet(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
