package com.ltmap.halobiosmaintain.mapper.work;

import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 沉积物表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SedimentMapper extends BaseMapper<Sediment> {

    List<Sediment> sedimentstatisticOneYear(@Param("year") String year, @Param("voyage") String voyage);
}
