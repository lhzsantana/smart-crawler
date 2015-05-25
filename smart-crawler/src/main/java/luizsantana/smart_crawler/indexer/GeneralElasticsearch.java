package luizsantana.smart_crawler.indexer;

import java.io.IOException;

import luizsantana.smart_crawler.utils.Utils;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class GeneralElasticsearch {

	private static final Logger logger = LoggerFactory
			.getLogger(BulkIndexer.class);

	private static CreateIndexRequestBuilder createIndexRequestBuilder;
	private static Utils utils = Utils.getInstance();
	private static Client client;

	public static synchronized Client getClient() throws Exception {

		if (client == null) {
			TransportClient transportClient;

			Settings settings = ImmutableSettings.settingsBuilder()
					.put("cluster.name", utils.getCluster()).build();
			transportClient = new TransportClient(settings);
			client = transportClient
					.addTransportAddress(new InetSocketTransportAddress(utils
							.getIndexAddress(), Integer.parseInt(utils
							.getIndexPort())));
		}

		return client;
	}

	public static void createIndexAndTypes() throws Exception {

		logger.info("Starting index creation");

		if (!GeneralElasticsearch.getClient().admin().indices()
				.prepareExists(utils.getIndexName()).get().isExists()) {

			createIndexRequestBuilder = GeneralElasticsearch.getClient()
					.admin().indices().prepareCreate(utils.getIndexName());

			try {
				// create names, documents, statistics, and classification
				createMappings();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createIndexRequestBuilder.execute().actionGet();

		}

		logger.info("Finishing index creation");
	}

	private static void createMappings() throws IOException {

		logger.info("Starting mappings creation");

		final XContentBuilder mappingRawPage = jsonBuilder().startObject()
				.startObject("raw-page").startObject("properties")
				.startObject("url").field("type", "string").endObject()
				.startObject("text").field("type", "string").endObject()
				.startObject("html").field("type", "string").endObject()
				.startObject("domain").field("type", "string").endObject()
				.endObject().endObject().endObject();

		final XContentBuilder mappingClassfiedPage = jsonBuilder()
				.startObject().startObject("classified-page")
				.startObject("properties").startObject("url")
				.field("type", "string").endObject().startObject("text")
				.field("type", "string").endObject().startObject("class")
				.field("type", "string").endObject().endObject().endObject()
				.endObject();

		logger.info(mappingRawPage.string());
		logger.info(mappingClassfiedPage.string());

		createIndexRequestBuilder.addMapping("raw-page", mappingRawPage);
		createIndexRequestBuilder.addMapping("classified-page",
				mappingClassfiedPage);

		logger.info("Finishing mappings creation");
	}

}
