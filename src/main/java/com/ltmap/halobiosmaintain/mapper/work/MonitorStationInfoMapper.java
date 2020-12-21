package com.ltmap.halobiosmaintain.mapper.work;

import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface MonitorStationInfoMapper extends BaseMapper<MonitorStationInfo> {

    List<MonitorStationInfo> queryStationInfo(@Param("year") String year, @Param("voyage") String voyage );

    List<MonitorStationInfo> queryStationInfoByDataType(@Param("year") String year, @Param("voyage") String voyage,@Param("dateType") String dateType );

}
