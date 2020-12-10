package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.work.SmallzooplanktonIinet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 生物质量表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IBiologicalQualityService extends IService<BiologicalQuality> {

    HashMap<String, HashMap<String, BigDecimal>> biologicalQualityRangeOneYear(String year, String voyage, String element);

    HashMap<String, HashMap<String, String>> biologicalQualityOrder(String year, String voyage);

    IPage<BiologicalQuality> listBiologicalQuality(Integer current, Integer size, String stationName, String biologicalChineseName, String startDate, String endDate,Long reportId);

    /**
     * 根据填报id删除对应所有数据
     * @param reportId
     * @return
     */
    Boolean deleteByReportId(Long reportId);
}
