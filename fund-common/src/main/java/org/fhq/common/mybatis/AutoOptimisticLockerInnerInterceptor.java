package org.fhq.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class AutoOptimisticLockerInnerInterceptor extends OptimisticLockerInnerInterceptor {
    @Override
    protected void doOptimisticLocker(Map<String, Object> map, String msId) {

        Object et = map.getOrDefault(Constants.ENTITY, null);
        if (Objects.nonNull(et)) {
            // version field
            TableFieldInfo versionFieldInfo = this.getVersionFieldInfo(et.getClass());
            if (null == versionFieldInfo) {
                return;
            }

            // current table
            TableInfo tableInfo = TableInfoHelper.getTableInfo(et.getClass());
            if (null == tableInfo.getKeyColumn()) {
                return;
            }

            log.info("id字段名字{}, 表名{}, version字段名字{}",
                    tableInfo.getKeyColumn(),
                    tableInfo.getTableName(),
                    versionFieldInfo.getColumn()
            );
            // TODO 获取主键值,动态拼接并执行sql
        }

    }
}
