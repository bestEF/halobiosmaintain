package com.ltmap.halobiosmaintain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author fjh
 * @since 2020-11-13
 */
@TableName("tbl_test")
public class TblTest extends Model<TblTest> {

    private static final long serialVersionUID=1L;

    /**
     * 测试表id
     */
    @TableId(value = "testId", type = IdType.ID_WORKER_STR)
    private String testId;

    /**
     * 测试表名
     */
    @TableField("testName")
    private String testName;

    /**
     * 测试时间
     */
    @TableField("testDateTime")
    private LocalDateTime testDateTime;

    public LocalDateTime getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(LocalDateTime testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Override
    protected Serializable pkVal() {
        return this.testId;
    }

    @Override
    public String toString() {
        return "TblTest{" +
                "testId='" + testId + '\'' +
                ", testName='" + testName + '\'' +
                ", testDateTime='" + testDateTime + '\'' +
                '}';
    }
}
