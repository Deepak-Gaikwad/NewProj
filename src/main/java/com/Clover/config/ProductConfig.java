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
import org.springframework.context.annotation.Primary;
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
@EnableJpaRepositories(basePackages = "com.Clover.slonkit_repo", entityManagerFactoryRef = "productEntityManager", transactionManagerRef = "productTransactionManager")
public class ProductConfig {

	@Autowired
	private Environment env;

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.product.datasource")
	public DataSource sqlDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.product.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.product.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.product.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.product.datasource.password"));

		return dataSource;
	}

	@Primary
	@Bean(name = "productEntityManager")
	public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(sqlDataSource()).properties(hibernateProperties()).packages("com.Clover.Model_slonkit")
				.persistenceUnit("productPU").build();
	}

	@Primary
	@Bean(name = "productTransactionManager")
	public PlatformTransactionManager mysqlTransactionManager(
			@Qualifier("productEntityManager") EntityManagerFactory entityManagerFactory) {
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
