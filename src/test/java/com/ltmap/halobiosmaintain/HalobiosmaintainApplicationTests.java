package com.ltmap.halobiosmaintain;

import com.ltmap.halobiosmaintain.entity.TblTest;
import com.ltmap.halobiosmaintain.service.ITblTestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class HalobiosmaintainApplicationTests {
    @Resource
    private ITblTestService testService;

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

    @Test
    void testBigDecimal(){
        String a="0.351";
        Double b=0.301;
        BigDecimal c = new BigDecimal(0.351);
        BigDecimal bigDecimal1 = new BigDecimal(a,new MathContext(3, RoundingMode.HALF_UP));
        BigDecimal bigDecimal2 = new BigDecimal(b,new MathContext(3, RoundingMode.HALF_UP));
        BigDecimal round = c.round(new MathContext(3, RoundingMode.HALF_UP));
        System.out.println(bigDecimal1);
        System.out.println(bigDecimal2);
        System.out.println(round);
    }

}
