package org.fhq.fund.service;

import org.fhq.fund.entity.App;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * App应用信息 服务类
 * </p>
 *
 * @author fuhaiq@gmail.com
 * @since 2025-09-28
 */
public interface IAppService extends IService<App> {

    /**
     * 价格小于 below 上调 30%
     * @param below
     * @return
     */
    boolean adjustPricesBelowBy30Percent(int below);
}
