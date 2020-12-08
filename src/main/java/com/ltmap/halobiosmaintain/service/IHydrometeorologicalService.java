package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.ltmap.halobiosmaintain.entity.work.Hydrometeorological;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 水文气象表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IHydrometeorologicalService extends IService<Hydrometeorological> {

    HashMap<String, HashMap<String, BigDecimal>> hydrometeorologicalRangeOneYear(String year, String voyage, String element);

    IPage<Hydrometeorological> listHydrometeorological(Integer current, Integer size,String stationName, String startDate, String endDate);

    Boolean deleteByReportId(Long reportId);
}
