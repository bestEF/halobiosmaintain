package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.FisheggQualitative;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 鱼卵定量表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface FisheggQuantitativeMapper extends BaseMapper<FisheggQuantitative> {

    List<FisheggQuantitative> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage,@Param("stationId") Long stationId);

    IPage<FisheggQuantitative> listFisheggQuantitative(IPage page,@Param("stationName")String stationName, @Param("biologicalChineseName")String biologicalChineseName, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("reportId") Long reportId);

}
