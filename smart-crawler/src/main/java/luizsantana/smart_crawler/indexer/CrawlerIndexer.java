package luizsantana.smart_crawler.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import luizsantana.smart_crawler.Trainer;
import luizsantana.smart_crawler.utils.Utils;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlerIndexer {

	private static final Logger logger = LoggerFactory.getLogger(CrawlerIndexer.class);

	private static Utils utils = Utils.getInstance();
	private static BulkRequestBuilder bulkRequest;

	private static int total;

	public void write(String type, String url, String page)
			throws ElasticsearchException, Exception {

		if (bulkRequest == null) {
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}

		XContentBuilder doc = jsonBuilder().startObject();
		doc.field("page").value(page);
		doc.field("url").value(url);
		doc.endObject();

		bulkRequest.add(GeneralElasticsearch.getClient()
				.prepareIndex("crawler", type).setSource(doc));

		total++;

		if (total % utils.getIndexBulkSize() == 0) {
			bulkRequest.execute().actionGet();
			bulkRequest = null;
			System.out.println("Sent requests " + total);
			;
		}
	}

	public void sendToElasticsearch(String url, String page) {

		try {
			this.write("page", url, page);
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
