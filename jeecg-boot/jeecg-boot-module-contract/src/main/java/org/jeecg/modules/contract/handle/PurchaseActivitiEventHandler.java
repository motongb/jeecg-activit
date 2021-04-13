package org.jeecg.modules.contract.handle;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.OaWpsModel;
import org.jeecg.common.system.api.WebOfficeAPI;
import org.jeecg.common.util.WpsUtil;
import org.jeecg.listener.IActivitiEventListener;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.contract.entity.BaseContract;
import org.jeecg.modules.contract.entity.ContractFieldParams;
import org.jeecg.modules.contract.entity.ContractModel;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.service.IContractModelService;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.jeecg.modules.contract.utils.ModelTransferInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author motb
 * @date 2021/3/12 8:39
 * @description //TODO GeneralActivitiListenerServiceImpl
 **/
@Component
@Transactional
public class PurchaseActivitiEventHandler implements IActivitiEventListener, ModelTransferInterface {

    @Autowired
    private IContractPurchaseService contractPurchaseService;

    @Autowired
    private IContractModelService contractModelService;

    @Autowired
    private WebOfficeAPI webOfficeAPI;

    @Autowired
    private WpsUtil wpsUtil;

    @Override
    public void apply(ActBusiness actBusiness) {
        ContractPurchase contractPurchase = contractPurchaseService.getById(actBusiness.getTableId());
        // 是否使用模板
        if (ContractConst.IS_USE_MODEL.equals(contractPurchase.getUseModel()) &&
                StringUtils.hasText(contractPurchase.getSourceModel()) &&
                StringUtils.hasText(contractPurchase.getFileModel())) {
            String fileContract = this.copyToStanderWord(contractPurchase);
            contractPurchase.setFileContract(fileContract);
        }
        contractPurchase.setStatus(ContractConst.STATUS_SIGNING);
        contractPurchase.setId(actBusiness.getTableId());
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

    @Override
    public void delete(ActBusiness actBusiness) {

    }

    /**
     * 从模板复制到正文
     *
     * @param baseContract
     */
    @Override
    public String copyToStanderWord(BaseContract baseContract) {
        ContractPurchase contractPurchase = (ContractPurchase) baseContract;
        ContractPurchase contractPurchaseVo = contractPurchaseService.getContractVoById(contractPurchase.getId(), true);
        if (Objects.isNull(contractPurchaseVo)) {
            return null;
        }
        // 从模板复制新文件
        OaWpsModel oaWpsModel = webOfficeAPI.copyByModelFile(contractPurchaseVo.getFileModel());
        IService<OaWpsModel> oaWpsModelIService = webOfficeAPI.getOaWpsModelService();
        // 查询模板对象
        OaWpsModel model = oaWpsModelIService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                .eq(OaWpsModel::getFileId, contractPurchaseVo.getFileModel()).orderByDesc(OaWpsModel::getVersion).last("limit 1"));
        ContractModel contractModel = contractModelService.getOne(new LambdaQueryWrapper<ContractModel>().eq(ContractModel::getFileId, contractPurchaseVo.getSourceModel()));
        // 表格参数索引
        Map<Integer, String> indexMap = new HashMap<>();
        List<ContractFieldParams> contractFieldParams = JSON.parseArray(contractModel.getParamsFields(), ContractFieldParams.class);
        contractFieldParams.forEach(item -> {
            // 列表数据
            if ("3".equals(item.getType()) && item.getTableIndex() != null) {
                indexMap.put(item.getTableIndex(), item.getFieldKey());
            }
        });
        // 转换参数
        Map<String, Object> params = transferObjParams(contractPurchaseVo);
        // 替换文本
        wpsUtil.replaceContent(model.getDownloadUrl(), oaWpsModel.getDownloadUrl(), params, indexMap);
        return oaWpsModel.getFileId();
    }
}
