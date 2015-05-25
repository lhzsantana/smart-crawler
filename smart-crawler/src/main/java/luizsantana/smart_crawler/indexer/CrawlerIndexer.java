package luizsantana.smart_crawler.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import luizsantana.smart_crawler.crawler.Crawler;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.xcontent.XContentBuilder;


public class CrawlerIndexer extends BulkIndexer{
	
	public void indexRawPage(String url, String text, String domain, String html) throws ElasticsearchException, Exception{

		XContentBuilder namedEntity = jsonBuilder()
        		.startObject()
        		.field("url", url)
        		.field("text", text)
        		.field("domain", domain)
        		.field("html", html)
        		.endObject();
		
		this.index(Crawler.indexTypeRawPage, namedEntity, false);
	}

	public void indexClassifiedPage(String url, String page, String domain, String html, String classification) throws ElasticsearchException, Exception{

		XContentBuilder namedEntity = jsonBuilder()
        		.startObject()
        		.startObject();
		
		this.index(Crawler.indexTypeRawPage, namedEntity, false);
	}
	
	public void indexNamedEntity(String name, String type) throws ElasticsearchException, Exception{
		
		XContentBuilder namedEntity = jsonBuilder()
        		.startObject()
        		.startObject();
		
		this.index(Crawler.indexTypeRawPage, namedEntity, false);
	}

}
