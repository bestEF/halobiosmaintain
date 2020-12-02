package com.ltmap.halobiosmaintain;

import com.ltmap.halobiosmaintain.entity.TblTest;
import com.ltmap.halobiosmaintain.service.ITblTestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class HalobiosmaintainApplicationTests {
    @Resource
    private ITblTestService testService;

    @Test
    void contextLoads() {
    }

    /**
     * 测试连接数据库
     */
    @Test
    void testMP(){
        TblTest tblTest = new TblTest();
        tblTest.setTestName("测试");
        tblTest.setTestDateTime(LocalDateTime.now());
        boolean saveFlag = testService.save(tblTest);
        System.out.println(saveFlag);
        List<TblTest> list = testService.list();
        for (TblTest test:list
             ) {
            System.out.println(test);
        }
    }
}
