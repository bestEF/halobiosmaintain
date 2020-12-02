package com.ltmap.halobiosmaintain.common.utils.excel_import;

/**
 * excel校验结果返回类
 * @author fjh
 * @date 2020/11/30 20:34
 */
public class ExcelCheck {
    private String fileName;
    private String result;
    private String code;
    private Boolean right;
    private Boolean hasNote;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    public Boolean getHasNote() {
        return hasNote;
    }

    public void setHasNote(Boolean hasNote) {
        this.hasNote = hasNote;
    }

    public ExcelCheck(){
        this.result="";
        this.code="";
        this.right=false;
        this.hasNote=false;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}