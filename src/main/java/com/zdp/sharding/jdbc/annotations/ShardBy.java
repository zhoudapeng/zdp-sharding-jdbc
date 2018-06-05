package com.zdp.sharding.jdbc.annotations;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/5/31
 * Time 上午10:19
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardBy {
}
