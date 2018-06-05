### 这是一个轻量级分库分表框架&读写分离框架，无需搭建中间层代理服务，开发者只需要引入本框架的jar包，并进行相应的配置即可实现分库分表&读写分离逻辑
### 读写分离
读写分离的实现依然是继承了spring提供的AbstractRoutingDataSource，支持数据库一主多从的架构开发者可以根据框架中提供的@Read,@Write注解 
显示告诉框架应该选择主库还是从库，这2个注解可以放在类上，也可以放到方法上，优先级为：方法级写>方法级读>类级写>类级读，方法和类都未标注时默认为写操作 
### 分库分表
开发者可以根据自身业务实现相应的分库分表逻辑，只需要实现DatabaseShardingStrategy和TableShardingStrategy并注入到spring，同时框架提供了ShardingDataSource 
开发者只需要根据自身的数据源信息组装好此数据源，在此框架中定义了一个DataSourceGroup的概念，每个shard对应一个DataSourceGroup，开发者可以在 
DataSourceGroup中定义一个写库和多个读库，实现读写分离，同时此框架会将计算过的表名set到分库分表对象中，所以开发者可以获取表名后跟自身使用的ORM框架整合，
只需要保证表名是变量，且最终从分库分表的对象获取即可，Demo中是跟Mybatis做的整合
### 如何开始
示例中将以如下场景进行介绍 

交易库分成2个库，每个库分2个表 

分库策略：${transId}%2 

