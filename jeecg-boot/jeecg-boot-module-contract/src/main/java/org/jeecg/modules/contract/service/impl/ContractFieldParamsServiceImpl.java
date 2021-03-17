package org.jeecg.modules.contract.service.impl;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.ContractFieldParams;
import org.jeecg.modules.contract.mapper.ContractFieldParamsMapper;
import org.jeecg.modules.contract.service.IContractFieldParamsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 合同字段参数
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Service
public class ContractFieldParamsServiceImpl extends ServiceImpl<ContractFieldParamsMapper, ContractFieldParams> implements IContractFieldParamsService {

	@Override
	public void addContractFieldParams(ContractFieldParams contractFieldParams) {
		if(oConvertUtils.isEmpty(contractFieldParams.getPid())){
			contractFieldParams.setPid(IContractFieldParamsService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			ContractFieldParams parent = baseMapper.selectById(contractFieldParams.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(contractFieldParams);
	}
	
	@Override
	public void updateContractFieldParams(ContractFieldParams contractFieldParams) {
		ContractFieldParams entity = this.getById(contractFieldParams.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = contractFieldParams.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				contractFieldParams.setPid(IContractFieldParamsService.ROOT_PID_VALUE);
			}
			if(!IContractFieldParamsService.ROOT_PID_VALUE.equals(contractFieldParams.getPid())) {
				baseMapper.updateTreeNodeStatus(contractFieldParams.getPid(), IContractFieldParamsService.HASCHILD);
			}
		}
		baseMapper.updateById(contractFieldParams);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteContractFieldParams(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    ContractFieldParams contractFieldParams = this.getById(idVal);
                    String pidVal = contractFieldParams.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<ContractFieldParams> dataList = baseMapper.selectList(new QueryWrapper<ContractFieldParams>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
                    if((dataList == null || dataList.size()==0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            ContractFieldParams contractFieldParams = this.getById(id);
            if(contractFieldParams==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(contractFieldParams.getPid());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<ContractFieldParams> queryTreeListNoPage(QueryWrapper<ContractFieldParams> queryWrapper) {
        List<ContractFieldParams> dataList = baseMapper.selectList(queryWrapper);
        List<ContractFieldParams> mapList = new ArrayList<>();
        for(ContractFieldParams data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !"0".equals(pidVal)){
                ContractFieldParams rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IContractFieldParamsService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<ContractFieldParams>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IContractFieldParamsService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private ContractFieldParams getTreeRoot(String pidVal){
        ContractFieldParams data =  baseMapper.selectById(pidVal);
        if(data != null && !"0".equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<ContractFieldParams> dataList = baseMapper.selectList(new QueryWrapper<ContractFieldParams>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(ContractFieldParams tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
