package org.jeecg.modules.activiti.entity;

import lombok.Data;

import java.util.List;

/**
 * @author motb
 * @date 2021/1/14 14:55
 * @description //TODO ActConditionGroup
 **/
@Data
public class ActConditionGroup {

    private String name;

    private Integer type;

    private List<ActCondition> conditions;
}
