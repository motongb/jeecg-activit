package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.contract.entity.ContractType;

import java.util.List;

/**
 * @Description: 合同类型
 * @Author: jeecg-boot
 * @Date: 2021-02-02
 * @Version: V1.0
 */
public interface IContractTypeService extends IService<ContractType> {

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
    void addContractType(ContractType contractType);

    /**
     * 修改节点
     */
    void updateContractType(ContractType contractType) throws JeecgBootException;

    /**
     * 删除节点
     */
    void deleteContractType(String id) throws JeecgBootException;

    /**
     * 查询所有数据，无分页
     */
    List<ContractType> queryTreeListNoPage(QueryWrapper<ContractType> queryWrapper);

}
