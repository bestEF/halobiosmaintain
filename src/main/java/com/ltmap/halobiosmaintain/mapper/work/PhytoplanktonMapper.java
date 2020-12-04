package com.ltmap.halobiosmaintain.mapper.work;

import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 浮游植物表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface PhytoplanktonMapper extends BaseMapper<Phytoplankton> {

    List<Phytoplankton> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage);
}
