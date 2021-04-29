package org.jeecg.modules.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.activiti.entity.ActZFieldGroup;

import java.util.List;

/**
 * @Description: 流程表字段组
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
public interface ActZFieldGroupMapper extends BaseMapper<ActZFieldGroup> {

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<ActZFieldGroup> selectByMainId(@Param("mainId") String mainId);
}
