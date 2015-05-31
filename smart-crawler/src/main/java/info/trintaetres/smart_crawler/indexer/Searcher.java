package info.trintaetres.smart_crawler.indexer;

import info.trintaetres.smart_crawler.machinelearning.naivebayes.NaiveBayesImpl;
import info.trintaetres.smart_crawler.utils.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Searcher {

	private static final Logger logger = LoggerFactory.getLogger(Searcher.class);

	private static Utils utils = Utils.getInstance();

	public Set<SearchHit> getFromDomain(String domain, int from, int size) throws ElasticsearchException, Exception {

		logger.info("Getting data for the domain "+domain);
		
		MatchQueryBuilder qb = QueryBuilders.matchQuery("domain", domain);
		
		return query(qb, from, size);		
	}
	
	public Set<SearchHit> getSample(int from, int size) throws ElasticsearchException, Exception{

		logger.info("Getting data sample");
		
		MatchAllQueryBuilder qb = QueryBuilders.matchAllQuery();
		
		return query(qb, from, size);		
	}
	
	private Set<SearchHit> query(QueryBuilder qb, int from, int size) throws ElasticsearchException, Exception{
		SearchResponse scrollResp = GeneralElasticsearch.getClient()
				.prepareSearch(utils.getIndexName())
				.setSearchType(SearchType.SCAN).setScroll(new TimeValue(60000))
				.setQuery(qb).setFrom(from).setSize(size).execute().actionGet();

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
	
		logger.info("There are "+response.size()+" responses");
		
		return response;
	}
}
