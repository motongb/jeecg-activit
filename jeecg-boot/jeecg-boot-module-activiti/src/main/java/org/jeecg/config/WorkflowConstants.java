package org.jeecg.config;

import java.awt.*;

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
     * 驳回
     */
    public static final String FAIL = "-1";
    /**
     * 审批中
     */
    public static final String SIGNING = "2";
    /**
     * 审批结束
     */
    public static final String FINALIZED = "3";
}