package org.jeecg.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.entity.CmsComment;
import org.jeecg.modules.base.mapper.CmsCommentMapper;
import org.jeecg.modules.base.service.ICmsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 评论表
 * @Author: jeecg-boot
 * @Date: 2021-03-18
 * @Version: V1.0
 */
@Service
public class CmsCommentServiceImpl extends ServiceImpl<CmsCommentMapper, CmsComment> implements ICmsCommentService {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void addCmsComment(CmsComment cmsComment) {
        if (oConvertUtils.isEmpty(cmsComment.getPid())) {
            cmsComment.setPid(ICmsCommentService.ROOT_PID_VALUE);
        } else {
            //如果当前节点父ID不为空 则设置父节点的hasChildren 为1
            CmsComment parent = baseMapper.selectById(cmsComment.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.insert(cmsComment);
        // 发送站内消息
        MessageDTO messageDTO = new MessageDTO(cmsComment.getCreateBy(), cmsComment.getReceive(), String.format("%s回复", cmsComment.getTitle()), cmsComment.getContent());
        sysBaseAPI.sendSysAnnouncement(messageDTO);
    }

    @Override
    public void updateCmsComment(CmsComment cmsComment) {
        CmsComment entity = this.getById(cmsComment.getId());
        if (entity == null) {
            throw new JeecgBootException("未找到对应实体");
        }
        String old_pid = entity.getPid();
        String new_pid = cmsComment.getPid();
        if (!old_pid.equals(new_pid)) {
            updateOldParentNode(old_pid);
            if (oConvertUtils.isEmpty(new_pid)) {
                cmsComment.setPid(ICmsCommentService.ROOT_PID_VALUE);
            }
            if (!ICmsCommentService.ROOT_PID_VALUE.equals(cmsComment.getPid())) {
                baseMapper.updateTreeNodeStatus(cmsComment.getPid(), ICmsCommentService.HASCHILD);
            }
        }
        baseMapper.updateById(cmsComment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCmsComment(String id) throws JeecgBootException {
        //查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if (id.indexOf(",") > 0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if (idVal != null) {
                    CmsComment cmsComment = this.getById(idVal);
                    String pidVal = cmsComment.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<CmsComment> dataList = baseMapper.selectList(new QueryWrapper<CmsComment>().eq("pid", pidVal).notIn("id", Arrays.asList(idArr)));
                    if ((dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)) {
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for (String pid : pidArr) {
                this.updateOldParentNode(pid);
            }
        } else {
            CmsComment cmsComment = this.getById(id);
            if (cmsComment == null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(cmsComment.getPid());
            baseMapper.deleteById(id);
        }
    }

    @Override
    public List<CmsComment> queryTreeListNoPage(QueryWrapper<CmsComment> queryWrapper) {
        List<CmsComment> dataList = baseMapper.selectList(queryWrapper);
        List<CmsComment> mapList = new ArrayList<>();
        for (CmsComment data : dataList) {
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if (pidVal != null && !"0".equals(pidVal)) {
                CmsComment rootVal = this.getTreeRoot(pidVal);
                if (rootVal != null && !mapList.contains(rootVal)) {
                    mapList.add(rootVal);
                }
            } else {
                if (!mapList.contains(data)) {
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    @Override
    public List<CmsComment> listVo(CmsComment cmsComment) {
        return baseMapper.queryList(cmsComment);
    }

    /**
     * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
     *
     * @param pid
     */
    private void updateOldParentNode(String pid) {
        if (!ICmsCommentService.ROOT_PID_VALUE.equals(pid)) {
            Integer count = baseMapper.selectCount(new QueryWrapper<CmsComment>().eq("pid", pid));
            if (count == null || count <= 1) {
                baseMapper.updateTreeNodeStatus(pid, ICmsCommentService.NOCHILD);
            }
        }
    }

    /**
     * 递归查询节点的根节点
     *
     * @param pidVal
     * @return
     */
    private CmsComment getTreeRoot(String pidVal) {
        CmsComment data = baseMapper.selectById(pidVal);
        if (data != null && !"0".equals(data.getPid())) {
            return this.getTreeRoot(data.getPid());
        } else {
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     *
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if (pidVal != null) {
                if (!sb.toString().contains(pidVal)) {
                    if (sb.toString().length() > 0) {
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal, sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     *
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal, StringBuffer sb) {
        List<CmsComment> dataList = baseMapper.selectList(new QueryWrapper<CmsComment>().eq("pid", pidVal));
        if (dataList != null && dataList.size() > 0) {
            for (CmsComment tree : dataList) {
                if (!sb.toString().contains(tree.getId())) {
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(), sb);
            }
        }
        return sb;
    }

}
