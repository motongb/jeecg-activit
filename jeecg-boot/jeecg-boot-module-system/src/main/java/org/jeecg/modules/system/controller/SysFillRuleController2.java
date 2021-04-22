package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.system.entity.SysFillRule;
import org.jeecg.modules.system.service.ISysFillRuleService;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 填值规则, 前端js增强没有put方法，所以复制一份post方法
 * @Author: jeecg-boot
 * @Date: 2019-11-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "填值规则")
@RestController
@RequestMapping("/sys/fillRule")
public class SysFillRuleController2 extends JeecgController<SysFillRule, ISysFillRuleService> {

    /**
     * 通过 ruleCode 执行自定义填值规则
     *
     * @param ruleCode 要执行的填值规则编码
     * @param formData 表单数据，可根据表单数据的不同生成不同的填值结果
     * @return 运行后的结果
     */
    @PostMapping("/executeRuleByCode/{ruleCode}")
    public Result executeByRuleCode(@PathVariable("ruleCode") String ruleCode, @RequestBody JSONObject formData) {
        Object result = FillRuleUtil.executeRule(ruleCode, formData);
        return Result.ok(result);
    }


    /**
     * 批量通过 ruleCode 执行自定义填值规则
     *
     * @param ruleData 要执行的填值规则JSON数组：
     *                 示例： { "commonFormData": {}, rules: [ { "ruleCode": "xxx", "formData": null } ] }
     * @return 运行后的结果，返回示例： [{"ruleCode": "order_num_rule", "result": "CN2019111117212984"}]
     */
    @PostMapping("/executeRuleByCodeBatch")
    public Result executeByRuleCodeBatch(@RequestBody JSONObject ruleData) {
        JSONObject commonFormData = ruleData.getJSONObject("commonFormData");
        JSONArray rules = ruleData.getJSONArray("rules");
        // 遍历 rules ，批量执行规则
        JSONArray results = new JSONArray(rules.size());
        for (int i = 0; i < rules.size(); i++) {
            JSONObject rule = rules.getJSONObject(i);
            String ruleCode = rule.getString("ruleCode");
            JSONObject formData = rule.getJSONObject("formData");
            // 如果没有传递 formData，就用common的
            if (formData == null) {
                formData = commonFormData;
            }
            // 执行填值规则
            Object result = FillRuleUtil.executeRule(ruleCode, formData);
            JSONObject obj = new JSONObject(rules.size());
            obj.put("ruleCode", ruleCode);
            obj.put("result", result);
            results.add(obj);
        }
        return Result.ok(results);
    }

}