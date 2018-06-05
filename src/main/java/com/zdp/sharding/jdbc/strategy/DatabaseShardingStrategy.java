package com.zdp.sharding.jdbc.strategy;

public interface DatabaseShardingStrategy {
    String getDataSourceGroupName(String shardingKey);
}
