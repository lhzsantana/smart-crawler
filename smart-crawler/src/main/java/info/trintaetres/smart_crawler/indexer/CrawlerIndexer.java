package info.trintaetres.smart_crawler.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import info.trintaetres.smart_crawler.crawler.Crawler;
import info.trintaetres.smart_crawler.utils.Utils;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.xcontent.XContentBuilder;


public class CrawlerIndexer extends BulkIndexer{

	private static Utils utils = Utils.getInstance();
	
	public void indexRawPage(String url, String text, String domain, String html) throws ElasticsearchException, Exception{
 
		if(utils.getIndexBlockedDomains().contains(domain)) return;
		
		XContentBuilder namedEntity = jsonBuilder()
        		.startObject()
        		.field("url", url)
        		.field("text", text)
        		.field("domain", domain)
        		.field("html", html)
        		.endObject();
		
		this.index(Crawler.indexTypeRawPage, namedEntity, url, false);
	}
	public void indexClassifiedPage(String url, String page, String domain, String html, String classification) throws ElasticsearchException, Exception{

		if(utils.getIndexBlockedDomains().contains(domain)) return;
		
		XContentBuilder namedEntity = jsonBuilder()
        		.startObject()
        		.startObject();
		
		//this.index(Crawler.indexTypeRawPage, namedEntity, url, false);
	}
	
	public void indexNamedEntity(String name, String type) throws ElasticsearchException, Exception{
		
		XContentBuilder namedEntity = jsonBuilder()
        		.startObject()
        		.field("name", name)
        		.field("type", type)
        		.endObject();
		
		
		//this.index(Crawler.indexTypeNamedEntity, namedEntity, false);
	}

}
