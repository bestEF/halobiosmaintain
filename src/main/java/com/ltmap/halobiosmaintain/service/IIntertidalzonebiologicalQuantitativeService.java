package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.IntertidalzonebiologicalQuantitative;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 潮间带生物定量表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IIntertidalzonebiologicalQuantitativeService extends IService<IntertidalzonebiologicalQuantitative> {

    List<IntertidalzonebiologicalQuantitative> queryBiologicalType(String year, String voyage);
    HashMap<String,Integer>  statisticTypeFromOneMap(String year, String voyage);
    BigDecimal queryBiologicalDensity(String year, String voyage);
    BigDecimal queryBiologicalBiomass(String year, String voyage);
    BigDecimal queryBiologicalDensityByStation(String year, String voyage,Long stationId);
    BigDecimal queryBiologicalBiomassByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalBiomassOneYear(String year, String voyage);

    IPage<IntertidalzonebiologicalQuantitative> listIntertidalzonebiologicalQuantitative(Integer current, Integer size,String stationName, String biologicalChineseName, String startDate, String endDate);
}
