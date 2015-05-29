package luizsantana.smart_crawler.machinelearning;

import java.util.Set;

import luizsantana.smart_crawler.indexer.Searcher;
import luizsantana.smart_crawler.words.TextPreparer;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

public class DataSetTrainerImpl {//implements DataSetTrainer {

	//SparkConf conf = new SparkConf().setAppName("SVM Classifier");
	//SparkContext sc = new SparkContext(conf);

	private static final Logger logger = LoggerFactory.getLogger(DataSetTrainerImpl.class);

	public static void main(String [] args) {
	
		DataSetTrainerImpl trainer = new DataSetTrainerImpl();
		//trainer.train("SVM Classifier",null,100);
		
		trainer.createDataSet("infomoney.com.br", "Finance", 100);
	}
	
	public void createDataSet(String domain, String classification, int sampleSize){
		
		Searcher searcher = new Searcher();	
		try {
			Set<SearchHit> values = searcher.getFromDomain(domain,sampleSize);
			
			for(SearchHit value: values){
				
				String text=TextPreparer.prepare(value.getSource().get("text").toString());
				
				logger.info(classification +"\t"+text);
			}
		} catch (ElasticsearchException e) {
			logger.error(e.getMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}			
	}
	
	/*
	public void train(String path, String [] classes, int numIterations) {

		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path)
				.toJavaRDD();

		// Split initial RDD into two... [60% training data, 40% testing data].
		JavaRDD<LabeledPoint> training = data.sample(false, 0.6, 11L);
		training.cache();
		JavaRDD<LabeledPoint> test = data.subtract(training);

		// Run training algorithm to build the model.
		final SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);

		// Clear the default threshold.
		model.clearThreshold();

		// Compute raw scores on the test set.
		JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test
				.map(new Function<LabeledPoint, Tuple2<Object, Object>>() {
					public Tuple2<Object, Object> call(LabeledPoint p) {
						Double score = model.predict(p.features());
						return new Tuple2<Object, Object>(score, p.label());
					}
				});

		// Get evaluation metrics.
		BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(
				JavaRDD.toRDD(scoreAndLabels));
		
		double auROC = metrics.areaUnderROC();
	}*/
}
