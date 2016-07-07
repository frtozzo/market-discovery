package it.icar.cnr.dm.marketdiscovery.utils;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.rules.JRip;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ModelFactory {

	private static AttributeSelectedClassifier createAttributeSelectionClassifier(
			AbstractClassifier baseModel) {

		AttributeSelectedClassifier attributeSelection = new AttributeSelectedClassifier();
		attributeSelection.setClassifier(baseModel);
		// attributeSelection.setEvaluator(new OneRAttributeEval());
		// Ranker ranker = new Ranker();
		// ranker.setNumToSelect(25);
		// attributeSelection.setSearch(ranker);
		// attributeSelection.setSearch(new EvolutionarySearch());
		return attributeSelection;

	}

	public static AbstractClassifier createTextClassifier(
			StringToWordVector stwv) {

		InputMappedClassifier imc = new InputMappedClassifier();
		imc.setSuppressMappingReport(true);

		// Base Classifier
		JRip baseModel = new JRip();

		// J48 baseModel = new J48();
		// NaiveBayes baseModel = new NaiveBayes();
		// A1DE baseModel = new A1DE();

		// baseModel.setFolds(5);
		// baseModel.setUsePruning(false);
		// NaiveBayes baseModel = new NaiveBayes();
		// Logistic baseModel = new Logistic();

		// Vector
		FilteredClassifier extractFeatures = new FilteredClassifier();
		extractFeatures.setFilter(stwv);
		extractFeatures.setClassifier(baseModel);

		// TODO: REINSERIRE
		// Attribute Selection
		// AttributeSelectedClassifier attributeSelection =
		// createAttributeSelectionClassifier(baseModel);

		// set model
		imc.setClassifier(extractFeatures);

		return imc;

	}
}
