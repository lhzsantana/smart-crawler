package info.trintaetres.smart_crawler.utils;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
	public Set<String> getIndexBlockedDomains() {
		return indexBlockedDomains;
	}

	public void setIndexBlockedDomains(Set<String> indexBlockedDomains) {
		this.indexBlockedDomains = indexBlockedDomains;
	}

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	private static Utils instance = null;
	
	public static Utils getInstance(){
		
		if(instance==null){
			instance=new Utils();
			instance.loadConfig();
		}
		
		return instance;
	}

	private String twitterConsumerKey;
	private String twitterConsumerSecret;
	private String twitterAccessToken;
	private String twitterAccessTokenSecret;
	
	private String cluster;

	private int indexBulkSize;
	private String indexAddress;	
	private String indexPort;	
	private String indexName;
	
	private String counterAddress;
	private String counterPort;
	private String counterKeySpace;
	
	private String trainingData;
	private String crawledData;

	private boolean sendToIndex;
	
	private boolean findNamedEntities;
	
	private String path;
	
	private int numberOfCrawlers;
	private int numIterations;
	
	private Set<String> indexBlockedDomains;
	private String [] seeds;
	private String [] classes;
	
	private void loadConfig() {

		logger.debug("Reading properties");
		
		String [] classes={"negócios", "esportes", "carros"};
		String [] localSeeds={"http://quatrorodas.abril.com.br/","www.infomoney.com.br","http://globoesporte.com/"};
		//String [] localSeeds={"http://quatrorodas.abril.com.br/","http://www.cacb.org.br","http://pt.wikipedia.org/wiki/Lista_de_jornais_do_Brasil", "http://www.infomoney.com.br", "http://www.globo.com", "http://www.exame.com.br", "http://www.valoreconomico.com.br"};
		//String [] localSeeds={"http://pt.wikipedia.org/wiki/Lista_de_jornais_do_Brasil"};
		
		this.indexBlockedDomains=new HashSet<String>();
		indexBlockedDomains.add("wikipedia.org");
		indexBlockedDomains.add("wikidata.org");
		indexBlockedDomains.add("mediawiki.org");
		indexBlockedDomains.add("wikimedia.org");
		
		this.setClasses(classes);
		this.seeds=localSeeds;
		this.path="~";

		this.indexAddress="127.0.0.1";
		this.indexPort="9300";
		this.indexName="crawler1";
		this.cluster="elasticsearch";
		
		this.sendToIndex=true;
		this.findNamedEntities=false;
		
		this.indexBulkSize=1;
		this.numberOfCrawlers=10;
		
		this.trainingData="trainingData.txt";
		this.crawledData="crawledData.txt";
		
		this.twitterConsumerKey = "peGXD4523452fsMw345ssfe46xYMjz3Ui";
		this.twitterConsumerSecret = "weyq8COCVrMBasdfspJZm6e324zta40cPkwQQle1X6";
		this.twitterAccessToken = "14642944-234sfsfasdfasdfarwer13";
		this.twitterAccessTokenSecret = "5wrw2wN4342kperwXKEnIMdsfsZpNHp8mfSz";
	}

	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String [] getSeeds() {
		return seeds;
	}

	public void setSeeds(String [] seeds) {
		this.seeds = seeds;
	}

	public boolean isSendToIndex() {
		return sendToIndex;
	}

	public void setSendToIndex(boolean sendToIndex) {
		this.sendToIndex = sendToIndex;
	}

	public boolean isFindNamedEntities() {
		return findNamedEntities;
	}

	public void setFindNamedEntities(boolean findNamedEntities) {
		this.findNamedEntities = findNamedEntities;
	}

	public String getIndexAddress() {
		return indexAddress;
	}

	public void setIndexAddress(String indexAddress) {
		this.indexAddress = indexAddress;
	}

	public String getIndexPort() {
		return indexPort;
	}

	public void setIndexPort(String indexPort) {
		this.indexPort = indexPort;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public int getIndexBulkSize() {
		return indexBulkSize;
	}

	public void setIndexBulkSize(int indexBulkSize) {
		this.indexBulkSize = indexBulkSize;
	}

	public String [] getClasses() {
		return classes;
	}

	public void setClasses(String [] classes) {
		this.classes = classes;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public String getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(String trainingData) {
		this.trainingData = trainingData;
	}

	public String getCrawledData() {
		return crawledData;
	}

	public void setCrawledData(String crawledData) {
		this.crawledData = crawledData;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getCounterAddress() {
		return counterAddress;
	}

	public void setCounterAddress(String counterAddress) {
		this.counterAddress = counterAddress;
	}

	public String getCounterPort() {
		return counterPort;
	}

	public void setCounterPort(String counterPort) {
		this.counterPort = counterPort;
	}

	public String getCounterKeySpace() {
		return counterKeySpace;
	}

	public void setCounterKeySpace(String counterKeySpace) {
		this.counterKeySpace = counterKeySpace;
	}

	public String getTwitterConsumerKey() {
		return twitterConsumerKey;
	}

	public void setTwitterConsumerKey(String twitterConsumerKey) {
		this.twitterConsumerKey = twitterConsumerKey;
	}

	public String getTwitterConsumerSecret() {
		return twitterConsumerSecret;
	}

	public void setTwitterConsumerSecret(String twitterConsumerSecret) {
		this.twitterConsumerSecret = twitterConsumerSecret;
	}

	public String getTwitterAccessToken() {
		return twitterAccessToken;
	}

	public void setTwitterAccessToken(String twitterAccessToken) {
		this.twitterAccessToken = twitterAccessToken;
	}

	public String getTwitterAccessTokenSecret() {
		return twitterAccessTokenSecret;
	}

	public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
		this.twitterAccessTokenSecret = twitterAccessTokenSecret;
	}


}
