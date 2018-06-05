package com.zdp.sharding.jdbc;

import com.zdp.sharding.jdbc.annotations.Sharding;
import com.zdp.sharding.jdbc.strategy.DatabaseShardingStrategy;
import com.zdp.sharding.jdbc.strategy.TableShardingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/4
 * Time 下午6:15
 */
public class ShardingCheckBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Sharding sharding = o.getClass().getAnnotation(Sharding.class);
        if (sharding == null) {
            return o;
        }
        String dbShardingStrategyStr = sharding.dbShardingStrategy();
        if (!StringUtils.isEmpty(dbShardingStrategyStr)) {
            DatabaseShardingStrategy databaseShardingStrategy = (DatabaseShardingStrategy) ShardingContext.getBean(sharding.dbShardingStrategy());
            if (databaseShardingStrategy == null) {
                throw new RuntimeException("DatabaseShardingStrategy的sharding策略不存在，请check上下文中" + databaseShardingStrategy);
            }
        }
        String tableShardingStrategyStr = sharding.tableShardingStrategy();
        if (!StringUtils.isEmpty(tableShardingStrategyStr)) {
            TableShardingStrategy tableShardingStrategy = (TableShardingStrategy) ShardingContext.getBean(tableShardingStrategyStr);
            if (tableShardingStrategy == null) {
                throw new RuntimeException("TableShardingStrategy的sharding策略不存在，请check上下文中" + tableShardingStrategy);
            }
        }
        return o;
    }
}
