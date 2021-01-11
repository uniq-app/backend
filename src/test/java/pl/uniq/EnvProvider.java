package pl.uniq;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EnvProvider {

	public static String getEnv(String key) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileReader("env.properties"));
		return properties.getProperty(key);
	}
}
