package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.SmallfishQualitative;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仔鱼定性表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ISmallfishQualitativeService extends IService<SmallfishQualitative> {

    List<SmallfishQualitative> queryBiologicalType(String year, String voyage);
    List<String> statisticTypeFromOneMap(String year, String voyage);

    IPage<SmallfishQualitative> listSmallfishQualitative(Integer current, Integer size,String stationName, String biologicalChineseName, String startDate, String endDate);

    Boolean deleteByReportId(Long reportId);
}
