package luizsantana.smart_crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Classifier {

	private static final Logger logger = LoggerFactory.getLogger(Trainer.class);
	
    public static void main( String[] args )
    {
    	logger.info("Starting classifier smart-crawler");
    	
    	Trainer trainer = new Trainer();
    	
    	trainer.train();
    	    	
    	logger.info("Finishing classifier smart-crawler"); 
    }
}
