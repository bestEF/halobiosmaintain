package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.Sediment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;

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
}
