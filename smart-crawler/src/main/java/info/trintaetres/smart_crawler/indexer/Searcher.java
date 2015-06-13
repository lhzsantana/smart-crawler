package info.trintaetres.smart_crawler.indexer;

import info.trintaetres.smart_crawler.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
		
	
	public Set<SearchHit> getAll() throws ElasticsearchException, Exception{

		int tries=0;
		
		do{
			try{
				
				String [] pageTypes={"Imóveis","Carreira","Minhas finanças","Onde investir"};

				Set<SearchHit> allHits = new HashSet<SearchHit>(); 
				
				for(String value:pageTypes){
					Set<SearchHit> partialHits=getFromField("originalCategory", value, 0, 2000);
					
					allHits.addAll(partialHits);
				}				
				
				return allHits;

			}catch(Exception e){
				logger.error("Error while connecting to Elasticsearch",e);
			}
			
			tries++;
		}while(tries<10000);
	
		return null;
	}
	
	
	public List<String> getFromField(String field, String value) throws ElasticsearchException, Exception{
		
		int tries=0;
		
		do{
			try{
						
				Set<SearchHit> hits=getFromField(field, value, 0, 2000);
				
				List<String> texts= new ArrayList<String>();
				
				for(SearchHit hit:hits){
				
					texts.add(hit.getSource().get("puretext").toString());
				}
				
				return texts;
				
			}catch(Exception e){
				logger.error("Error while connecting to Elasticsearch",e);
			}
			
			tries++;
		}while(tries<10000);
		
		return null;
	}

	public Set<SearchHit> getFromField(String field, String value, int from, int size) throws ElasticsearchException, Exception {

		logger.info("Getting data for the field "+field+" with value "+value);
		
		MatchQueryBuilder qb = QueryBuilders.matchQuery(field, value);
		
		return query(qb, from, size);		
	}

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
