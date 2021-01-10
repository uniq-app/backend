package pl.uniq.datasource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DataSourceTest {

	@Value("${POSTGRES_HOST}")
	String dbHost;
	@Value("${POSTGRES_DB}")
	String dbName;
	@Value("${POSTGRES_USER}")
	String dbUser;
	@Value("${POSTGRES_PASSWORD}")
	String dbPassword;

	@Test
	@Disabled
	//TODO[darullef]
	void test() throws SQLException {
		String url = "jdbc:postgresql://" + dbHost + "/" + dbName;
		Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
		assertTrue(connection.isValid(10));
	}
}
