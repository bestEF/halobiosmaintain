package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.ltmap.halobiosmaintain.entity.work.SmallzooplanktonIinet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 小型浮游动物（II型网）表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ISmallzooplanktonIinetService extends IService<SmallzooplanktonIinet> {

    List<SmallzooplanktonIinet> queryBiologicalType(String year, String voyage);
    List<String> statisticTypeFromOneMap(String year, String voyage);
    BigDecimal queryBiologicalDensity(String year, String voyage);

    BigDecimal queryBiologicalDensityByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage);

    IPage<SmallzooplanktonIinet> listSmallzooplanktonIinet(Integer current, Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId);

    Boolean deleteByReportId(Long reportId);
}
