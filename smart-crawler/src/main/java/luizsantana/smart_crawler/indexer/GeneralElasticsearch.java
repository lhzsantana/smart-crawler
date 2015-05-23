package luizsantana.smart_crawler.indexer;

import luizsantana.smart_crawler.utils.Utils;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralElasticsearch {
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerIndexer.class);
	
	private static Utils utils = Utils.getInstance();	
	private static Client client;
	private static TransportClient transportClient;

	public static synchronized Client getClient() throws Exception {

		if (client == null) {
			Settings settings = ImmutableSettings.settingsBuilder()
					.put("cluster.name", utils.getCluster()).build();
			transportClient = new TransportClient(settings);
			client = transportClient
					.addTransportAddress(new InetSocketTransportAddress(utils.getIndexAddress(),
							Integer.parseInt(utils.getIndexPort())));
		}

		return client;
	}
}
