package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.LargezooplanktonInet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 大型浮游动物_I型网_表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ILargezooplanktonInetService extends IService<LargezooplanktonInet> {

    List<LargezooplanktonInet> queryBiologicalType(String year, String voyage);
    List<String> statisticTypeFromOneMap(String year, String voyage);
    BigDecimal queryBiologicalDensity(String year, String voyage);
    BigDecimal queryBiologicalBiomass(String year, String voyage);
    BigDecimal queryBiologicalDensityByStation(String year, String voyage,Long stationId);
    BigDecimal queryBiologicalBiomassByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalBiomassOneYear(String year, String voyage);
}
