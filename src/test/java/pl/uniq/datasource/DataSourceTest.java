package pl.uniq.datasource;

import org.junit.jupiter.api.Test;
import pl.uniq.EnvProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DataSourceTest {

	@Test
	void test() throws SQLException {
		String dbHost = EnvProvider.getEnv("POSTGRES_HOST");
		String dbName = EnvProvider.getEnv("POSTGRES_DB");
		String dbUser = EnvProvider.getEnv("POSTGRES_USER");
		String dbPassword = EnvProvider.getEnv("POSTGRES_PASSWORD");

		String url = "jdbc:postgresql://" + dbHost + "/" + dbName;
		Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
		assertTrue(connection.isValid(10));
	}
}
