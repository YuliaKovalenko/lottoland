package common.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

	private static final String PROP_FILE = "/application.properties";

	public static String loadProperty(String propertyName) {
		Properties props = new Properties();
		try {
			props.load(PropertyLoader.class.getResourceAsStream(PROP_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String value = null;

		if (propertyName != null) {
			value = props.getProperty(propertyName);
		}
		return value;
	}
}