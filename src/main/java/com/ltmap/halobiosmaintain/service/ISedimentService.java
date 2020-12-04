package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.Hydrometeorological;
import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 沉积物表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ISedimentService extends IService<Sediment> {

    HashMap<String, HashMap<String, BigDecimal>> sedimentstatisticOneYear(String year, String voyage, String element);

    IPage<Sediment> listSediment(Integer current, Integer size,String stationName, String startDate, String endDate);
}
