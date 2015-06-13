package info.trintaetres.smart_crawler.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import info.trintaetres.smart_crawler.crawler.Crawler;
import info.trintaetres.smart_crawler.machinelearning.LoadModels;
import info.trintaetres.smart_crawler.machinelearning.svm.InfomoneyTest;
import info.trintaetres.smart_crawler.machinelearning.svm.Model;
import info.trintaetres.smart_crawler.machinelearning.svm.SVMImpl;
import info.trintaetres.smart_crawler.parsers.Infomoney;
import info.trintaetres.smart_crawler.utils.Utils;
import info.trintaetres.smart_crawler.words.TextPreparer;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Vector;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlerIndexer extends BulkIndexer {


	private static final Logger logger = LoggerFactory
			.getLogger(CrawlerIndexer.class);
	
	private static Utils utils = Utils.getInstance();
	private static LoadModels loader = new LoadModels();
	//private static SVMImpl svm = new SVMImpl();
	//private static HashingTF tf = new HashingTF(50);
	private static Infomoney parser = new Infomoney();
		
	/*
	private Vector getVectorPage(String html) throws IOException {

		String text = TextPreparer.prepare(html);

		return tf.transform(Arrays.asList(text.split(" ")));
	}
	
	private String classify(String html){

		List<Model>models=loader.loadModels();
		String category="";
		
		try {
			category = svm.predictOneVsAll(models, getVectorPage(html));
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return category;
	}*/

	private String getBiggestImg(Document doc) {

		Elements images = doc.select("img");
				
		long max=0;
		String srcImg="";
		
		for (Element element : images) {

			try{
				final String src = element.absUrl("src");
	
				final URL url = new URL(src);
				final long size = url.openConnection().getContentLength();
				
				if(size>max){
					max=size;
					srcImg=src;
				}
			}catch(Exception e){
				
			}
		}

		return srcImg;
	}

	public void indexRawPage(String url, String text, String domain, String html)
			throws ElasticsearchException, Exception {

		if (utils.getIndexBlockedDomains().contains(domain)) return;

		if(domain.contains("infomoney")){
			Document doc = Jsoup.parse(html);
			parser.load(html);
			
			String category = parser.getCategory();
			String pureText = parser.getPuretext();
			
			logger.info(category +"-"+pureText);
			
			if(!category.equals("") && !pureText.equals("")){
	
				XContentBuilder namedEntity = jsonBuilder().startObject()
						.field("title", doc.title())
						.field("img", getBiggestImg(doc))
						.field("url", url)
						.field("text", text)
						.field("puretext", pureText)
						.field("originalCategory", category)
						.field("domain", domain)
						//.field("category", classify(html))
						.field("html", html).endObject();
		
				this.index(Crawler.indexTypeRawPage, namedEntity, url, false);
			}			
		}
	}

	public void indexClassifiedPage(String url, String originalCategory, String category, String img, String title, String text, String domain)
			throws ElasticsearchException, Exception {

		XContentBuilder namedEntity = jsonBuilder().startObject()
			.field("title", title)
			.field("img", img)
			.field("text", text)
			.field("originalCategory", originalCategory)
			.field("domain", domain)
			.field("category", category)
			.endObject();
		
		this.index(Crawler.indexTypeClassifiedPage, namedEntity, url, false);
	}


	public void indexNamedEntity(String name, String type)
			throws ElasticsearchException, Exception {

		XContentBuilder namedEntity = jsonBuilder().startObject()
				.field("name", name).field("type", type).endObject();

		// this.index(Crawler.indexTypeNamedEntity, namedEntity, false);
	}

}
