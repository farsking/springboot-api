package com.yanbin.config;

import com.google.common.collect.Lists;
import com.yanbin.db.DataSourceChangeAdvice;
import com.yanbin.db.DataSourceSwitcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by yanbin on 2017/7/5.
 * 数据库配置
 */
@Configuration
public class DataSourceConfig {

    private static Logger log = LoggerFactory.getLogger(DataSourceConfig.class);


    @Value("${spring.dataSource.type}")
    private Class<? extends DataSource> dataSourceType;
    /**
     * 写库 数据源配置
     * @return
     */
    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.dataSource.master")
    public DataSource masterDataSource() {
        log.info("-------------------- master init ---------------------");
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    /**
     * 有多少个从库就要配置多少个
     * @return
     */
    @Bean(name = "salve1")
    @ConfigurationProperties(prefix = "spring.dataSource.slave1")
    public DataSource slaveDataSource1() {
        log.info("-------------------- slave1 DataSourceOne init ---------------------");
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "slave2")
    @ConfigurationProperties(prefix = "spring.dataSource.slave2")
    public DataSource slaveDataSource2() {
        log.info("-------------------- slave2 DataSourceTwo init ---------------------");
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name="dataSourceAdvice")
    public DataSourceChangeAdvice dataSourceChangeAdvice(){
        List<String> slaves = Lists.newArrayList();
        slaves.add("slave1");
        slaves.add("slave2");
        return new DataSourceChangeAdvice(new DataSourceSwitcher(slaves));
    }

}

