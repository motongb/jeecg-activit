package org.jeecg.config;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class WorkflowConstants {
    /**
     * 动态流程图颜色定义
     **/
    public static final Color COLOR_NORMAL = new Color(0, 205, 0);
    public static final Color COLOR_CURRENT = new Color(255, 0, 0);

    /**
     * 定义生成流程图时的边距(像素)
     **/
    public static final int PROCESS_PADDING = 5;

    /**
     * 开始事件
     */
    public static final String EVENT_START = "start";

    /**
     * 结束事件
     */
    public static final String EVENT_END = "end";

    /**
     * 提交申请，启动流程
     */
    public static final String EVENT_APPLY = "apply";

    /**
     * 撤回申请
     */
    public static final String EVENT_CANCEL = "cancel";

    /**
     * 驳回至发起人
     */
    public static final String EVENT_BACK = "back";

    /**
     * 审批通过
     */
    public static final String EVENT_PASS = "pass";

    /**
     * 审批结束
     */
    public static final String EVENT_FINALIZED = "finalized";

    /**
     * 流程监听器实现类映射
     */
    public static final Map<String, String> ActivitiListenerInstanceMap = new HashMap<String, String>() {{
        put("contract_purchase", "purchaseActivitiEventListenerHandle");
    }};
}