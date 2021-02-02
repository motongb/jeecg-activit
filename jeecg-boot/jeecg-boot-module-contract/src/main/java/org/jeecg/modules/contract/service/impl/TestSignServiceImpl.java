package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.TestSign;
import org.jeecg.modules.contract.mapper.TestSignMapper;
import org.jeecg.modules.contract.service.ITestSignService;
import org.springframework.stereotype.Service;

/**
 * @Description: 会签流程表单
 * @Author: jeecg-boot
 * @Date: 2021-01-26
 * @Version: V1.0
 */
@Service
public class TestSignServiceImpl extends ServiceImpl<TestSignMapper, TestSign> implements ITestSignService {

}
