package org.jeecg.modules.contract.utils;

/**
 * @author motb
 * @date 2021/2/4 15:10
 * @description //TODO ContractConst
 **/
public interface ContractConst {

    /**
     * 起草
     */
    String STATUS_START = "1";

    /**
     * 会签阶段
     */
    String STATUS_SIGNING = "2";

    /**
     * 定稿结束
     */
    String STATUS_FINALIZED = "3";

    /**
     * 申请中断
     */
    String STATUS_BACK = "-1";

    /**
     * 我方
     */
    String FIRST_MEMBER = "0";

    /**
     * 乙方
     */
    String SECOND_MEMBER = "1";

    /**
     * 丙方
     */
    String THIRD_MEMBER = "2";

    /**
     * 使用模板
     */
    String IS_USE_MODEL = "1";

    /**
     * 币种
     */
    String DICT_COIN = "coin_type";

    /**
     * 合同明细单位
     */
    String DICT_ITEM_UNIT = "contract_item_unit";

    /**
     * 采购方式
     */
    String DICT_PURCHASE_WAY = "purchase_way";
}
