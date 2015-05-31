package info.trintaetres.smart_crawler.machinelearning.naivebayes;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

public class NaiveBayesImpl  implements Serializable {
	
	private static SparkConf conf;
	private static JavaSparkContext sc;
	private static NaiveBayesModel model;
	
	public void startClassficication(){

		conf = new SparkConf().setAppName("Naive Bayes Classifier")
				.setMaster("spark://luiz-System-Product-Name:7077")
				.setJars(new String[]{"target/smart-crawler-0.0.1-SNAPSHOT.jar"})
				.set("spark.akka.frameSize", "20");
		
		sc = new JavaSparkContext(conf);

		NaiveBayesModel model = NaiveBayesModel.load(sc.sc(), "");
	}
	
	private static final long serialVersionUID = 5209690473689123129L;


	private static final Logger logger = LoggerFactory
			.getLogger(NaiveBayesImpl.class);
	
	public void train(List<LabeledPoint> trainList, List<LabeledPoint> testList) {

		SparkConf conf = new SparkConf().setAppName("SVM Classifier")
				.setMaster("spark://luiz-System-Product-Name:7077")
				.setJars(new String[]{"target/smart-crawler-0.0.1-SNAPSHOT.jar"})
				.set("spark.akka.frameSize", "40");
		
		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<LabeledPoint> training = sc.parallelize(trainList, 2).cache();
		JavaRDD<LabeledPoint> test = sc.parallelize(testList, 2).cache();

		final NaiveBayesModel model = NaiveBayes.train(training.rdd(), 1.0);

		JavaPairRDD<Double, Double> predictionAndLabel = 
		  test.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
			  
			private static final long serialVersionUID = -4498879256866700408L;

			@Override public Tuple2<Double, Double> call(LabeledPoint p) {
		      return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
		    }
		  });
		
		double accuracy = predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
			
			private static final long serialVersionUID = 8604799362581634343L;

			@Override public Boolean call(Tuple2<Double, Double> pl) {
		      return pl._1().equals(pl._2());
		    }
		  }).count() / (double) test.count();

		logger.info("Model accuracy "+accuracy);
		
		// Save and load model
		model.save(sc.sc(), "myModelPath3");
		
		sc.close();
	}
	
	public double classify(Vector testData){
		
		return model.predict(testData);
	}
}
