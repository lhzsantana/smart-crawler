package info.trintaetres.smart_crawler.machinelearning.svm;

import info.trintaetres.smart_crawler.machinelearning.GenericML;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

public class SVMImpl extends GenericML implements Serializable {

	private static final long serialVersionUID = 5209690473689123129L;

	private static final Logger logger = LoggerFactory.getLogger(SVMImpl.class);

	public double predictOneToOne(Vector data, String modelPath) {

		logger.info("Starting one to one SVM exec");
		
		SVMModel current = SVMModel.load(sc.sc(), modelPath);
		
		logger.info("Finishing one to one SVM exec");
				
		return current.predict(data);
	}

	public String predictOneVsAll(List<Model> models, Vector data) {

		logger.info("Starting one versus all SVM exec");
		
		SVMModel current;
		
		Map<String,Integer> votes = new HashMap<String,Integer>();

		for (Model model : models) {
			
			current = SVMModel.load(sc.sc(), model.getModelPath());
			double prediction=current.predict(data);
	
			if(prediction>0){				
				Integer currentVotes=votes.get(model.getClass1());
				if(currentVotes==null) currentVotes=0; 
				votes.put(model.getClass1(), ++currentVotes);
			}else{
				Integer currentVotes=votes.get(model.getClass0());
				if(currentVotes==null) currentVotes=0;
				votes.put(model.getClass0(), ++currentVotes);
			}

			logger.info("Prediction to model "+model.getModelPath()+" is "+prediction);
		
		}

		logger.info("Finishing one versus all SVM exec");		

		Integer maxVotes=0;
		String maxClass="";
		for(String model:votes.keySet()){
			
			if(votes.get(model)>maxVotes){
				maxVotes=votes.get(model);
				maxClass=model;
			}			
			logger.info(model+" have "+votes.get(model)+" votes");
		}		
		
		return maxClass;
	}
	
	public void train(List<LabeledPoint> trainList, List<LabeledPoint> testList, String modelPath) {

		logger.info("Starting train");

		logger.info("Number of labeled train points "+trainList.size());
		logger.info("Number of labeled test points "+testList.size());
				
		JavaRDD<LabeledPoint> training = sc.parallelize(trainList).cache();
		JavaRDD<LabeledPoint> test = sc.parallelize(testList).cache();

		logger.info("Size of training dataset"+training.count());
		
		logger.info("Run training algorithm to build the model");	
		int numIterations = 200;
				
		final SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);

		logger.info("Clear the default threshold");		
		model.clearThreshold();

		/*
		logger.info("Compute raw scores on the test set");
		try{
			
			JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test
					.map(new Function<LabeledPoint, Tuple2<Object, Object>>() {
	
						private static final long serialVersionUID = -2386041653117582576L;
	
						public Tuple2<Object, Object> call(LabeledPoint p) {
							Double score = model.predict(p.features());
							return new Tuple2<Object, Object>(score, p.label());
						}
					});
			logger.info("Getting metrics ");
			BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(
					JavaRDD.toRDD(scoreAndLabels));
			
			double auROC = metrics.areaUnderROC();
			logger.info("Area under ROC = " + auROC);		
		
		}catch(Exception e){
			logger.error("Error while calculating ROC",e);
		}
		*/
		
		logger.info("Saving the model");
		model.save(sc.sc(), modelPath);

		logger.info("Finishing train");
	}
	

	class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}

}
