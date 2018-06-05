package com.zdp.sharding.jdbc.strategy;

public interface TableShardingStrategy {
    String getTableName(String shardingKey);
}
