package com.github.alexeysa83.finalproject.dao.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@Import(SettingsConfig.class)
@EnableTransactionManagement
@EnableJpaRepositories (basePackages = "com.github.alexeysa83.finalproject.dao.repository")
public class HibernateConfig {

    private final SettingsConfig settingsConfig;

    public HibernateConfig(SettingsConfig settingsConfig) {
        this.settingsConfig = settingsConfig;
    }

    @Bean
    public DataSource dataSource() {
        final DatasourceSettings datasourceSettings = settingsConfig.datasourceSettings();
        final HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(datasourceSettings.getUrl());
        hikariDataSource.setUsername(datasourceSettings.getUsernameDB());
        hikariDataSource.setPassword(datasourceSettings.getPassword());
        hikariDataSource.setDriverClassName(datasourceSettings.getDriver());
        hikariDataSource.setMaximumPoolSize(20);
        return hikariDataSource;

//        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        dataSource.setJdbcUrl(datasourceSettings.getUrl());
//        dataSource.setUser(datasourceSettings.getUsername());
//        dataSource.setPassword(datasourceSettings.getPassword());
//        try {
//            dataSource.setDriverClass(datasourceSettings.getDriver());
//        } catch (PropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
//        dataSource.setMinPoolSize(5);
//        dataSource.setAcquireIncrement(5);
//        dataSource.setMaxPoolSize(20);
//        dataSource.setMaxStatements(180);
//        return dataSource;
           }

    @Bean
    public LocalSessionFactoryBean entityManagerFactory() {
        final LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("com.github.alexeysa83.finalproject.dao.entity");
        sessionFactoryBean.setHibernateProperties(settingsConfig.hibernateProperties());
        return sessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

//    @Bean
//    public TransactionTemplate transactionTemplate() {
//        return new TransactionTemplate(transactionManager());
//    }
}
