package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 大型底栖动物定性表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface MacrobenthosQualitativeMapper extends BaseMapper<MacrobenthosQualitative> {

    List<MacrobenthosQualitative> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId") Long stationId);

    IPage<MacrobenthosQualitative> listMacrobenthosQualitative(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate);


}
