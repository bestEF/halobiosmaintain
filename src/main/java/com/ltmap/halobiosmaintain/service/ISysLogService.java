package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.main.SysLog;
import com.ltmap.halobiosmaintain.vo.resp.SysLogResp;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 清空所有日志
     * @return
     */
    void clearSysLog();

    /**查询所有日志
     * @param current
     * @param size
     * @param userName
     * @param startDate
     * @param endDate
     * @return
     */
    IPage<SysLogResp> listSysLog(
            Integer current,
            Integer size,
            String userName,
            String startDate,
            String endDate);

    /**
     * 日志持久化
     * @param userId
     * @param businessName
     * @param opType
     */
    void saveLog(String userId, String businessName, String opType);
}
