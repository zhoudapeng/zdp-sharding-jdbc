package com.zdp.sharding.jdbc.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sharding {
    /**
     * 分库策略
     * @return
     */
    String dbShardingStrategy() default "";

    /**
     * 分表策略
     * @return
     */
    String tableShardingStrategy() default "";
}
