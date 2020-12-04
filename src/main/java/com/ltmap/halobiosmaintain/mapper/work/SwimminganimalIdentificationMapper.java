package com.ltmap.halobiosmaintain.mapper.work;

import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 游泳动物生物鉴定表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SwimminganimalIdentificationMapper extends BaseMapper<SwimminganimalIdentification> {

    List<SwimminganimalIdentification> queryBiologicalType(@Param("year") String year, @Param("voyage") String voyage);
}
