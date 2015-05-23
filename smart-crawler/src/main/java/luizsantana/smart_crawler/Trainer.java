package luizsantana.smart_crawler;

import luizsantana.smart_crawler.crawler.Crawler;
import luizsantana.smart_crawler.crawler.CrawlerImpl;
import luizsantana.smart_crawler.machinelearning.DataSetTrainer;
import luizsantana.smart_crawler.machinelearning.DataSetTrainerImpl;
import luizsantana.smart_crawler.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Trainer {
	
	private static final Logger logger = LoggerFactory.getLogger(Trainer.class);
		
	public void train(){
		
		logger.info("Starting trainer smart-crawler");
		
    	Utils utils = Utils.getInstance();
		
		try {
			Crawler crawler = new CrawlerImpl();

			crawler.configCrawler(utils.getSeeds(),utils.getPath(),utils.getNumberOfCrawlers());
			
			crawler.crawl();
			
		} catch (Exception e) {
			logger.error("Error on crawling", e);
		}

		DataSetTrainer dataSetTrainer = new DataSetTrainerImpl();

		dataSetTrainer.train(utils.getPath(), utils.getClasses(), utils.getNumIterations());

		logger.info("Finishing trainer smart-crawler");
	}
}
