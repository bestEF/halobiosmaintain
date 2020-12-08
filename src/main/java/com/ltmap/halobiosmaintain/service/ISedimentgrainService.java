package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.Sedimentgrain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 沉积物粒度表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ISedimentgrainService extends IService<Sedimentgrain> {

    IPage<Sedimentgrain> listSedimentgrain(Integer current, Integer size,String stationName, String startDate, String endDate);

    Boolean deleteByReportId(Long reportId);
}
