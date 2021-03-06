package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.IntertidalzonebiologicalQuantitative;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 大型浮游动物_I型网_表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface LargezooplanktonInetMapper extends BaseMapper<LargezooplanktonInet> {

    List<LargezooplanktonInet> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId") Long stationId);

    IPage<LargezooplanktonInet> listLargezooplanktonInet(IPage page, @Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
