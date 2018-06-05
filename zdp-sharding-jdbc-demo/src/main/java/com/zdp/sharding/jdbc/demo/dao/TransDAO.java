package com.zdp.sharding.jdbc.demo.dao;

import com.zdp.sharding.jdbc.annotations.Read;
import com.zdp.sharding.jdbc.annotations.ShardBy;
import com.zdp.sharding.jdbc.annotations.Sharding;
import com.zdp.sharding.jdbc.annotations.Write;
import com.zdp.sharding.jdbc.demo.Trans;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/5
 * Time 上午9:38
 */
@Sharding(dbShardingStrategy = "transDBShardingStrategy",tableShardingStrategy = "transTableShardingStrategy")
public interface TransDAO {
    @Write
    void insert(@ShardBy @Param("trans") Trans trans);

    @Read
    Trans query(@ShardBy @Param("trans") Trans trans);
}
