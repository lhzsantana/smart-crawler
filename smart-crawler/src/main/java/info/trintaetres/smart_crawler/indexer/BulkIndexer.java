package info.trintaetres.smart_crawler.indexer;

import info.trintaetres.smart_crawler.utils.Utils;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BulkIndexer {

	private static final Logger logger = LoggerFactory
			.getLogger(BulkIndexer.class);

	private static Utils utils = Utils.getInstance();
	private static BulkRequestBuilder bulkRequest;

	protected void index(String type, XContentBuilder document, String id, boolean last)
			throws ElasticsearchException, Exception {

		if (bulkRequest == null) {
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}

		bulkRequest.add(GeneralElasticsearch.getClient()
				.prepareIndex(utils.getIndexName(), type)
				.setId(id)
				.setSource(document));
		
		logger.info("Adding the document "+bulkRequest.numberOfActions());

		if (bulkRequest.numberOfActions() %  utils.getIndexBulkSize()  == 0 || last) {

			logger.debug("Indexing " + bulkRequest.numberOfActions() + " documents ");
			
			bulkRequest.execute().get();

			bulkRequest = null;
			System.gc();
		}
	}
}
