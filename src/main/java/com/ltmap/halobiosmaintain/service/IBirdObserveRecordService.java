package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.work.BirdObserve;
import com.ltmap.halobiosmaintain.entity.work.BirdObserveRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 鸟类观测记录子表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-12-04
 */
public interface IBirdObserveRecordService extends IService<BirdObserveRecord> {

    IPage<BirdObserveRecord> listBirdObserveRecord(Integer current, Integer size, String chineseName,Long id);
}