分表策略：${transId}/2%2
1. 定义，组装数据源
```java
    <context:property-placeholder location="classpath:properties/*.properties" />

    <bean id="shard_0_write" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${driverClass_shard_0}"/>
        <property name="jdbcUrl" value="${jdbcUrl_shard_0}"/>
        <property name="user" value="${user_shard_0}"/>
        <property name="password" value="${password_shard_0}"/>
        <property name="initialPoolSize" value="5"/>              <!-- 初始大小 -->
        <property name="minPoolSize" value="5"/>                  <!-- 最小连接处 -->
        <property name="maxPoolSize" value="50"/>              <!-- 连接池中保留的最大连接数 -->
        <property name="maxStatements"
                  value="0"/>              <!-- 数据源内加载的PreparedStatements数量，和checkoutTimeout合用可能会导致死锁 -->
        <property name="maxIdleTime" value="20"/>              <!-- 最大空闲时间(秒)。若为0则永不丢弃 -->
        <property name="acquireIncrement" value="5"/>              <!-- 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 -->
        <property name="acquireRetryAttempts" value="10"/>         <!-- 定义在从数据库获取新连接失败后重复尝试的次数 -->
        <property name="idleConnectionTestPeriod" value="60"/> <!-- 每X秒检查所有连接池中的空闲连接 -->
        <property name="testConnectionOnCheckin" value="true"/>
        <property name="preferredTestQuery" value="select 1 from dual"/>
        <property name="breakAfterAcquireFailure" value="false"/>    <!-- 如果连接无法从数据库中获取一个acquireretryattempts后获得永久关闭 -->
    </bean>

    <bean id="shard_0_read_0" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${driverClass_shard_0}"/>
        <property name="jdbcUrl" value="${jdbcUrl_shard_0}"/>
        <property name="user" value="${user_shard_0}"/>
        <property name="password" value="${password_shard_0}"/>
        <property name="initialPoolSize" value="5"/>              <!-- 初始大小 -->
        <property name="minPoolSize" value="5"/>                  <!-- 最小连接处 -->
        <property name="maxPoolSize" value="50"/>              <!-- 连接池中保留的最大连接数 -->
        <property name="maxStatements"
                  value="0"/>              <!-- 数据源内加载的PreparedStatements数量，和checkoutTimeout合用可能会导致死锁 -->
        <property name="maxIdleTime" value="20"/>              <!-- 最大空闲时间(秒)。若为0则永不丢弃 -->
        <property name="acquireIncrement" value="5"/>              <!-- 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 -->
        <property name="acquireRetryAttempts" value="10"/>         <!-- 定义在从数据库获取新连接失败后重复尝试的次数 -->
        <property name="idleConnectionTestPeriod" value="60"/> <!-- 每X秒检查所有连接池中的空闲连接 -->
        <property name="testConnectionOnCheckin" value="true"/>
        <property name="preferredTestQuery" value="select 1 from dual"/>
        <property name="breakAfterAcquireFailure" value="false"/>    <!-- 如果连接无法从数据库中获取一个acquireretryattempts后获得永久关闭 -->
    </bean>

    <bean id="shard_0_read_1" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${driverClass_shard_0}"/>
        <property name="jdbcUrl" value="${jdbcUrl_shard_0}"/>
        <property name="user" value="${user_shard_0}"/>
        <property name="password" value="${password_shard_0}"/>
        <property name="initialPoolSize" value="5"/>              <!-- 初始大小 -->
        <property name="minPoolSize" value="5"/>                  <!-- 最小连接处 -->
        <property name="maxPoolSize" value="50"/>              <!-- 连接池中保留的最大连接数 -->
        <property name="maxStatements"
                  value="0"/>              <!-- 数据源内加载的PreparedStatements数量，和checkoutTimeout合用可能会导致死锁 -->
        <property name="maxIdleTime" value="20"/>              <!-- 最大空闲时间(秒)。若为0则永不丢弃 -->
        <property name="acquireIncrement" value="5"/>              <!-- 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 -->
        <property name="acquireRetryAttempts" value="10"/>         <!-- 定义在从数据库获取新连接失败后重复尝试的次数 -->
        <property name="idleConnectionTestPeriod" value="60"/> <!-- 每X秒检查所有连接池中的空闲连接 -->
        <property name="testConnectionOnCheckin" value="true"/>
        <property name="preferredTestQuery" value="select 1 from dual"/>
        <property name="breakAfterAcquireFailure" value="false"/>    <!-- 如果连接无法从数据库中获取一个acquireretryattempts后获得永久关闭 -->
    </bean>

    <bean id="shard_1_write" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${driverClass_shard_1}"/>
        <property name="jdbcUrl" value="${jdbcUrl_shard_1}"/>
        <property name="user" value="${user_shard_1}"/>
        <property name="password" value="${password_shard_1}"/>
        <property name="initialPoolSize" value="5"/>              <!-- 初始大小 -->
        <property name="minPoolSize" value="5"/>                  <!-- 最小连接处 -->
        <property name="maxPoolSize" value="50"/>              <!-- 连接池中保留的最大连接数 -->
        <property name="maxStatements"
                  value="0"/>              <!-- 数据源内加载的PreparedStatements数量，和checkoutTimeout合用可能会导致死锁 -->
        <property name="maxIdleTime" value="20"/>              <!-- 最大空闲时间(秒)。若为0则永不丢弃 -->
        <property name="acquireIncrement" value="5"/>              <!-- 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 -->
        <property name="acquireRetryAttempts" value="10"/>         <!-- 定义在从数据库获取新连接失败后重复尝试的次数 -->
        <property name="idleConnectionTestPeriod" value="60"/> <!-- 每X秒检查所有连接池中的空闲连接 -->
        <property name="testConnectionOnCheckin" value="true"/>
        <property name="preferredTestQuery" value="select 1 from dual"/>
        <property name="breakAfterAcquireFailure" value="false"/>    <!-- 如果连接无法从数据库中获取一个acquireretryattempts后获得永久关闭 -->
    </bean>

    <bean id="shard_1_read_0" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${driverClass_shard_1}"/>
        <property name="jdbcUrl" value="${jdbcUrl_shard_1}"/>
        <property name="user" value="${user_shard_1}"/>
        <property name="password" value="${password_shard_1}"/>
        <property name="initialPoolSize" value="5"/>              <!-- 初始大小 -->
        <property name="minPoolSize" value="5"/>                  <!-- 最小连接处 -->
        <property name="maxPoolSize" value="50"/>              <!-- 连接池中保留的最大连接数 -->
        <property name="maxStatements"
                  value="0"/>              <!-- 数据源内加载的PreparedStatements数量，和checkoutTimeout合用可能会导致死锁 -->
        <property name="maxIdleTime" value="20"/>              <!-- 最大空闲时间(秒)。若为0则永不丢弃 -->
        <property name="acquireIncrement" value="5"/>              <!-- 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 -->
        <property name="acquireRetryAttempts" value="10"/>         <!-- 定义在从数据库获取新连接失败后重复尝试的次数 -->
        <property name="idleConnectionTestPeriod" value="60"/> <!-- 每X秒检查所有连接池中的空闲连接 -->
        <property name="testConnectionOnCheckin" value="true"/>
        <property name="preferredTestQuery" value="select 1 from dual"/>
        <property name="breakAfterAcquireFailure" value="false"/>    <!-- 如果连接无法从数据库中获取一个acquireretryattempts后获得永久关闭 -->
    </bean>

    <bean id="shard_1_read_1" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${driverClass_shard_1}"/>
        <property name="jdbcUrl" value="${jdbcUrl_shard_1}"/>
        <property name="user" value="${user_shard_1}"/>
        <property name="password" value="${password_shard_1}"/>
        <property name="initialPoolSize" value="5"/>              <!-- 初始大小 -->
        <property name="minPoolSize" value="5"/>                  <!-- 最小连接处 -->
        <property name="maxPoolSize" value="50"/>              <!-- 连接池中保留的最大连接数 -->
        <property name="maxStatements"
                  value="0"/>              <!-- 数据源内加载的PreparedStatements数量，和checkoutTimeout合用可能会导致死锁 -->
        <property name="maxIdleTime" value="20"/>              <!-- 最大空闲时间(秒)。若为0则永不丢弃 -->
        <property name="acquireIncrement" value="5"/>              <!-- 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 -->
        <property name="acquireRetryAttempts" value="10"/>         <!-- 定义在从数据库获取新连接失败后重复尝试的次数 -->
        <property name="idleConnectionTestPeriod" value="60"/> <!-- 每X秒检查所有连接池中的空闲连接 -->
        <property name="testConnectionOnCheckin" value="true"/>
        <property name="preferredTestQuery" value="select 1 from dual"/>
        <property name="breakAfterAcquireFailure" value="false"/>    <!-- 如果连接无法从数据库中获取一个acquireretryattempts后获得永久关闭 -->
    </bean>

    <bean id="dataSource" class="com.zdp.sharding.jdbc.ShardingDatasource">
        <property name="targetDataSources">
            <map>
                <entry key="shard_0_write" value-ref="shard_0_write"></entry>
                <entry key="shard_0_read_0" value-ref="shard_0_read_0"></entry>
                <entry key="shard_0_read_1" value-ref="shard_0_read_1"></entry>
                <entry key="shard_1_write" value-ref="shard_1_write"></entry>
                <entry key="shard_1_read_0" value-ref="shard_1_read_0"></entry>
                <entry key="shard_1_read_1" value-ref="shard_1_read_1"></entry>
            </map>
        </property>
    </bean>

    <bean id="dataSourceGroup0" class="com.zdp.sharding.jdbc.DataSourceGroup">
        <property name="writeKey" value="shard_0_write"></property>
        <property name="readKeys">
            <list>
                <value>shard_0_read_0</value>
                <value>shard_0_read_1</value>
            </list>
        </property>
    </bean>

    <bean id="dataSourceGroup1" class="com.zdp.sharding.jdbc.DataSourceGroup">
        <property name="writeKey" value="shard_1_write"></property>
        <property name="readKeys">
            <list>
                <value>shard_1_read_0</value>
                <value>shard_1_read_1</value>
            </list>
        </property>
    </bean>
```
2. 配置分库分表策略 
```java
<bean id="transDBShardingStrategy" class="com.zdp.sharding.jdbc.strategy.impl.DatabaseShardingStrategyImpl"></bean>

<bean id="transTableShardingStrategy" class="com.zdp.sharding.jdbc.strategy.impl.TableShardingStrategyImpl"></bean>
```
3. 配置拦截器
```java
    <bean class="com.zdp.sharding.jdbc.ShardingContext"/>
    <bean id="shardingInterceptor" class="com.zdp.sharding.jdbc.ShardingInterceptor" >
        <property name="dataSourceGroups">
            <map>
                <entry key="trans_shard_0" value-ref="dataSourceGroup0"></entry>
                <entry key="trans_shard_1" value-ref="dataSourceGroup1"></entry>
            </map>
        </property>
    </bean>
    <aop:config>
        <!--切入点-->
        <aop:pointcut id="shardingPoint"
                      expression="execution(public * com.zdp.sharding.jdbc.demo.dao.*.*(..)) "/>
        <!--在该切入点使用自定义拦截器-->
        <aop:advisor pointcut-ref="shardingPoint" advice-ref="shardingInterceptor"/>
    </aop:config>
```
4. 配置mybatis 
```java
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="configLocation" value="classpath:mybatis.xml"/>
    </bean>

    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg index="0" ref="sqlSessionFactory"/>
    </bean>

    <!-- 自动扫描 mapper 接口 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.zdp.sharding.jdbc.demo.dao"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <property name="sqlSessionTemplateBeanName" value="sqlSession"/>
    </bean>
```
5. 编写DAO接口
```java
// @Sharding表示此接口需要分库分表，transDBShardingStrategy表示分库策略使用transDBShardingStrategy，transTableShardingStrategy表示分表策略是transTableShardingStrategy
@Sharding(dbShardingStrategy = "transDBShardingStrategy",tableShardingStrategy = "transTableShardingStrategy")
public interface TransDAO {
    // @Write表示此方法要走主库，@ShardBy表示根据trans作为分库分表的依据
    @Write
    void insert(@ShardBy @Param("trans") Trans trans);
    @Read
    Trans query(@ShardBy @Param("trans") Trans trans);
}
```
6. 执行demo.sql
```java
create database trans_shard_0;
use trans_shard_0;
CREATE TABLE `trans_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

CREATE TABLE `trans_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

create database trans_shard_1;
use trans_shard_1;
CREATE TABLE `trans_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

CREATE TABLE `trans_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8
```
7. 执行Demo
```java
public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-*.xml");
        TransDAO transDAO  = (TransDAO) context.getBean("transDAO");
        Trans trans = new TransBuilder().setTransId("1").setTransName("1").build();
        transDAO.insert(trans);
        trans = new TransBuilder().setTransId("2").setTransName("2").build();
        transDAO.insert(trans);
        trans = new TransBuilder().setTransId("3").setTransName("3").build();
        transDAO.insert(trans);
        trans = new TransBuilder().setTransId("4").setTransName("4").build();
        transDAO.insert(trans);
        System.out.println(transDAO.query(new TransBuilder().setTransId("1").build()));
        System.out.println(transDAO.query(new TransBuilder().setTransId("2").build()));
        System.out.println(transDAO.query(new TransBuilder().setTransId("3").build()));
        System.out.println(transDAO.query(new TransBuilder().setTransId("4").build()));
    }
```