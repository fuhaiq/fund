package org.fhq.fund.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fhq.fund.entity.App;
import org.fhq.fund.mapper.AppMapper;
import org.fhq.fund.service.IAppService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * App应用信息 服务实现类
 * </p>
 *
 * @author fuhaiq@gmail.com
 * @since 2025-09-28
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

    @Override
    public boolean adjustPricesBelowBy30Percent(int below) {
        return update(
                new LambdaUpdateWrapper<App>()
                        .lt(App::getPrice, 100)
                        .setSql("price = price * 1.3"));
    }
}
