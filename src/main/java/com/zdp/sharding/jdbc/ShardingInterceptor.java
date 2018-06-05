package com.zdp.sharding.jdbc;

import com.zdp.sharding.jdbc.annotations.Read;
import com.zdp.sharding.jdbc.annotations.ShardBy;
import com.zdp.sharding.jdbc.annotations.Sharding;
import com.zdp.sharding.jdbc.annotations.Write;
import com.zdp.sharding.jdbc.strategy.DatabaseShardingStrategy;
import com.zdp.sharding.jdbc.strategy.TableShardingStrategy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/5/31
 * Time 上午10:23
 */
public class ShardingInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ShardingInterceptor.class);

    private Map<String,DataSourceGroup> dataSourceGroups;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Sharding sharding = method.getDeclaringClass().getAnnotation(Sharding.class);
        if (sharding == null) {
            logger.info("无需sharding，直接执行");
            return methodInvocation.proceed();
        }
        Shardable shardingObject = getShardingObject(methodInvocation);
        if (shardingObject == null) {
            throw new RuntimeException("未指定分表依据，请确保至少有一个标有@ShardBy注解的参数");
        }
        handleDatabaseName(sharding,shardingObject,method);
        haneldTableName(sharding,shardingObject);
        return methodInvocation.proceed();
    }

    private boolean isReadAction(Method method) {
        // 优先级:方法级写>方法级读>类级写>类级读，方法和类都未标注时默认为写操作
        Write writeAnnotation = method.getAnnotation(Write.class);
        if (writeAnnotation != null) {
            return false;
        }
        Read readAnnotation = method.getAnnotation(Read.class);
        if (readAnnotation != null) {
            return true;
        }
        writeAnnotation = method.getDeclaringClass().getAnnotation(Write.class);
        if (writeAnnotation != null) {
            return false;
        }
        readAnnotation = method.getDeclaringClass().getAnnotation(Read.class);
        if (readAnnotation != null) {
            return true;
        }
        return false;
    }

    private void handleDatabaseName( Sharding sharding, Shardable shardingObject,Method method) {
        boolean isReadAction = isReadAction(method);
        logger.debug("此次操作是{}操作",isReadAction?"读":"写");
        DatabaseShardingStrategy databaseShardingStrategy = (DatabaseShardingStrategy) ShardingContext.getBean(sharding.dbShardingStrategy());
        String dataSourceGroupName = databaseShardingStrategy.getDataSourceGroupName(shardingObject.shardingKey());
        DataSourceGroup dataSourceGroup = dataSourceGroups.get(dataSourceGroupName);
        String dataSourceName = dataSourceGroup.getWriteKey();
        if (isReadAction) {
            List<String> readKeys = dataSourceGroup.getReadKeys();
            Random rd = new Random();
            dataSourceName = readKeys.get(rd.nextInt(readKeys.size()));
        }
        logger.debug("dataSourceName：" + dataSourceName);
        DBNameHolder.set(dataSourceName);
    }

    private void haneldTableName( Sharding sharding, Shardable shardingObject) {
        TableShardingStrategy tableShardingStrategy = (TableShardingStrategy) ShardingContext.getBean(sharding.tableShardingStrategy());
        String tableName = tableShardingStrategy.getTableName(shardingObject.shardingKey());
        logger.debug("tableName:" + tableName);
        shardingObject.setTableName(tableName);
    }

    private Shardable getShardingObject(MethodInvocation methodInvocation) {
        Annotation[][] annotations = methodInvocation.getMethod().getParameterAnnotations();
        if (annotations == null) {
            throw new RuntimeException("未指定分表依据，请确保至少有一个标有@ShardBy注解的参数");
        }
        Object[] args = methodInvocation.getArguments();
        Shardable shardingObject = null;
        for (int i = 0;i < args.length;i++) {
            Object arg = args[i];
            if (arg instanceof Shardable) {
                Annotation[] annotationList =  annotations[i];
                if (annotationList.length == 0) {
                    continue;
                }
                for (Annotation annotation:annotationList) {
                    if (annotation.annotationType() == ShardBy.class) {
                        if (arg instanceof Shardable) {
                            shardingObject = (Shardable) arg;
                            break;
                        }
                        throw new RuntimeException("Sharding字段必须是Shardable接口的实现类");
                    }
                }
            }
        }
        return shardingObject;
    }

    public void setDataSourceGroups(Map<String, DataSourceGroup> dataSourceGroups) {
        this.dataSourceGroups = dataSourceGroups;
    }
}
