package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQualitative;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 大型底栖动物定性表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IMacrobenthosQualitativeService extends IService<MacrobenthosQualitative> {

    List<MacrobenthosQualitative> queryBiologicalType(String year, String voyage);

    List<String> statisticTypeFromOneMap(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalDensity(String year, String voyage);
    HashMap<String,BigDecimal> queryBiologicalBiomass(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalDensityByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal>  queryBiologicalBiomassByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalBiomassOneYear(String year, String voyage);

    IPage<MacrobenthosQualitative> listMacrobenthosQualitative(Integer current, Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId);

    Boolean deleteByReportId(Long reportId);
}
