package luizsantana.smart_crawler.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.List;

import luizsantana.smart_crawler.namedentity.Pair;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NERIndexer {
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerIndexer.class);

	private static BulkRequestBuilder bulkRequest;

	private static int total;

	public void write(String type, Pair pair)
			throws ElasticsearchException, Exception {

		if (bulkRequest == null) {
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}

		XContentBuilder doc = jsonBuilder().startObject();
		doc.field("name").value(pair.getName());
		doc.field("category").value(pair.getCategory());
		doc.endObject();

		bulkRequest.add(GeneralElasticsearch.getClient()
				.prepareIndex("crawler", type).setSource(doc));

		total++;

		if (total % 10 == 0) {
			bulkRequest.execute().actionGet();
			bulkRequest = null;
			System.out.println("Sent requests " + total);
			;
		}
	}

	public void sendToElasticsearch(List<Pair> pairs) {

		try {
			for(Pair pair:pairs){	
				this.write("namedentities", pair);
			}
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
