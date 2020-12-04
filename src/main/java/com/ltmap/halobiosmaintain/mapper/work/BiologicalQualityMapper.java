package com.ltmap.halobiosmaintain.mapper.work;

import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
