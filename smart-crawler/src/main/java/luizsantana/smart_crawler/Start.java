package luizsantana.smart_crawler;

import luizsantana.smart_crawler.crawler.Crawler;
import luizsantana.smart_crawler.crawler.CrawlerImpl;
import luizsantana.smart_crawler.indexer.GeneralElasticsearch;
import luizsantana.smart_crawler.machinelearning.DataSetTrainer;
import luizsantana.smart_crawler.machinelearning.DataSetTrainerImpl;
import luizsantana.smart_crawler.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Start {
	
	private static final Logger logger = LoggerFactory.getLogger(Start.class);
	private static Utils utils = null;
	
	private static void setup() throws Exception{

    	utils = Utils.getInstance();
    	
		GeneralElasticsearch.createIndexAndTypes();
		
	}
	
	public static void main(String [] args) throws Exception{

		logger.info("Starting smart-crawler");
		
		setup();
				
		crawl();
		
		logger.info("Starting smart-crawler");
	}
	
	private static void train(){
		
		logger.info("Starting crawling for training");
		
		crawl();
		
		/*DataSetTrainer dataSetTrainer = new DataSetTrainerImpl();

		dataSetTrainer.train(utils.getPath(), utils.getClasses(), utils.getNumIterations());

		logger.info("Finishing crawling for training");*/
	}
	
	private static void crawl(){

		try {
			Crawler crawler = new CrawlerImpl();

			crawler.configCrawler(utils.getSeeds(),utils.getPath(),utils.getNumberOfCrawlers());
			
			crawler.crawl();
			
		} catch (Exception e) {
			logger.error("Error on crawling", e);
		}
	}
}
