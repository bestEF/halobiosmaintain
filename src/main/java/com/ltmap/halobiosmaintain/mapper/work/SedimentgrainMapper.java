package com.ltmap.halobiosmaintain.mapper.work;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.ltmap.halobiosmaintain.entity.work.Sedimentgrain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 沉积物粒度表 Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface SedimentgrainMapper extends BaseMapper<Sedimentgrain> {

    IPage<Sedimentgrain> listSedimentgrain(IPage page, @Param("stationName")String stationName, @Param("startDate")String startDate, @Param("endDate")String endDate);

}
