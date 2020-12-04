package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.FisheggQualitative;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 鱼卵定性表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IFisheggQualitativeService extends IService<FisheggQualitative> {

    List<FisheggQualitative> queryBiologicalType(String year, String voyage);
    List<String> statisticTypeFromOneMap(String year, String voyage);

    IPage<FisheggQualitative> listFisheggQualitative(Integer current, Integer size, String stationName, String biologicalChineseName, String startDate, String endDate);
}
