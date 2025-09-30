package org.fhq.common.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.fhq.common.util.ApplicationContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

@Slf4j
public class MybatisUtil {

    public static <T> Optional<BaseMapper<T>> findBaseMapperByEntityClass(Class<T> entityClass) {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        String simpleName = entityClass.getSimpleName(); // 如 "User"
        String expectedMapperBeanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Mapper"; // 如 "User" → "userMapper"
        try {
            Object bean = context.getBean(expectedMapperBeanName);

            if (bean instanceof BaseMapper) {
                @SuppressWarnings("unchecked")
                BaseMapper<T> mapper = (BaseMapper<T>) bean;
                return Optional.of(mapper);
            } else {
                // 该 Bean 存在，但不是 BaseMapper 的实现类
                log.warn("Bean [{}] 存在，但不是 BaseMapper 的实现类", expectedMapperBeanName);
                return Optional.empty();
            }
        } catch (BeansException e) {
            log.warn("未找到实体 {} 对应的 Mapper Bean: {}",
                    entityClass.getSimpleName(),
                    expectedMapperBeanName,
                    e);
            return Optional.empty();
        }
    }

}
