package it.icar.cnr.dm.marketdiscovery;

import it.icar.cnr.dm.marketdiscovery.utils.ArffUtils;
import it.icar.cnr.dm.marketdiscovery.utils.InputParametersReadingUtils;
import it.icar.cnr.dm.marketdiscovery.utils.ModelFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import weka.classifiers.Evaluation;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;
import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MarketDiscovery {

	// *********CONSTANT**********

	public static int RANDOM_SEED = 28062016;
	private static final boolean DEBUG = true;
	public static int numFolds = 5;

	// private static double trainingPercentage = 0.7;

	/**
	 * @param args
	 * @throws
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// carico la configurazione
		String path = "resinput/configuration";
		String filepathname = "resoutput/test.arff";

		Properties configuration = InputParametersReadingUtils
				.loadParameters(path);

		boolean idf = InputParametersReadingUtils.readParametersAsBoolean(
				configuration, "idf");
		boolean tf = InputParametersReadingUtils.readParametersAsBoolean(
				configuration, "tf");
		boolean lowercase = InputParametersReadingUtils
				.readParametersAsBoolean(configuration, "lowercase");
		String stopwords = InputParametersReadingUtils.readParametersAsString(
				configuration, "stopwords");

		String delimiters = InputParametersReadingUtils.readParametersAsString(
				configuration, "delimiters");

		int wordsTokeep = InputParametersReadingUtils.readParametersAsInt(
				configuration, "wordsTokeep");

		int maxNGramSize = InputParametersReadingUtils.readParametersAsInt(
				configuration, "maxNGramSize");

		Instances dataset = ArffUtils.loadDataset(filepathname);

		// data preparation
		dataset.setClassIndex(dataset.numAttributes() - 1);
		dataset.deleteAttributeAt(0);

		// // creazione e settaggio parametri StringToWordVwctor
		// StringToWordVector stwv = prepareFilter(idf, tf, lowercase,
		// wordsTokeep, stopwords, maxNGramSize, delimiters);
		//
		// // Init Classifier
		// InputMappedClassifier imc = (InputMappedClassifier) ModelFactory
		// .createTextClassifier(stwv);
		//
		// // cross validation - 5 folds
		// System.out.println("***** cross validation - " + numFolds
		// + " folds *****");
		// Evaluation crossEval = EvaluationUtils.evaluateModel(dataset, imc,
		// RANDOM_SEED);
		//
		// // print cross - evaluation
		// System.out.println(crossEval.toClassDetailsString());
		// System.out.println(crossEval.toSummaryString());

		// imc.buildClassifier(preprocessedDataset);
		// System.out.println("MODEL:");
		// System.out.println(imc);

		// training e test evaluation
		System.out.println("***** Hold-out evaluation *****");

		// split training set

		Instances training, test;

		//
		Instances[] split = ArffUtils.stratifiedSplit(dataset, 0.8);
		//
		training = split[0];
		test = split[1];

		// building model

		InputMappedClassifier holdOutModel = (InputMappedClassifier) ModelFactory
				.createTextClassifier(prepareFilter(idf, tf, lowercase,
						wordsTokeep, stopwords, maxNGramSize, delimiters));

		holdOutModel.buildClassifier(training);

		System.out.println(holdOutModel.getClassifier().toString());

		System.out.println(">test size: " + test.size());

		Evaluation holdoutEval = new Evaluation(test);
		holdoutEval.evaluateModel(holdOutModel, test);

		System.out.println(holdoutEval.toClassDetailsString());
		System.out.println(holdoutEval.toSummaryString());

		// System.out.println(test);
		// System.out.println(holdOutModel.getClassifier());

	}

	private static StringToWordVector prepareFilter(boolean useIdf,
			boolean useTf, boolean applyLowercase, int featuresTokeep,
			String stopwordsListFile, int maxNGramSize, String delimiters)
			throws IOException {

		StringToWordVector stwv = new StringToWordVector();
		stwv.setWordsToKeep(featuresTokeep);
		stwv.setIDFTransform(useIdf);
		stwv.setTFTransform(useTf);
		stwv.setLowerCaseTokens(applyLowercase);
		stwv.setOutputWordCounts(true);
		// SnowballStemmer sns = new SnowballStemmer();
		// stwv.setStemmer(sns);
		WordsFromFile sw = new WordsFromFile();
		sw.setStopwords(new File(stopwordsListFile));
		stwv.setStopwordsHandler(sw);

		NGramTokenizer tokenizer = new NGramTokenizer();
		tokenizer.setNGramMaxSize(maxNGramSize);
		tokenizer.setNGramMinSize(maxNGramSize);
		tokenizer.setDelimiters(delimiters);
		// Setting tokenizer
		stwv.setTokenizer(tokenizer);

		// AlphabeticTokenizer at = new AlphabeticTokenizer();

		// stwv.setTokenizer(at);

		// System.out.println(preprocessedDataset);

		return stwv;

	}
}
