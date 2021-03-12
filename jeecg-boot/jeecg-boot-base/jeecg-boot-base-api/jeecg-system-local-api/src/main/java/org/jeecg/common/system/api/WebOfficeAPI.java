package org.jeecg.common.system.api;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.OaWpsModel;

/**
 * @author motb
 * @date 2021/3/12 10:08
 * @description //TODO WebOfficeAPI
 **/
public interface WebOfficeAPI {

    OaWpsModel copyByModelFile(String fileId);

    IService<OaWpsModel> getOaWpsModelService();
}
