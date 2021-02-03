package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.ContractType;
import org.jeecg.modules.contract.mapper.ContractTypeMapper;
import org.jeecg.modules.contract.service.IContractTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 合同类型
 * @Author: jeecg-boot
 * @Date: 2021-02-02
 * @Version: V1.0
 */
@Service
public class ContractTypeServiceImpl extends ServiceImpl<ContractTypeMapper, ContractType> implements IContractTypeService {

    @Override
    public void addContractType(ContractType contractType) {
        if (oConvertUtils.isEmpty(contractType.getPid())) {
            contractType.setPid(IContractTypeService.ROOT_PID_VALUE);
        } else {
            //如果当前节点父ID不为空 则设置父节点的hasChildren 为1
            ContractType parent = baseMapper.selectById(contractType.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.insert(contractType);
    }

    @Override
    public void updateContractType(ContractType contractType) {
        ContractType entity = this.getById(contractType.getId());
        if (entity == null) {
            throw new JeecgBootException("未找到对应实体");
        }
        String old_pid = entity.getPid();
        String new_pid = contractType.getPid();
        if (!old_pid.equals(new_pid)) {
            updateOldParentNode(old_pid);
            if (oConvertUtils.isEmpty(new_pid)) {
                contractType.setPid(IContractTypeService.ROOT_PID_VALUE);
            }
            if (!IContractTypeService.ROOT_PID_VALUE.equals(contractType.getPid())) {
                baseMapper.updateTreeNodeStatus(contractType.getPid(), IContractTypeService.HASCHILD);
            }
        }
        baseMapper.updateById(contractType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContractType(String id) throws JeecgBootException {
        //查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if (id.indexOf(",") > 0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if (idVal != null) {
                    ContractType contractType = this.getById(idVal);
                    String pidVal = contractType.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<ContractType> dataList = baseMapper.selectList(new QueryWrapper<ContractType>().eq("pid", pidVal).notIn("id", Arrays.asList(idArr)));
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
            ContractType contractType = this.getById(id);
            if (contractType == null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(contractType.getPid());
            baseMapper.deleteById(id);
        }
    }

    @Override
    public List<ContractType> queryTreeListNoPage(QueryWrapper<ContractType> queryWrapper) {
        List<ContractType> dataList = baseMapper.selectList(queryWrapper);
        List<ContractType> mapList = new ArrayList<>();
        for (ContractType data : dataList) {
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if (pidVal != null && !"0".equals(pidVal)) {
                ContractType rootVal = this.getTreeRoot(pidVal);
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

    /**
     * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
     *
     * @param pid
     */
    private void updateOldParentNode(String pid) {
        if (!IContractTypeService.ROOT_PID_VALUE.equals(pid)) {
            Integer count = baseMapper.selectCount(new QueryWrapper<ContractType>().eq("pid", pid));
            if (count == null || count <= 1) {
                baseMapper.updateTreeNodeStatus(pid, IContractTypeService.NOCHILD);
            }
        }
    }

    /**
     * 递归查询节点的根节点
     *
     * @param pidVal
     * @return
     */
    private ContractType getTreeRoot(String pidVal) {
        ContractType data = baseMapper.selectById(pidVal);
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
        List<ContractType> dataList = baseMapper.selectList(new QueryWrapper<ContractType>().eq("pid", pidVal));
        if (dataList != null && dataList.size() > 0) {
            for (ContractType tree : dataList) {
                if (!sb.toString().contains(tree.getId())) {
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(), sb);
            }
        }
        return sb;
    }

}
