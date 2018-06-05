package com.zdp.sharding.jdbc.demo;

import com.zdp.sharding.jdbc.Shardable;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/5
 * Time 上午9:41
 */
public class Trans implements Shardable{
    private Long id;
    private String transId;
    private String transName;
    private String tableName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    @Override
    public String shardingKey() {
        return transId;
    }

    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "Trans{" +
                "id=" + id +
                ", transId='" + transId + '\'' +
                ", transName='" + transName + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
