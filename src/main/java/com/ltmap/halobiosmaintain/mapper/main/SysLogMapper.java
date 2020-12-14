package com.ltmap.halobiosmaintain.mapper.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.entity.main.SysLog;
import com.ltmap.halobiosmaintain.vo.resp.SysLogResp;
import org.apache.ibatis.annotations.Param;

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
            @Param("userName") String userName,
            @Param("startDate")String startDate,
            @Param("endDate")String endDate);

    /**
     * 清空日志
     * @return
     */
    void clearSysLog();
}
