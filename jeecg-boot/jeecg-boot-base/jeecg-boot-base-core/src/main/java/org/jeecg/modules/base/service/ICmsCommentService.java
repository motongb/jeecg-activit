package org.jeecg.modules.base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.base.entity.CmsComment;

import java.util.List;

/**
 * @Description: 评论表
 * @Author: jeecg-boot
 * @Date: 2021-03-18
 * @Version: V1.0
 */
public interface ICmsCommentService extends IService<CmsComment> {

    /**
     * 根节点父ID的值
     */
    public static final String ROOT_PID_VALUE = "0";

    /**
     * 树节点有子节点状态值
     */
    public static final String HASCHILD = "1";

    /**
     * 树节点无子节点状态值
     */
    public static final String NOCHILD = "0";

    /**
     * 新增节点
     */
    void addCmsComment(CmsComment cmsComment);

    /**
     * 修改节点
     */
    void updateCmsComment(CmsComment cmsComment) throws JeecgBootException;

    /**
     * 删除节点
     */
    void deleteCmsComment(String id) throws JeecgBootException;

    /**
     * 查询所有数据，无分页
     */
    List<CmsComment> queryTreeListNoPage(QueryWrapper<CmsComment> queryWrapper);

    /**
     *
     * @param cmsComment
     * @return
     */
    List<CmsComment> listVo(CmsComment cmsComment);
}
