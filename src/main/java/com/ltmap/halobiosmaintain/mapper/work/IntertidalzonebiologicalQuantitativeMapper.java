package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.entity.work.IntertidalzonebiologicalQuantitative;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 潮间带生物定量表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IntertidalzonebiologicalQuantitativeMapper extends BaseMapper<IntertidalzonebiologicalQuantitative> {

    List<IntertidalzonebiologicalQuantitative> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId") Long stationId);

    IPage<IntertidalzonebiologicalQuantitative> listIntertidalzonebiologicalQuantitative(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
