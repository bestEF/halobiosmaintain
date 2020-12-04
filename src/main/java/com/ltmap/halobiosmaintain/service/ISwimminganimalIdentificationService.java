package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.SmallfishQuantitative;
import com.ltmap.halobiosmaintain.entity.work.SwimminganimalIdentification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 游泳动物生物鉴定表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface ISwimminganimalIdentificationService extends IService<SwimminganimalIdentification> {

    List<SwimminganimalIdentification> queryBiologicalType(String year, String voyage);
    HashMap<String,Integer> statisticTypeFromOneMap(String year, String voyage);
}
