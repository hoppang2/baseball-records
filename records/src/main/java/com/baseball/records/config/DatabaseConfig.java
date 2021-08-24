package com.baseball.records.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.yml")
public class DatabaseConfig {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private final ApplicationContext applicationContext;

    public DatabaseConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
//net.sf.log4jdbc.DriverSpy
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();

        DataSource dataSource = new HikariDataSource(hikariConfig());
        logger.info("datasource : {}", dataSource.toString());
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/sql/map/*/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/sql/sql-config.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplatePrimary")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
