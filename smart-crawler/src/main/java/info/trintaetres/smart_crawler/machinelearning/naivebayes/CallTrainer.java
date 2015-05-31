package info.trintaetres.smart_crawler.machinelearning.naivebayes;

import info.trintaetres.smart_crawler.indexer.Searcher;
import info.trintaetres.smart_crawler.machinelearning.SVMImpl;
import info.trintaetres.smart_crawler.words.TextPreparer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallTrainer {

	private static final Logger logger = LoggerFactory
			.getLogger(CallTrainer.class);

	public static void main(String[] args) {

		CallTrainer caller = new CallTrainer();

		List<LabeledPoint> trainList = new ArrayList<LabeledPoint>();

		// 0 for Music
		trainList.addAll(caller.createDataSet("musica.com.br", 0d, 0, 50));
		// 1 for Finance
		trainList.addAll(caller.createDataSet("infomoney.com.br", 1d, 0, 50));

		List<LabeledPoint> testList = new ArrayList<LabeledPoint>();

		// 0 for Music
		trainList.addAll(caller.createDataSet("musica.com.br", 0d, 50, 50));
		// 1 for Finance
		trainList.addAll(caller.createDataSet("valor.com.br", 1d, 50, 50));
		
		boolean naive = false;

		if (naive) {

			NaiveBayesImpl trainer = new NaiveBayesImpl();
			trainer.train(trainList, testList);
			
		} else {

			SVMImpl trainer = new SVMImpl();
			trainer.train(trainList, testList);
		}

	}

	public List<LabeledPoint> createDataSet(String domain,
			Double classification, int from, int sampleSize) {

		List<LabeledPoint> labeledPoints = new ArrayList<LabeledPoint>();

		try {

			Searcher searcher = new Searcher();

			Set<SearchHit> values = searcher.getFromDomain(domain, from,
					sampleSize);

			HashingTF tf = new HashingTF();

			for (SearchHit value : values) {

				String text = TextPreparer.prepare(value.getSource()
						.get("text").toString());

				labeledPoints.add(new LabeledPoint(classification, tf
						.transform(Arrays.asList(text.split(" ")))));

				logger.debug(classification + "\t" + text);

				if (labeledPoints.size() > sampleSize)
					break;
			}
		} catch (ElasticsearchException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return labeledPoints;
	}
}
