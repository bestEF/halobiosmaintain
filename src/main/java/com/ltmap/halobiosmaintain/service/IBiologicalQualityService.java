package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.entity.work.BiologicalQuality;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * <p>
 * 生物质量表 服务类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
public interface IBiologicalQualityService extends IService<BiologicalQuality> {

    HashMap<String, HashMap<String, BigDecimal>> biologicalQualityRangeOneYear(String year, String voyage, String element);
}
