package luizsantana.smart_crawler.crawler;

public interface Crawler {
	
	public final static String indexTypeRawPage="raw-page";
	public final static String indexTypeClassifiedPage="classified-page";
	public final static String indexTypeNamedEntity="named-entity";
	public final static int bulkSize=1;
	
	public void configCrawler(String[] seeds, String folder, int numberOfCrawlers);

	public void crawl() throws Exception;
	
}
