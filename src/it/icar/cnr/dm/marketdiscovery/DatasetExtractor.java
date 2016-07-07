package it.icar.cnr.dm.marketdiscovery;

import it.icar.cnr.dm.marketdiscovery.utils.ArffUtils;

import java.io.IOException;

import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;

public class DatasetExtractor {

	public static void extractDatasetFromDB(String username, String password,
			String queryAsString, String outputFile, String stopwords)
			throws Exception, IOException {
		InstanceQuery query = new InstanceQuery();
		query.setUsername(username);
		query.setPassword(password);
		query.setQuery(queryAsString);

		// You can declare that your data set is sparse
		// query.setSparseData(true);
		Instances dataset = query.retrieveInstances();

		// TODO:controllare se si puo' evitare
		NominalToString converterType = new NominalToString();
		converterType.setAttributeIndexes("2");
		converterType.setInputFormat(dataset);
		dataset = Filter.useFilter(dataset, converterType);
		ArffUtils.cleanDataset(dataset, 1);
		ArffUtils.clean(dataset, 1, stopwords);
		ArffUtils.saveDatasetOnArffFile(outputFile, dataset);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		/***** READ PARAMETERS *******/

		String username = "root";
		String password = "pwd";
		String queryAsString = "select idLink,cleanbody,marketLabel from pages";
		String outputDir = "resoutput/";
		String outputFile = outputDir + "test.arff";
		String stopwords = "resinput/stopwords";

		/***** EXECUTE METHOD *******/
		extractDatasetFromDB(username, password, queryAsString, outputFile,
				stopwords);

	}
}
