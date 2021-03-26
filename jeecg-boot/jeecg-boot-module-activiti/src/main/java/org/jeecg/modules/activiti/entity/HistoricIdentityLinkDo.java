package org.jeecg.modules.activiti.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author motb
 * @date 2021/3/25 11:06
 * @description //TODO HistoricIdentityLinkDo
 **/
@Data
@TableName("act_hi_identitylink")
public class HistoricIdentityLinkDo {

    @TableId(value = "ID_", type = IdType.ASSIGN_ID)
    protected String id;

    @TableField(value = "TYPE_")
    protected String type;

    @TableField(value = "USER_ID_")
    protected String userId;

    @TableField(value = "GROUP_ID_")
    protected String groupId;

    @TableField(value = "TASK_ID_")
    protected String taskId;

    @TableField(value = "PROC_INST_ID_")
    protected String processInstanceId;
}
