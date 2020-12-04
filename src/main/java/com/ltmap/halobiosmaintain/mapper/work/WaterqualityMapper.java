package com.ltmap.halobiosmaintain.mapper.work;

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
}
