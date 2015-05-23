package luizsantana.smart_crawler.crawler;


public interface Crawler {
	
	public void configCrawler(String[] seeds, String folder, int numberOfCrawlers);

	public void crawl() throws Exception;
	
}
