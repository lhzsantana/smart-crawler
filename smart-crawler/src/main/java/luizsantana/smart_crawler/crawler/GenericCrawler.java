package luizsantana.smart_crawler.crawler;

import java.util.List;

import luizsantana.smart_crawler.indexer.CrawlerIndexer;
import luizsantana.smart_crawler.namedentity.NERecognizer;
import luizsantana.smart_crawler.namedentity.Pair;
import luizsantana.smart_crawler.utils.Utils;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class GenericCrawler extends WebCrawler {

	private static NERecognizer recognizer = new NERecognizer();
	private static Utils utils = Utils.getInstance();
	private static CrawlerIndexer indexer = new CrawlerIndexer();

	public void visit(Page page) {

		if (page.getParseData() instanceof HtmlParseData) {
			
			String url = page.getWebURL().getURL();
			String domain = page.getWebURL().getDomain();

			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();

			if (utils.isFindNamedEntities()) {

				try {
					List<Pair> pairs = recognizer.findEnglishNames(text);
					
					for(Pair pair:pairs){
						indexer.indexNamedEntity(pair.getName(), pair.getCategory());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (utils.isSendToIndex()) {
				try {
					logger.info(text);
					indexer.indexRawPage(url, text, domain, html);
				} catch (Exception e) {
					logger.error("Page not indexed",e);
				}
			}
		}
		
		logger.debug("Finishing page visit {}", page.getWebURL());
	}
}
