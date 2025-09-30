package org.fhq.common.mybatis;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.fhq.common.mybatis.MybatisUtil.findBaseMapperByEntityClass;

@Slf4j
public class AutoOptimisticLockerInnerInterceptor extends OptimisticLockerInnerInterceptor {

    /**
     * 不支持 wrapper 模式
     */
    public AutoOptimisticLockerInnerInterceptor() {
        super(false);
    }

    @Override
    protected void doOptimisticLocker(Map<String, Object> map, String msId) {
        // updateById(et)
        Object et = map.getOrDefault(Constants.ENTITY, null);
        if (Objects.nonNull(et)) {
            final Class<?> entityClass = et.getClass();

            // version field
            TableFieldInfo fieldInfo = this.getVersionFieldInfo(entityClass);
            if (null == fieldInfo) {
                return;
            }

            // current table
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
            if (null == tableInfo.getKeyColumn()) {
                return;
            }

            try {
                Field versionField = fieldInfo.getField();
                // 获取旧的 version 值
                Object keyPropertyValue = tableInfo.getPropertyValue(et, tableInfo.getKeyProperty());
                @SuppressWarnings("unchecked")
                Optional<BaseMapper<Object>> mapperOpt = findBaseMapperByEntityClass((Class<Object>) entityClass);
                if (mapperOpt.isEmpty()) {
                    return;
                }
                BaseMapper<Object> mapper = mapperOpt.get();
                QueryWrapper<Object> currentVersion = new QueryWrapper<>()
                        .eq(tableInfo.getKeyProperty(), keyPropertyValue)
                        .select(fieldInfo.getProperty());
                Object entity = mapper.selectOne(currentVersion);
                // 旧的 version 值
                Object originalVersionVal = tableInfo.getPropertyValue(entity, fieldInfo.getProperty());
                String versionColumn = fieldInfo.getColumn();
                // 新的 version 值
                Object updatedVersionVal = this.getUpdatedVersionVal(fieldInfo.getPropertyType(), originalVersionVal);
                String methodName = msId.substring(msId.lastIndexOf(StringPool.DOT) + 1);
                if ("update".equals(methodName)) {
                    AbstractWrapper<?, ?, ?> aw = (AbstractWrapper<?, ?, ?>) map.getOrDefault(Constants.WRAPPER, null);
                    if (aw == null) {
                        UpdateWrapper<?> uw = new UpdateWrapper<>();
                        uw.eq(versionColumn, originalVersionVal);
                        map.put(Constants.WRAPPER, uw);
                    } else {
                        aw.apply(versionColumn + " = {0}", originalVersionVal);
                    }
                } else {
                    map.put(Constants.MP_OPTLOCK_VERSION_ORIGINAL, originalVersionVal);
                }
                versionField.set(et, updatedVersionVal);
            } catch (IllegalAccessException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }
}
