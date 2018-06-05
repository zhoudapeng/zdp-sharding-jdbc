package com.zdp.sharding.jdbc.demo;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/5
 * Time 下午3:43
 */
public class TransBuilder {
    private String transId;
    private String transName;

    public TransBuilder setTransId(String transId) {
        this.transId = transId;
        return this;
    }

    public TransBuilder setTransName(String transName) {
        this.transName = transName;
        return this;
    }

    public Trans build() {
        Trans trans = new Trans();
        trans.setTransId(transId);
        trans.setTransName(transName);
        return trans;
    }
}
