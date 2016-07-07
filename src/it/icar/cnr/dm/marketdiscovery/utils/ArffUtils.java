package it.icar.cnr.dm.marketdiscovery.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

public class ArffUtils {

	public static String[] arrayStopWords(String stopwords) throws IOException {
		String[] stopWords = null;
		int n = countRows(stopwords);
		try {
			Scanner stopWordsFile = new Scanner(new File(stopwords));
			stopWords = new String[n];
			for (int i = 0; i < n; i++)
				stopWords[i] = stopWordsFile.next();
			stopWordsFile.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		return stopWords;
	}

	public static void clean(Instances dataset, int column, String stopwords)
			throws IOException {
		for (Instance inst : dataset) {
			removeStopWords(column, inst, stopwords);
		}
	}

	public static void cleanDataset(Instances dataset, int columnToClean) {
		for (Instance inst : dataset) {
			cleanInstance(columnToClean, inst);
		}
	}

	public static void cleanInstance(int columnToClean, Instance inst) {

		String value = inst.stringValue(columnToClean);
		value = value.replaceAll("�", "");
		inst.setValue(columnToClean, value);

	}

	public static int compareWords(String word1, String word2) {
		return word1.compareToIgnoreCase(word2);
	}

	// metodo che conta le linee di un file
	public static int countRows(String stopwords) throws IOException {
		int n = 0;
		FileReader f = new FileReader(new File(stopwords));
		BufferedReader br = new BufferedReader(f);
		while (br.readLine() != null) {
			n++;
		}
		br.close();
		return n;

	}

	// confronta una parola ad una lista di stopwords
	public static Boolean isStopWord(String word, String[] stopWords) {

		boolean found = false;
		for (int i = 0; i < stopWords.length; i++) {
			String stop = stopWords[i];
			if (word.equalsIgnoreCase(stop)) {
				found = true;
				break;
			}
		}
		return found;

	}

	public static Instances loadDataset(String filepathname)
			throws IOException, FileNotFoundException {

		Instances dataset = new Instances(
				new FileReader(new File(filepathname)));
		return dataset;

	}

	public static void main(String[] args) throws Exception {

		Instances dataset = new Instances(new FileReader(new File(
				"resoutput/test.arff")));

		int columnToClean = 1;

		String stopwords = "resinput/stopwords";

		// cleanDataset(dataset, columnToClean);

		clean(dataset, columnToClean, stopwords);
		System.out.println(dataset);

	}

	// public static Instances[] orderedStratifiedSplit(Instances dataset,
	// double pcg) throws Exception {
	//
	// Instances training = new Instances(dataset, 0);
	//
	// Instances test = new Instances(dataset, 0);
	//
	// List<List<Instance>> classes = new ArrayList<List<Instance>>(dataset
	//
	// .classAttribute().numValues());
	//
	// List<String> classValues = new ArrayList<String>();
	//
	// for (int i = 0; i < dataset.classAttribute().numValues(); i++)
	//
	// classValues.add(dataset.classAttribute().value(i));
	//
	// Collections.sort(classValues);
	//
	// for (int i = 0; i < dataset.classAttribute().numValues(); i++)
	//
	// classes.add(new ArrayList<Instance>());
	//
	// for (Instance inst : dataset) {
	//
	// int indexOfClassValue = (int) inst.classValue();
	//
	// int mapping = classValues.indexOf(dataset.classAttribute().value(
	//
	// indexOfClassValue));
	//
	// classes.get(mapping).add(inst);
	//
	// }
	//
	// for (List<Instance> currentClass : classes) {
	//
	// double numInstances = currentClass.size();
	//
	// double numTraining = numInstances * pcg;
	//
	// List<Instance> first = currentClass.subList(0, (int) numTraining);
	//
	// List<Instance> second = currentClass.subList((int) numTraining,
	//
	// (int) numInstances);
	//
	// training.addAll(first);
	//
	// test.addAll(second);
	//
	// }
	//
	// return new Instances[] { training, test };
	//
	// }

	public static void removeStopWords(int column, Instance inst,
			String stopwords) throws IOException {
		String[] wordsList = arrayStopWords(stopwords);
		ArrayList<String> word = new ArrayList<String>();
		String value = inst.stringValue(column);
		StringTokenizer st = new StringTokenizer(value);
		while (st.hasMoreTokens()) {
			String stringToTest = st.nextToken();
			if (!isStopWord(stringToTest, wordsList)) {
				word.add(stringToTest);
			}
		}
		// System.out.println(word);
		inst.setValue(column, word.toString());

	}

	public static void saveDatasetOnArffFile(String outputFile,
			Instances dataset) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		bw.write(dataset.toString());
		bw.flush();
		bw.close();
	}

	public static Instances[] stratifiedSplit(Instances dataset, double pcg)
			throws Exception {

		// RemovePercentage rp = new RemovePercentage();
		Resample rp = new Resample();
		rp.setNoReplacement(true);
		rp.setSampleSizePercent(pcg * 100);
		// rp.setPercentage(pcg * 100);
		rp.setInvertSelection(false);
		rp.setInputFormat(dataset);
		Instances training = Filter.useFilter(dataset, rp);
		training.setClassIndex(dataset.classIndex());
		rp.setInvertSelection(true);
		rp.setInputFormat(dataset);
		Instances test = Filter.useFilter(dataset, rp);
		test.setClassIndex(dataset.classIndex());
		return new Instances[] { training, test };

	}

}
