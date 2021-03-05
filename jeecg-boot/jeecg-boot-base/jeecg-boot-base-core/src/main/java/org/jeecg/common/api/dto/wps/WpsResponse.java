package org.jeecg.common.api.dto.wps;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author motb
 * @date 2021/3/1 17:05
 * @description //TODO WpsResponse
 **/
public class WpsResponse {

    private static final String STATUS_KEY = "status";

    private static final String MESSAGE_KEY = "message";

    private static final String CODE_KEY = "200";

    public static Map<String, Object> OK() {
        return OK(null);
    }

    public static Map<String, Object> OK(Map<String, Object> data) {
        if (Objects.isNull(data)) {
            data = new HashMap<>();
        }
        data.put(STATUS_KEY, "200");
        data.put(MESSAGE_KEY, "success");
        data.put(CODE_KEY, 200);
        return data;
    }

    public static Map<String, Object> fail(String message) {
        Map<String, Object> data = new HashMap<>();
        if (StringUtils.isEmpty(message)) {
            data.put(MESSAGE_KEY, "something wrong");
        } else {
            data.put(MESSAGE_KEY, message);
        }
        data.put(STATUS_KEY, "-1");
        data.put(CODE_KEY, -1);
        return data;
    }
}
