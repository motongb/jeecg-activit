package org.jeecg.modules.contract.handle;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.OaWpsModel;
import org.jeecg.common.system.api.WebOfficeAPI;
import org.jeecg.common.util.WpsUtil;
import org.jeecg.listener.IActivitiEventListener;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.entity.vo.ContractPurchaseVo;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author motb
 * @date 2021/3/12 8:39
 * @description //TODO GeneralActivitiListenerServiceImpl
 **/
@Service
public class PurchaseActivitiEventListenerHandle implements IActivitiEventListener {

    @Autowired
    private IContractPurchaseService contractPurchaseService;

    @Autowired
    private WebOfficeAPI webOfficeAPI;

    @Autowired
    private WpsUtil wpsUtil;

    @Override
    public void apply(ActBusiness actBusiness) {
        ContractPurchase contractPurchase = contractPurchaseService.getById(actBusiness.getTableId());
        this.copyToStanderWord(contractPurchase);
        contractPurchase.setStatus(ContractConst.STATUS_SIGNING);
        contractPurchaseService.updateById(contractPurchase);
    }

    @Override
    public void cancel(ActBusiness actBusiness) {
        contractPurchaseService.update(new UpdateWrapper<ContractPurchase>().set("status", ContractConst.STATUS_START).eq("id", actBusiness.getTableId()));
    }

    @Override
    public void back(ActBusiness actBusiness) {
        contractPurchaseService.update(new UpdateWrapper<ContractPurchase>().set("status", ContractConst.STATUS_BACK).eq("id", actBusiness.getTableId()));
    }

    @Override
    public void pass(ActBusiness actBusiness) {
    }

    @Override
    public void finalized(ActBusiness actBusiness) {
        contractPurchaseService.update(new UpdateWrapper<ContractPurchase>().set("status", ContractConst.STATUS_FINALIZED).eq("id", actBusiness.getTableId()));
    }

    /**
     * 从模板复制到正文
     *
     * @param contractPurchase
     */
    private void copyToStanderWord(ContractPurchase contractPurchase) {
        ContractPurchaseVo contractPurchaseVo = contractPurchaseService.getContractVoById(contractPurchase.getId(), true);
        if (Objects.isNull(contractPurchaseVo)) {
            return;
        }
        // 是否使用模板
        if (ContractConst.IS_USE_MODEL.equals(contractPurchaseVo.getUseModel()) &&
                StringUtils.hasText(contractPurchaseVo.getSourceModel()) &&
                StringUtils.hasText(contractPurchaseVo.getFileModel())) {
            // 从模板复制新文件
            OaWpsModel oaWpsModel = webOfficeAPI.copyByModelFile(contractPurchaseVo.getFileModel());
            IService<OaWpsModel> oaWpsModelIService = webOfficeAPI.getOaWpsModelService();
            // 模板对象
            OaWpsModel model = oaWpsModelIService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                    .eq(OaWpsModel::getFileId, contractPurchaseVo.getFileModel()).orderByDesc(OaWpsModel::getVersion).last("limit 1"));
            // 设置新文件id
            contractPurchase.setFileContract(oaWpsModel.getFileId());
            // 表格参数索引
            Map<Integer, String> indexMap = new HashMap<>();
            // 转换参数
            Map<String, Object> params = transferObjParams(contractPurchaseVo);
            // 替换文本
            wpsUtil.replaceContent(model.getDownloadUrl(), oaWpsModel.getDownloadUrl(), params, indexMap);
        }
    }

    private Map<String, Object> transferObjParams(ContractPurchaseVo contractPurchaseVo) {
        Map<String, Object> params = BeanUtil.beanToMap(contractPurchaseVo);
        ContractMember firstMemberObj = contractPurchaseVo.getFirstMemberObj();
        ContractMember secondMemberObj = contractPurchaseVo.getSecondMemberObj();
        ContractMember thirdMemberObj = contractPurchaseVo.getThirdMemberObj();
        if (firstMemberObj != null) {
            Map<String, Object> firstMemberObjMap = BeanUtil.beanToMap(firstMemberObj);
            firstMemberObjMap.forEach((k, v) -> params.put("firstMember_" + k, v));
        }
        if (secondMemberObj != null) {
            Map<String, Object> secondMemberObjMap = BeanUtil.beanToMap(secondMemberObj);
            secondMemberObjMap.forEach((k, v) -> params.put("secondMember_" + k, v));
        }
        if (thirdMemberObj != null) {
            Map<String, Object> thirdMemberObjMap = BeanUtil.beanToMap(thirdMemberObj);
            thirdMemberObjMap.forEach((k, v) -> params.put("thirdMember_" + k, v));
        }
        return params;
    }
}
