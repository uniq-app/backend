package pl.uniq.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSource {

	@Value("${POSTGRES_HOST}")
	String dbHost;
	@Value("${POSTGRES_DB}")
	String dbName;
	@Value("${POSTGRES_USER}")
	String dbUser;
	@Value("${POSTGRES_PASSWORD}")
	String dbPassword;

	@Bean
    public HikariDataSource hikariDataSource()
    {
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://" + dbHost + "/" + dbName)
                .username(dbUser)
                .password(dbPassword)
                .type(HikariDataSource.class)
                .build();
    }
}
