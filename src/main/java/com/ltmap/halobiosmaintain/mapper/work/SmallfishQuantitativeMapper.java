package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQualitative;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仔鱼定量表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SmallfishQuantitativeMapper extends BaseMapper<SmallfishQuantitative> {

    List<SmallfishQuantitative> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId") Long stationId);

    IPage<SmallfishQuantitative> listSmallfishQuantitative(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
