package org.jeecg.modules.activiti.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author motb
 * @date 2021/1/19 16:56
 * @description //TODO TaskPassVo
 **/
@Data
public class TaskPassVo {
    @ApiModelProperty("任务id")
    private String id;
    @ApiModelProperty("流程实例id")
    private String procInstId;
    @ApiModelProperty("下个节点审批人")
    private List<Map<String, String>> assignees;
    @ApiModelProperty("优先级")
    private Integer priority;
    @ApiModelProperty("意见评论")
    private String comment;
    @ApiModelProperty("是否发送站内消息")
    private Boolean sendMessage;
    @ApiModelProperty("是否发送短信通知")
    private Boolean sendSms;
    @ApiModelProperty("是否发送邮件通知")
    private Boolean sendEmail;
    @ApiModelProperty("流程参数")
    private Map<String, Object> vals;
}
