package info.trintaetres.smart_crawler.utils;

import java.util.HashMap;
import java.util.Map;

public class TrainingData {
	
	private static TrainingData instance=null;
	
	public static Map<String, String> pages = new HashMap<String, String>();
	
	public static TrainingData getInstance(){
		
		if(instance==null){
			instance=new TrainingData();
		}
		
		return instance;
	}

}
