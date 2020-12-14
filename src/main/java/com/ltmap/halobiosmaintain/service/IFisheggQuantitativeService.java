package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 鱼卵定量表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IFisheggQuantitativeService extends IService<FisheggQuantitative> {

    /**
     * 根据填报id删除对应所有数据
     * @param reportId
     */
    Boolean deleteByReportId(Long reportId);
    List<FisheggQuantitative> queryBiologicalType(String year, String voyage);

    List<String> statisticTypeFromOneMap(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalDensity(String year, String voyage);

    HashMap<String,BigDecimal> queryBiologicalDensityByStation(String year, String voyage,Long stationId);

    HashMap<String,BigDecimal> queryBiologicalDensityOneYear(String year, String voyage);

    IPage<FisheggQuantitative> listFisheggQuantitative(Integer current, Integer size,String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId);
}
