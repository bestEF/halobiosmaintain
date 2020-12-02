package com.ltmap.halobiosmaintain.vo.resp;

import com.ltmap.halobiosmaintain.entity.main.SysLog;
import lombok.Data;

/**
 * 系统日志返回RespVo
 */
@Data
public class SysLogResp extends SysLog {
    String userName;            //调用人
}
