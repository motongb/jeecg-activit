package org.jeecg.modules.base.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.base.entity.CmsComment;

import java.util.List;

/**
 * @Description: 评论表
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
public interface CmsCommentMapper extends BaseMapper<CmsComment> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

	List<CmsComment> queryList(@Param("query") CmsComment cmsComment);
}
