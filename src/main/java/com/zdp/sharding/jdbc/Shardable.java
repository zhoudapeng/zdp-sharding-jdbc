package com.zdp.sharding.jdbc;

public interface Shardable {
    String shardingKey();
    void setTableName(String tableName);
}
