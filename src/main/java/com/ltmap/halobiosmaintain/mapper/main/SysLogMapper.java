package com.ltmap.halobiosmaintain.mapper.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.main.SysLog;
import com.ltmap.halobiosmaintain.vo.resp.SysLogResp;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

    /**
     * 查询所有日志
     * @param iPage
     * @param userName
     * @param startDate
     * @param endDate
     * @return
     */
    IPage<SysLogResp> listSysLog(
            IPage<SysLogResp> iPage,
            String userName,
            String startDate,
            String endDate);

    /**
     * 清空日志
     * @return
     */
    void clearSysLog();
}
