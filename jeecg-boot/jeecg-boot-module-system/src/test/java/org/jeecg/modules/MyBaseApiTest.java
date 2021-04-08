package org.jeecg.modules;

import org.apache.shiro.SecurityUtils;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.system.api.IMyBaseAPI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author motb
 * @date 2021/4/6 14:34
 * @description //TODO MyBaseApiTest
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
public class MyBaseApiTest {

    @Autowired
    private IMyBaseAPI myBaseAPI;

    @Test
    public void getAutoRuleCode() {
        String code = myBaseAPI.getAutoRuleCode("code", "contract_purchase");
        System.out.println(code);
        Assert.assertNotNull(code);
    }
}
