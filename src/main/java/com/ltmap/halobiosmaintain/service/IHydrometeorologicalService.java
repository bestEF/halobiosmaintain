package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.Hydrometeorological;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;

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
}
