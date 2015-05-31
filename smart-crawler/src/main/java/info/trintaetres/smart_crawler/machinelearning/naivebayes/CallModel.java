package info.trintaetres.smart_crawler.machinelearning.naivebayes;

import info.trintaetres.smart_crawler.indexer.Searcher;
import info.trintaetres.smart_crawler.words.TextPreparer;

import java.util.Arrays;
import java.util.Set;

import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Vector;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CallModel {

	private static final Logger logger = LoggerFactory
			.getLogger(CallModel.class);
	
	public static void main(String[] args) throws ElasticsearchException, Exception {
	
		NaiveBayesImpl classifier = new NaiveBayesImpl();
		
		classifier.startClassficication();
		
		Searcher searcher = new Searcher();

		Set<SearchHit> values = searcher.getFromDomain("globo.com", 0, 10000);

		HashingTF tf = new HashingTF();
		
		for (SearchHit value : values) {

			String text = TextPreparer.prepare(value.getSource()
					.get("text").toString());
			
			String domain = value.getSource().get("domain").toString();

			Vector vector = tf.transform(Arrays.asList(text.split(" ")));
			
			double result = classifier.classify(vector);
			
			logger.info("The class of a document of the domain "+domain+" is "+result);
		}		
	}
	

}
