package info.trintaetres.smart_crawler.machinelearning;

public interface DataSetTrainer {

	public void train(String path, String [] classes, int numIterations);
}
