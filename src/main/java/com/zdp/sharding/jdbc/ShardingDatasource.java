package com.zdp.sharding.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/4
 * Time 下午5:47
 */
public class ShardingDatasource extends AbstractRoutingDataSource {
    private static Logger logger = LoggerFactory.getLogger(ShardingDatasource.class);
    @Override
    protected Object determineCurrentLookupKey() {
        String key = DBNameHolder.get();
        logger.debug("key=" + key);
        return key;
    }
}
