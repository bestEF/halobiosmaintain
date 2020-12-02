package com.ltmap.halobiosmaintain.common.utils.excel_import;

/**
 * 判断Excel的版本是2003还是2007的工具类
 * @author fjh
 * @date 2020/12/1 10:59
 * @email 1562106220@qq.com
 */
public class WDWUtil {

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}