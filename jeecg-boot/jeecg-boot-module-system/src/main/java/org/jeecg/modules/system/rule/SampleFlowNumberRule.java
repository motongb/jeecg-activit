package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.YouBianCodeUtil;
import org.springframework.util.StringUtils;

/**
 * @author motb
 * @date 2021/4/19 9:58
 * @description //TODO SampleFlowNumberRule
 **/
public class SampleFlowNumberRule implements IFillRuleHandler {
    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        String code = "";
        String lastCode = formData.getString("lastCode");
        int numberLength = formData.getIntValue("numberLength");
        if (StringUtils.hasText(lastCode)) {
            code = YouBianCodeUtil.getNextYouBianCode(lastCode, numberLength);
        } else {
            String firstNo = formData.getString("firstNo");
            code = StringUtils.isEmpty(firstNo) ? "1000" : YouBianCodeUtil.getNextYouBianCode(firstNo, numberLength);
        }
        return code;
    }
}
