package luizsantana.smart_crawler.indexer;

import java.util.ArrayList;
import java.util.List;

import luizsantana.smart_crawler.crawler.Crawler;
import luizsantana.smart_crawler.utils.Utils;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BulkIndexer {

	private static final Logger logger = LoggerFactory.getLogger(BulkIndexer.class);

	private static Utils utils = Utils.getInstance();
	private static BulkRequestBuilder bulkRequest;
	private static List<XContentBuilder> documents=new ArrayList<XContentBuilder>();

	protected void index(String type, XContentBuilder document, boolean last) throws ElasticsearchException, Exception{
		
		documents.add(document);
		
		if(Crawler.bulkSize%documents.size() == 0 || last){
			this.index(type);
		}
	}

	private void index(String type)
			throws ElasticsearchException, Exception {

		if (bulkRequest == null) {
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}
		
		for(XContentBuilder document:documents){
			bulkRequest.add(GeneralElasticsearch
					.getClient()
					.prepareIndex(utils.getIndexName(), type)
					.setSource(document));

			logger.info("Adding the document "+document.string());
		}
		
		bulkRequest.execute().actionGet();
		
		logger.info("Indexing "+documents+" of type "+type);
		
		documents=new ArrayList<XContentBuilder>();
		bulkRequest = null;
		System.gc();
	}
}
