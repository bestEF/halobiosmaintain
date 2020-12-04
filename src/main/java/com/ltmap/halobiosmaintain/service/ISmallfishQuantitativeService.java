package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 仔鱼定量表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ISmallfishQuantitativeService extends IService<SmallfishQuantitative> {

    List<SmallfishQuantitative> queryBiologicalType(String year, String voyage);
    List<String> statisticTypeFromOneMap(String year, String voyage);

    BigDecimal queryBiologicalDensity(String year, String voyage);

    BigDecimal queryBiologicalDensityByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage);
}
