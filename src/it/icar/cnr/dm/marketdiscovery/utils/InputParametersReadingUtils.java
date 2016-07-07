package it.icar.cnr.dm.marketdiscovery.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class InputParametersReadingUtils {

	public static double readParametersAsDouble(Properties configuration,
			String parameterName) {

		String valueAsString = configuration.getProperty(parameterName).trim();

		double value = 0.;

		try {
			value = Double.parseDouble(valueAsString);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(">>Error in parsing parameter " + parameterName
					+ ", unparsable value :" + valueAsString);
			System.exit(-1);
		}

		return value;

	}

	public static double[] readParametersAsDoubleArray(
			Properties configuration, String parameterName, String separator) {

		String[] readParametersAsStringArray = readParametersAsStringArray(
				configuration, parameterName, separator);
		double[] result = new double[readParametersAsStringArray.length];
		for (int i = 0; i < readParametersAsStringArray.length; i++)
			result[i] = Double.parseDouble(readParametersAsStringArray[i]
					.trim());
		return result;

	}

	public static int readParametersAsInt(Properties configuration,
			String parameterName) {

		String valueAsString = configuration.getProperty(parameterName).trim();

		int value = 0;

		try {
			value = Integer.parseInt(valueAsString);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(">>Error in parsing parameter " + parameterName
					+ ", unparsable value :" + valueAsString);
			System.exit(-1);
		}

		return value;

	}
	
	public static boolean readParametersAsBoolean(Properties configuration,
			String parameterName) {
			
		String valueAsString = configuration.getProperty(parameterName).trim();
		
		boolean value = false;
		
		try{
			value=Boolean.parseBoolean(valueAsString);
			
		}catch(Exception e){
			System.err.println(">>Error in parsing parameter " + parameterName
					+ ", unparsable value :" + valueAsString);
			System.exit(-1);
		}
		
		
		return value;
		
	}

	public static int[] readParametersAsIntArray(Properties configuration,
			String parameterName, String separator) {

		String[] readParametersAsStringArray = readParametersAsStringArray(
				configuration, parameterName, separator);
		int[] result = new int[readParametersAsStringArray.length];
		for (int i = 0; i < readParametersAsStringArray.length; i++)
			result[i] = Integer.parseInt(readParametersAsStringArray[i].trim());
		return result;

	}

	public static String readParametersAsString(Properties configuration,
			String parameterName) {
		return configuration.getProperty(parameterName);
	}

	public static String[] readParametersAsStringArray(
			Properties configuration, String parameterName, String separator) {
		String[] split = configuration.getProperty(parameterName).split(
				separator);
		String[] cleanedSplit = new String[split.length];
		for (int i = 0; i < split.length; i++)
			cleanedSplit[i] = split[i].trim();
		return cleanedSplit;
	}

	public static Properties loadParameters(String path) throws IOException,
			FileNotFoundException {
		Properties configuration = new Properties();
		configuration.load(new FileInputStream(new File(path)));
		return configuration;
	}

}
