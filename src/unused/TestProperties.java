package unused;

import it.icar.cnr.dm.marketdiscovery.utils.InputParametersReadingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class TestProperties {

	public static void main(String[] args) throws Exception {

		System.out.println(">>Reading Configuration File...");

		// if (args.length != 1)
		// throw new RuntimeException("Missing Parameters");

		// prendo il parametro del file di configurazione
		// String configurationFile = args[0];

		// carico la configurazione
		Properties configuration = new Properties();
		configuration.load(new FileInputStream(new File(
				"resinput/configuration")));

		// read parameters
		int x = InputParametersReadingUtils.readParametersAsInt(configuration,
				"parameterX");
		System.out.println(x);
		boolean test = InputParametersReadingUtils.readParametersAsBoolean(
				configuration, "par");
		System.out.println(test);

	}

}
