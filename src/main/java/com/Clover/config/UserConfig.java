package com.Clover.config;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.Clover.moneykit_repo", entityManagerFactoryRef = "userEntityManager", transactionManagerRef = "userTransactionManager")
public class UserConfig {
	@Autowired
	private Environment env;

	@Bean
	@ConfigurationProperties(prefix = "spring.user.datasource")
	public DataSource sql2DataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.user.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.user.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.user.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.user.datasource.password"));
		return dataSource;
	}

	@Bean(name = "userEntityManager")
	public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(sql2DataSource()).properties(hibernateProperties()).packages("com.Clover.Model_moneykit")
				.persistenceUnit("userPU").build();
	}

	@Bean(name = "userTransactionManager")
	public PlatformTransactionManager postgresqlTransactionManager(
			@Qualifier("userEntityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, ?> hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		return properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()));
	}
}
