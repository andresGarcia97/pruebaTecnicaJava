package co.com.config;

import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@EnableConfigurationProperties({ LiquibaseProperties.class })
public class LiquibaseConfiguration {

	private static final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

	private static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

	private final Environment env;

	@Value("${application.liquibase.async:false}")
	private boolean async;

	public LiquibaseConfiguration(Environment env) {
		this.env = env;
	}

	@Bean
	SpringLiquibase liquibase(@Qualifier("taskExecutor") Executor executor,
			@LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
			LiquibaseProperties liquibaseProperties, ObjectProvider<DataSource> dataSource,
			DataSourceProperties dataSourceProperties) {
		SpringLiquibase liquibase;
		if (async) {
			log.info("Configuring Liquibase with async");
			liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(this.env, executor,
					liquibaseDataSource.getIfAvailable(), liquibaseProperties, dataSource.getIfUnique(),
					dataSourceProperties);
		} else {
			log.info("Configuring Liquibase with sync");
			liquibase = SpringLiquibaseUtil.createSpringLiquibase(liquibaseDataSource.getIfAvailable(),
					liquibaseProperties, dataSource.getIfUnique(), dataSourceProperties);
		}
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
		liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
		liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
		liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
		liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
		liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
		liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
		liquibase.setShouldRun(liquibaseProperties.isEnabled());
		if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_NO_LIQUIBASE))) {
			liquibase.setShouldRun(false);
			log.warn("Liquibase disabled");
		} else {
			liquibase.setShouldRun(liquibaseProperties.isEnabled());
			log.info("Configuring Liquibase");
		}
		return liquibase;
	}

}
