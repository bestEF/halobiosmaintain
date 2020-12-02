package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.entity.main.SysLog;
import com.ltmap.halobiosmaintain.mapper.main.SysLogMapper;
import com.ltmap.halobiosmaintain.service.ISysLogService;
import com.ltmap.halobiosmaintain.vo.resp.SysLogResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    //日志持久化
    public void saveLog(String userId, String businessName, String opType){
        SysLog sysLog = new SysLog();
        sysLog.setBusinessName(businessName);
        sysLog.setOpType(opType);
        sysLog.setUserId(userId);
        sysLog.setOperatTime(new Date());
        save(sysLog);
    }

    //清空所有日志
    public void clearSysLog(){
        sysLogMapper.clearSysLog();
    }

    //查询所有日志
    public IPage<SysLogResp> listSysLog(
            Integer current,
            Integer size,
            String userName,
            String startDate,
            String endDate){

        if(StringUtils.isNotBlank(userName)){
            userName = StringUtils.wrap(userName, "%");
        }

        IPage<SysLogResp> sysLogRespIPage = sysLogMapper.listSysLog(
                new Page<SysLogResp>(current, size),
                userName,
                startDate,
                endDate);

        return sysLogRespIPage;
    }
}
