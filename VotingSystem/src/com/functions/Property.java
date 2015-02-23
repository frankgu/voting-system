package com.functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {

	private final static Properties prop = new Properties();

	public Property() {

	}

	public String loadProperties(String value) throws IOException {

		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}

		return prop.getProperty(value);
	}
}
