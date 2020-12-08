package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.ltmap.halobiosmaintain.entity.work.Waterquality;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 水质表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IWaterqualityService extends IService<Waterquality> {

    HashMap<String,HashMap<String,BigDecimal>> waterQualitystatisticOneYear(String year, String voyage,String element);

    HashMap<String,HashMap<String,BigDecimal>> waterQualityOrder(String year, String voyage);

    IPage<Waterquality> listWaterquality(Integer current, Integer size,String stationName, String startDate, String endDate);

    Boolean deleteByReportId(Long reportId);
}
