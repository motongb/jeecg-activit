package org.jeecg.modules;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.util.WpsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author motb
 * @date 2021/3/9 9:52
 * @description //TODO WpsTest
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
public class WpsTest {
    @Autowired
    private WpsUtil wpsUtil;


    @Test
    public void replaceParagraph() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "一般采购合同");
        List<Map<String, Object>> tableData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "name" + i);
            map.put("code", "code" + i);
            map.put("year", "year" + i);
            tableData.add(map);
        }
        params.put("table0", tableData);
        Map<Integer, String> indexMap = new HashMap<>();
        indexMap.put(0, "table0");
        wpsUtil.replaceContent("temp/3_工程合同会签人员名单_1615193793207.docx",
                "temp/3_工程合同会签人员名单_1615251312381.docx", params, indexMap);
    }
}
