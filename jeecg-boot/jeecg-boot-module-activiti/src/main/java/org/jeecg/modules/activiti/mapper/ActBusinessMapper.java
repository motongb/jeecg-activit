package org.jeecg.modules.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.entity.ActBusiness;

import java.util.List;
import java.util.Map;

/**
 * @Description: 流程业务扩展表
 * @Author: pmc
 * @Date: 2020-03-30
 * @Version: V1.0
 */
public interface ActBusinessMapper extends BaseMapper<ActBusiness> {

    Map<String, Object> getBusiData(@Param("tableId") String tableId, @Param("tableName") String tableName);

    List<Map<String, Object>> getList(@Param("field") String field, @Param("fieldValue") String fieldValue, @Param("tableName") String tableName);

    int insertBusiData(@Param("sql") String sql);

    int insertBatch(@Param("tableName") String tableName, @Param("fields") String fields, @Param("list") List<List<Object>> list);

    int updateBusiData(@Param("tableName") String tableName, @Param("tableId") String tableId, @Param("values") Map<String, Object> values);

    int deleteBusiData(@Param("tableId") String tableId, @Param("tableName") String tableName);

    int deleteSubData(@Param("pid") String pid, @Param("tableName") String tableName, @Param("pField") String pField);

    List<String> findUserIdByTypeAndTaskId(@Param("type") String type, @Param("taskId") String taskId);

    int insertHI_IDENTITYLINK(@Param("id") String id, @Param("type") String type, @Param("userId") String userId, @Param("taskId") String taskId, @Param("procInstId") String procInstId);

    List<String> selectIRunIdentity(@Param("taskId") String taskId, @Param("type") String type);

    int updateBusinessStatus(@Param("tableName") String tableName, @Param("tableId") String tableId, @Param("actStatus") String actStatus);

    List<String> listByTypeApp(@Param("type") String type);

    List<String> deployment_idListByType(@Param("type") String type);

    List<LoginUser> getUsersByName(@Param("searchVal") String searchVal);
}
