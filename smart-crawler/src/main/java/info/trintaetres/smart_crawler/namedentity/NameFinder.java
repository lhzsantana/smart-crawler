package info.trintaetres.smart_crawler.namedentity;

import info.trintaetres.smart_crawler.crawler.examples.papers.beans.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class NameFinder {

	private static Logger log = Logger.getLogger(NameFinder.class);
	private static AbstractSequenceClassifier<CoreLabel> classifier;
	
	public NameFinder() throws ClassCastException, ClassNotFoundException, IOException{
		
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";

		classifier = CRFClassifier.getClassifier(serializedClassifier);
	}

	public List<User> findPersons(String content)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {
		
		return findEnglishNames(content, "PERSON");
	}

	public List<User> findOrganizations(String content)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {
		
		return findEnglishNames(content, "ORGANIZATION");
	}

	public List<User> findLocations(String content)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {
		
		return findEnglishNames(content, "LOCATION");
	}

	public List<User> findPhones(String content)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {
		
		return findEnglishNames(content, "PHONE");
	}

	private List<User> findEnglishNames(String content, String annotation)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {

		log.info(content);

		Document doc = Jsoup.parse(content);
		Elements links = doc.select("a[href]");

		List<User> names = new ArrayList<User>();
		
		for (Element link : links) {

			List<List<CoreLabel>> resp = classifier.classify(link.text());

			boolean isPerson = false;
			
			for (List<CoreLabel> sentence : resp) {
				for (CoreLabel word : sentence) {

					if(word.getString(CoreAnnotations.AnswerAnnotation.class).equals(annotation)) isPerson = true;
				}
			}
			
			if(isPerson){
				
				User user = new User();
				user.setName(link.text());
				user.setUrl(link.html());
				//log.info(link.text());
				names.add(user);
			}
		}

		return names;
	}
	
	public List<Pair> findAllAnnotation(String content)
			throws InvalidFormatException, IOException, ClassCastException,
			ClassNotFoundException {

		log.info(content);

		Document doc = Jsoup.parse(content);
		Elements links = doc.select("a[href]");

		List<Pair> pairs = new ArrayList<Pair>();
		
		for (Element link : links) {
			/*
			List<List<CoreLabel>> resp = classifier.classify(link.text());

			boolean isPerson = false;
			
			for (List<CoreLabel> sentence : resp) {
				for (CoreLabel word : sentence) {

					Pair pair = new Pair();
					pair.setAnnotation(word.getString(CoreAnnotations.AnswerAnnotation.class));
					pair.setWord(word.word());

					log.info(word.word() + '-' + word.get(CoreAnnotations.AnswerAnnotation.class));
					
					pairs.add(pair);
				}
			}
			*/
		}

		return pairs;
	}
}
