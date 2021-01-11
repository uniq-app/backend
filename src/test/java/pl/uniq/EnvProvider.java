package pl.uniq;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EnvProvider {

	public static String getEnv(String key) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader("env.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
}
