package luizsantana.smart_crawler.indexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import luizsantana.smart_crawler.utils.Utils;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

public class Searcher {

	private static Utils utils = Utils.getInstance();

	public Set<SearchHit> getFromDomain(String domain, int size) throws ElasticsearchException, Exception {

		MatchQueryBuilder qb = QueryBuilders.matchQuery("domain", domain);
		
		return query(qb, size);		
	}
	
	private Set<SearchHit> query(QueryBuilder qb, int size) throws ElasticsearchException, Exception{
		SearchResponse scrollResp = GeneralElasticsearch.getClient()
				.prepareSearch(utils.getIndexName())
				.setSearchType(SearchType.SCAN).setScroll(new TimeValue(60000))
				.setQuery(qb).setSize(size).execute().actionGet();

		Set<SearchHit> response = new HashSet<SearchHit>();

		while (true) {
			response.addAll(Arrays.asList(scrollResp.getHits().getHits()));

			scrollResp = GeneralElasticsearch.getClient()
					.prepareSearchScroll(scrollResp.getScrollId())
					.setScroll(new TimeValue(600000)).execute().actionGet();
			
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}

		}
		return response;
	}
}
