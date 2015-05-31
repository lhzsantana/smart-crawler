package info.trintaetres.smart_crawler.machinelearning;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

public class SVMImpl  implements Serializable {
	
	private static final long serialVersionUID = 5209690473689123129L;

	private static final Logger logger = LoggerFactory
			.getLogger(SVMImpl.class);
	
	private static SparkConf conf;
	private static JavaSparkContext sc;
	private static NaiveBayesModel savedModel;
	
	public void startClassficication(String [] model){

		conf = new SparkConf().setAppName("SVM Classifier")
				.setMaster("spark://luiz-System-Product-Name:7077")
				.setJars(new String[]{"target/smart-crawler-0.0.1-SNAPSHOT.jar"})
				.set("spark.akka.frameSize", "20");
		
		sc = new JavaSparkContext(conf);
	}
		
	public String vote(List<String> models, Vector testData){
		
		NaiveBayesModel current;
		
		for(String model:models){

			current = NaiveBayesModel.load(sc.sc(), model);
			current.predict(testData);
		}
		
		return null;
	}
	
	public void train(List<LabeledPoint> trainList, List<LabeledPoint> testList) {

		SparkConf conf = new SparkConf().setAppName("SVM Classifier")
				.setMaster("spark://luiz-System-Product-Name:7077")
				.setJars(new String[]{"target/smart-crawler-0.0.1-SNAPSHOT.jar"})
				.set("spark.akka.frameSize", "40");
		
		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<LabeledPoint> training = sc.parallelize(trainList, 2).cache();
		JavaRDD<LabeledPoint> test = sc.parallelize(testList, 2).cache();

	    // Run training algorithm to build the model.
	    int numIterations = 100;
	    final SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);

	    // Clear the default threshold.
	    model.clearThreshold();

	    // Compute raw scores on the test set.
	    JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test.map(
	      new Function<LabeledPoint, Tuple2<Object, Object>>() {
	    	  
			private static final long serialVersionUID = -2386041653117582576L;

			public Tuple2<Object, Object> call(LabeledPoint p) {
	          Double score = model.predict(p.features());
	          return new Tuple2<Object, Object>(score, p.label());
	        }
	      }
	    );

	    // Get evaluation metrics.
	    BinaryClassificationMetrics metrics =
	      new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));
	    double auROC = metrics.areaUnderROC();

	    System.out.println("Area under ROC = " + auROC);

	    // Save and load model
	    model.save(sc.sc(), "myModelPath");
	    SVMModel sameModel = SVMModel.load(sc.sc(), "myModelPath");
	    
		sc.close();
	}
}
