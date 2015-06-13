package info.trintaetres.smart_crawler.machinelearning.svm;

import info.trintaetres.smart_crawler.indexer.Searcher;
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
