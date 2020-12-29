package pl.uniq.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSource {

    @Bean
    public HikariDataSource hikariDataSource()
    {
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://db:5432/uniq")
                .username("admin")
                .password("admin")
                .type(HikariDataSource.class)
                .build();
    }
}
