package org.fhq.common.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor()); // 乐观锁
        interceptor.addInnerInterceptor(new MybatisPaginationInnerInterceptor(DbType.POSTGRE_SQL)); // 分页
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor()); // 防止全表更新与删除
        interceptor.addInnerInterceptor(new AutoOptimisticLockerInnerInterceptor()); // 自动乐观锁
        return interceptor;
    }

    @Bean
    public MybatisMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisMetaObjectHandler();
    }
}
