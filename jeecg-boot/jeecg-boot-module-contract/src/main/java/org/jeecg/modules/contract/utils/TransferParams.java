package org.jeecg.modules.contract.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jeecg.modules.contract.entity.Contract;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author motb
 * @date 2021/3/16 11:21
 * @description //TODO TransferParams
 **/
public interface TransferParams {

    default Map<String, Object> transferObjParams(Object obj) {
        Map<String, Object> map = JSONUtil.parseObj(obj);
        Class<?> clazz = obj.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (value instanceof Contract) {
                    JSONObject jsonObject = (JSONObject) map.get(fieldName);
                    jsonObject.forEach((k, v) -> map.put(fieldName + "_" + k, v));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
}
