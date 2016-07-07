package it.icar.cnr.dm.marketdiscovery.utils;

import it.icar.cnr.dm.marketdiscovery.MarketDiscovery;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;

public class EvaluationUtils {

	public static Evaluation evaluateModel(Instances preprocessedDataset,
			InputMappedClassifier imc, long seed) throws Exception {
		Evaluation crossEval = new Evaluation(preprocessedDataset);
		crossEval.crossValidateModel(imc, preprocessedDataset,
				MarketDiscovery.numFolds, new Random(seed));
		return crossEval;
	}

}
