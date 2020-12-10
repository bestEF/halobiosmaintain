package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQualitative;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仔鱼定性表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SmallfishQualitativeMapper extends BaseMapper<SmallfishQualitative> {

    List<SmallfishQualitative> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage);

    IPage<SmallfishQualitative> listSmallfishQualitative(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
