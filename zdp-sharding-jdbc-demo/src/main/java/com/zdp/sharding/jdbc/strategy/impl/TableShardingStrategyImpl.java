package com.zdp.sharding.jdbc.strategy.impl;

import com.zdp.sharding.jdbc.strategy.TableShardingStrategy;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/5
 * Time 上午9:34
 */
public class TableShardingStrategyImpl implements TableShardingStrategy {
    @Override
    public String getTableName(String shardingKey) {
        int n = Integer.valueOf(shardingKey.substring(shardingKey.length()-1));
        return "trans_" + n / 2 % 2;
    }
}
