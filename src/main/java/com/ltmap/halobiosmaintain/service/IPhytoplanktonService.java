package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.IntertidalzonebiologicalQuantitative;
import com.ltmap.halobiosmaintain.entity.work.Phytoplankton;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQualitative;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 浮游植物表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IPhytoplanktonService extends IService<Phytoplankton> {

    List<Phytoplankton> queryBiologicalType(String year, String voyage);

    HashMap<String,Integer> statisticTypeFromOneMap(String year, String voyage);

    IPage<Phytoplankton> listPhytoplankton(Integer current, Integer size,String stationName, String biologicalChineseName, String startDate, String endDate);

    Boolean deleteByReportId(Long reportId);
}
