package org.jeecg.modules.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.activiti.entity.ActZprocess;

import java.util.List;
import java.util.Map;

/**
 * @Description: 流程定义扩展表
 * @Author: pmc
 * @Date: 2020-03-22
 * @Version: V1.0
 */
public interface ActZprocessMapper extends BaseMapper<ActZprocess> {

    List<ActZprocess> selectNewestProcess(@Param("processKey") String processKey);

    IPage<ActZprocess> pageVo(IPage<ActZprocess> page, @Param("params") Map<String, Object> params);

    ActZprocess selectVoById(String id);
}
