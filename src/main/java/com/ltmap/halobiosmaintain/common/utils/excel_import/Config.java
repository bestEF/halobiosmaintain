package com.ltmap.halobiosmaintain.common.utils.excel_import;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author fjh
 * @date 2020/11/30 21:42
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Config {

    private String excelRules;
    private String excelUpLoad;
    private String excelJson;
    private String excelExport;

    public String getExcelRules() {
        return excelRules;
    }
    public void setExcelRules(String excelRules) {
        this.excelRules = excelRules;
    }

    public String getExcelUpLoad() {
        try{
            return new File("").getCanonicalPath()+excelUpLoad;
        }catch (IOException e){
            return null;
        }
    }
    public void setExcelUpLoad(String excelUpLoad) {
        this.excelUpLoad = excelUpLoad;
    }

    public String getExcelJson() {
        return excelJson;
    }
    public void setExcelJson(String excelJson) {
        this.excelJson = excelJson;
    }

    public String getExcelExport() {
        return excelExport;
    }
    public void setExcelExport(String excelExport) {
        this.excelExport = excelExport;
    }
}