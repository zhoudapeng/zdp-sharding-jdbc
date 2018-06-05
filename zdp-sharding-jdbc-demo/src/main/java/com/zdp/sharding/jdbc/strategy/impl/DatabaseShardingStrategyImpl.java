package com.zdp.sharding.jdbc.strategy.impl;

import com.zdp.sharding.jdbc.strategy.DatabaseShardingStrategy;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/4
 * Time 下午9:47
 */
public class DatabaseShardingStrategyImpl implements DatabaseShardingStrategy
{
    @Override
    public String getDataSourceGroupName(String shardingKey) {
        int n = Integer.valueOf(shardingKey.substring(shardingKey.length()-1));
        return "trans_shard_" + n % 2;
    }
}
