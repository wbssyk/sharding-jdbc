package com.example.shardingjdbc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Method
 * @Author yakun.shi
 * @Description 不需要分库分表的配置
 * @Return
 * @Date 2019/9/19 17:33
 */
@Configuration
@MapperScan(basePackages = "com.example.shardingjdbc.dao.nosharding", sqlSessionTemplateRef = "noshardingSqlSessionTemplate")
public class NoShardingDataSourceConfig {

    /**
     * 获取sqlSessionFactory实例
     *
     * @param noShardIngDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "noshardingSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource0") DataSource noShardIngDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(noShardIngDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/nosharding/*.xml"));
        return bean.getObject();
    }


    @Bean(name = "noshardingSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("noshardingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
