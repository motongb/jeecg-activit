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
}
