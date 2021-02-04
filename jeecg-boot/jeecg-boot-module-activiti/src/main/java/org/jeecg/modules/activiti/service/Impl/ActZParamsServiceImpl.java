package org.jeecg.modules.activiti.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.activiti.entity.ActZParams;
import org.jeecg.modules.activiti.mapper.ActZParamsMapper;
import org.jeecg.modules.activiti.service.IActZParamsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 流程参数表
 * @Author: jeecg-boot
 * @Date: 2021-01-21
 * @Version: V1.0
 */
@Service
public class ActZParamsServiceImpl extends ServiceImpl<ActZParamsMapper, ActZParams> implements IActZParamsService {

    @Override
    public Map<String, Object> getActParams(String tableId) {
        List<ActZParams> otherParams = list(new LambdaQueryWrapper<ActZParams>().eq(ActZParams::getPid, tableId));
        if (!CollectionUtils.isEmpty(otherParams)) {
            Map<String, Object> params = new HashMap<>();
            otherParams.forEach(p -> params.put(p.getParamsKey(), p.getParamsVal()));
            return params;
        }
        return null;
    }
}
