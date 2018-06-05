package com.zdp.sharding.jdbc;

import java.util.List;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/4
 * Time 下午5:52
 */
public class DataSourceGroup {
    private String writeKey;
    private List<String> readKeys;

    public String getWriteKey() {
        return writeKey;
    }

    public void setWriteKey(String writeKey) {
        this.writeKey = writeKey;
    }

    public List<String> getReadKeys() {
        return readKeys;
    }

    public void setReadKeys(List<String> readKeys) {
        this.readKeys = readKeys;
    }
}
