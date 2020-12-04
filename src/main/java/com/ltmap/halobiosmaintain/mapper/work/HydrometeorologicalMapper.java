package com.ltmap.halobiosmaintain.mapper.work;

import com.ltmap.halobiosmaintain.entity.work.Hydrometeorological;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 水文气象表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface HydrometeorologicalMapper extends BaseMapper<Hydrometeorological> {

    List<Hydrometeorological> hydrometeorologicalRangeOneYear(@Param("year") String year, @Param("voyage") String voyage);
}
