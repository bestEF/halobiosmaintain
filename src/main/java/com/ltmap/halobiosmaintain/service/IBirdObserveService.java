package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 鸟类观测记录主表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
public interface IBirdObserveService extends IService<BirdObserve> {

    IPage<BirdObserve> listBirdObserve(Integer current, Integer size, String protecName, String place, String startDate, String endDate);

    /**
     * 保存鸟类主表
     * @param birdObserve
     * @return
     */
    Long addBirdObserve(BirdObserve birdObserve);
}
