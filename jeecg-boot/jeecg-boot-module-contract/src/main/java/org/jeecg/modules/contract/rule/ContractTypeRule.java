package org.jeecg.modules.contract.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.base.entity.TreeEntity;
import org.jeecg.modules.contract.entity.ContractType;
import org.jeecg.modules.contract.mapper.ContractTypeMapper;
import org.jeecg.modules.contract.service.IContractTypeService;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author motb
 * @date 2021/4/1 17:19
 * @description // 合同类型编码
 **/
public class ContractTypeRule implements IFillRuleHandler {

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        String pid = (String) formData.get("pid");
        String code = "";
        // 查询父节点
        ContractTypeMapper contractTypeMapper = (ContractTypeMapper) SpringContextUtils.getBean("contractTypeMapper");
        // 根节点
        if (StringUtils.isEmpty(pid) || IContractTypeService.ROOT_PID_VALUE.equals(pid)) {
            LambdaQueryWrapper<ContractType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TreeEntity::getPid, pid).orderByDesc(ContractType::getCode).last("limit 1");
            ContractType contractType = contractTypeMapper.selectOne(queryWrapper);
            if (Objects.isNull(contractType)) { // 没有数据默认值T01
                code = YouBianCodeUtil.getNextYouBianCode("T00");
            } else {
                code = YouBianCodeUtil.getNextYouBianCode(contractType.getCode());
            }
        } else { //子节点
            // 父节点
            ContractType parent = contractTypeMapper.selectById(pid);
            if (Objects.isNull(parent)) {
                throw new JeecgBootException("数据丢失");
            }
            // 兄弟节点
            ContractType brother = contractTypeMapper.selectOne(new LambdaQueryWrapper<ContractType>()
                    .eq(TreeEntity::getPid, pid).orderByDesc(ContractType::getCode).last("limit 1"));
            if (Objects.isNull(brother)) {
                char zimu = 'Z';
                int index = parent.getCode().length();
                if (index > 3) {
                    String s = parent.getCode().substring(index - 4, index - 3);
                    zimu = s.charAt(0);
                }
                code = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), String.format("%s%s000", parent.getCode(), getNextZiMu(zimu)));
            } else {
                code = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), brother.getCode());
            }
        }
        return code;
    }

    private static char getNextZiMu(char zimu) {
        if (zimu == 'Z') {
            return 'A';
        }
        zimu++;
        return zimu;
    }
}
