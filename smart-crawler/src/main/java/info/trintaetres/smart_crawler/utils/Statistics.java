package info.trintaetres.smart_crawler.utils;


public class Statistics {
	
	private static int documentsCounter=0;		
	
	public static int getDocumentsCounter() {
		return documentsCounter;
	}


	public static void addDocumentsCounter() {
		documentsCounter++;
	}
}
