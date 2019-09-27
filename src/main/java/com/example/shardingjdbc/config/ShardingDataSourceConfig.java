package com.example.shardingjdbc.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sharding-jdbc
 * @description: 分库分表配置文件
 * @author: yaKun.shi
 * @create: 2019-09-19 09:40
 **/
@Configuration
@MapperScan("com.example.shardingjdbc.dao.sharding")
public class ShardingDataSourceConfig {

    private final static String testLogicTable = "test_db";
    private final static String logLogicTable = "log";

    /**
     * 配置数据源0，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     *
     * @return
     * @date 2019-09-19 09:40
     */
    @Bean(name = "dataSource0")
    @ConfigurationProperties(prefix = "spring.datasource.db0")
    public DataSource dataSource0() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 配置数据源1，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     *
     * @return
     * @date 2019-09-19 09:40
     */
    @Bean(name = "dataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource dataSource1() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * shardingjdbc数据源
     *
     * @return
     * @throws SQLException
     * @date 2019-09-19 09:40
     */
    @Bean("shardIngDataSource")
    public DataSource shardIngDataSource(@Qualifier("dataSource0") DataSource dataSource0,
                                         @Qualifier("dataSource1") DataSource dataSource1) throws SQLException {
        // 第一步: 获取众多数据源  配置真实数据源


        Map<String, DataSource> dataSourceMap = new HashMap<>(16);
        dataSourceMap.put("db0", dataSource0);
        dataSourceMap.put("db1", dataSource1);

        // 第二步: 获取总配置类 切片规则
        ShardingRuleConfiguration shardIngRuleConfig = new ShardingRuleConfiguration();
        shardIngRuleConfig.getBindingTableGroups().add(testLogicTable);
        // 第三步: 获取其余配置信息(如果需要的话)
        Properties properties = getProperties();

        // 第四步: 定制指定逻辑表的切片(分库分表)策略
        List<TableRuleConfiguration> allTableRuleConfiguration = getAllTableRuleConfiguration();

        // 第五步: 将定制了自己的切片策略的表的配置规则，加入总配置中
        shardIngRuleConfig.getTableRuleConfigs().addAll(allTableRuleConfiguration);

        // 第六步: 创建并返回shardIng总数据源,注入容器
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardIngRuleConfig, properties);
    }


    /**
     * 获取sqlSessionFactory实例
     *
     * @param shardIngDataSource
     * @return
     * @throws Exception
     * @date 2019-09-19 09:40
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("shardIngDataSource") DataSource shardIngDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardIngDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath:/mapper/sharding/*.xml"));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 需要手动配置事务管理器
     *
     * @param shardIngDataSource
     * @return
     * @date 2019-09-19 09:40
     */
    @Bean
    public DataSourceTransactionManager transactionalManager(@Qualifier("shardIngDataSource") DataSource shardIngDataSource) {
        return new DataSourceTransactionManager(shardIngDataSource);
    }


    /**
     * 获取其余配置信息
     *
     * @return 其余配置信息
     * @date 2019-09-19 09:40
     */
    private Properties getProperties() {
        Properties properties = new Properties();
        // 打印出分库路由后的sql
        properties.setProperty("sql.show", "true");
        return properties;
    }

    /**
     * 对指定逻辑表进行切片(分库分表)个性化配置
     * 注:这里只配置了定制的
     *
     * @return 指定表的分库分表配置
     * @date 2019-09-19 09:40
     */
    private List<TableRuleConfiguration> getAllTableRuleConfiguration() {
        List<TableRuleConfiguration> list = new ArrayList<>(8);
        TableRuleConfiguration testDbRuleConfig = getTestDbRuleConfig();
//        TableRuleConfiguration logRuleConfig = getLogRuleConfig();
        list.add(testDbRuleConfig);
//        list.add(logRuleConfig);
        return list;
    }


    /**
     * test_db表分片策略
     *
     * @return 指定表的 分片策略
     * @date 2019-09-19 09:40
     */
    private TableRuleConfiguration getTestDbRuleConfig() {
        // 配置test_db表规则
        /*
         * 真实库表名
         *
         * 注：库与表之间使用【.】分割;
         * 注：库表与库表之间使用【,】分割
         *  db${0..1}.test_db${0..1}
         * 下述inline表达式的结果即为 db0.test_db0, db0.test_db1, db1.test_db0, db1.test_db1
         */

        String nodes = "db0.test_db0,db0.test_db1";
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration(testLogicTable, "db${0..1}.test_db${0..1}");


        //分布式id配置使用  使用雪花算法
        KeyGeneratorConfiguration keyGeneratorConfiguration = new
                KeyGeneratorConfiguration("SNOWFLAKE", "id");
        tableRuleConfig.setKeyGeneratorConfig(keyGeneratorConfiguration);
        // 配置分库 + 分表策略


        // 使用StandardShardingStrategyConfiguration方法来分库
        tableRuleConfig.setDatabaseShardingStrategyConfig(
                new StandardShardingStrategyConfiguration("id", new DatabaseShardIngAlgorithm()));
        // 使用StandardShardingStrategyConfiguration方法来分库
        tableRuleConfig.setTableShardingStrategyConfig(
                new StandardShardingStrategyConfiguration("year", new TableShardingAlgorithm()));
        return tableRuleConfig;
    }


    /**
     * @Method
     * @Author yakun.shi
     * @Description 使用 InlineShardingStrategyConfiguration 分库分表
     * @Return
     * @Date 2019/9/20 9:51
     */
    private void setShardingRuleConfig(TableRuleConfiguration tableRuleConfig) {
        // 分库策略
        tableRuleConfig.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("id", "db${id % 2}"));
        // 分表策略
        tableRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("year", "test_db${year % 2}"));
    }


    @Autowired
    private LogTableShardIngAlgorithm logTableShardIngAlgorithm;

    /**
     * log表分片策略
     * @return
     */
    private TableRuleConfiguration getLogRuleConfig() {
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration(logLogicTable);
        //分布式id配置使用  使用雪花算法
        KeyGeneratorConfiguration keyGeneratorConfiguration = new
                KeyGeneratorConfiguration("SNOWFLAKE", "id");
        tableRuleConfig.setKeyGeneratorConfig(keyGeneratorConfiguration);
        // 配置分库 + 分表策略


        // 使用StandardShardingStrategyConfiguration方法来分库
//        tableRuleConfig.setDatabaseShardingStrategyConfig(
//                new StandardShardingStrategyConfiguration("id", new DatabaseShardIngAlgorithm()));
        // 使用StandardShardingStrategyConfiguration方法来分库
        // 分库策略
        tableRuleConfig.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("id", "db0"));

        tableRuleConfig.setTableShardingStrategyConfig(
                new StandardShardingStrategyConfiguration("createtime", logTableShardIngAlgorithm));
        return tableRuleConfig;
    }

}
