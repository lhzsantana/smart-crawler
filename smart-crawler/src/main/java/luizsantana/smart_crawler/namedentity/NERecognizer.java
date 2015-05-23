package luizsantana.smart_crawler.namedentity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class NERecognizer {

	private static AbstractSequenceClassifier<CoreLabel> classifier;

	public NERecognizer() {

		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";

		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Pair> findEnglishNames(String content)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {

		List<List<CoreLabel>> resp = classifier.classify(content);

		List<Pair> names = new ArrayList<Pair>();
		
		for (List<CoreLabel> sentence : resp) {
			for (CoreLabel word : sentence) {

				Pair pair = new Pair();
				pair.setCategory(word
						.getString(CoreAnnotations.AnswerAnnotation.class));
				pair.setName(word.word());

				names.add(pair);
			}
		}
		
		return names;
	}
}
