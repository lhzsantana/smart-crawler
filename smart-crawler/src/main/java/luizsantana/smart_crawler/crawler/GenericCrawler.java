package luizsantana.smart_crawler.crawler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import luizsantana.smart_crawler.indexer.CrawlerIndexer;
import luizsantana.smart_crawler.indexer.NERIndexer;
import luizsantana.smart_crawler.namedentity.NERecognizer;
import luizsantana.smart_crawler.namedentity.Pair;
import luizsantana.smart_crawler.utils.Statistics;
import luizsantana.smart_crawler.utils.Utils;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class GenericCrawler extends WebCrawler {

	private static CrawlerIndexer crawlerIndexer = new CrawlerIndexer();
	private static NERIndexer nerIndexer = new NERIndexer();
	private static NERecognizer recognizer = new NERecognizer();
	private static Utils utils = Utils.getInstance();

	public void visit(Page page) {

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("the-file-name.txt", "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		logger.debug("Starting page visit {}", page.getWebURL());

		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();

		logger.debug("Docid: {}", docid);
		logger.debug("URL: {}", url);
		logger.debug("Domain: '{}'", domain);
		logger.debug("Sub-domain: '{}'", subDomain);
		logger.debug("Path: '{}'", path);
		logger.debug("Parent page: {}", parentUrl);
		logger.debug("Anchor text: {}", anchor);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();

			if (utils.isFindNamedEntities()) {

				try {
					List<Pair> names = recognizer.findEnglishNames(text);

					nerIndexer.sendToElasticsearch(names);
					logger.debug(names.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			logger.debug(text);
			writer.println(text);

			if (utils.isSendToIndex()) {
				Statistics.addDocumentsCounter();
				crawlerIndexer.sendToElasticsearch(url, text);
			}

			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			logger.debug("Text length: {}", text.length());
			logger.debug("Html length: {}", html.length());
			logger.debug("Number of outgoing links: {}", links.size());
		}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			logger.debug("Response headers:");
			for (Header header : responseHeaders) {
				logger.debug("\t{}: {}", header.getName(), header.getValue());
			}
		}
		logger.debug("Finishing page visit {}", page.getWebURL());
		

		writer.close();
	}
}
