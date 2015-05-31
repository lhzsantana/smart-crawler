package info.trintaetres.smart_crawler.crawler;

import info.trintaetres.smart_crawler.crawler.examples.papers.CiteSeerXExtractor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

public abstract class Extractor {

	protected static Logger log = Logger.getLogger(Extractor.class);

	protected HttpURLConnection c;

	private List<OpenProxy> proxies = new ArrayList<OpenProxy>();

	private static int proxyNum;
	private static OpenProxy currentProxy;

	private class OpenProxy {

		private String proxyAdd;
		private int proxyPort;

		public OpenProxy(String add, int port) {
			this.proxyAdd = add;
			this.proxyPort = port;
		}

		public String getProxyAdd() {
			return proxyAdd;
		}

		public int getProxyPort() {
			return proxyPort;
		}
	}

	public static void main(String[] args) throws IOException {

		Extractor extractor = new CiteSeerXExtractor();

	}

	public void getProxies() throws IOException {

		Document doc = Jsoup.connect("http://proxylist.hidemyass.com/").get();

		for (Element table : doc.select("table[id=listable]")) {
			for (Element row : table.select("tr")) {
				Elements tds = row.select("td");

				// System.out.println(row.text());
				if (tds.size() > 2) {
					
					
					URL url = new URL("http://www.example.com");
					StringWebResponse response = new StringWebResponse(tds.get(1).html(), url);
					WebClient client = new WebClient();
					HtmlPage page = HTMLParser.parseHtml(response, client.getCurrentWindow());

					String ip=page.asText().replace("\n", "").replace("\r", "");
					int port=Integer.valueOf(tds.get(2).html());
					System.out.println(ip);
					//final HtmlTable tb = page.getHtmlElementById("listable");
					//System.out.println("Cell (1,2)=" + tb.getCellAt(1,2));

					System.out.println(port);
					System.out.println("---");
					
					proxies.add(new OpenProxy(ip,port));
				}
			}
		}

	}

	public Extractor() throws IOException {

		this.getProxies();
	}

	public void getNextProxy() {

		log.info("Getting another proxy");

		if (proxyNum == proxies.size()) {
			currentProxy = null;
		} else {
			currentProxy = proxies.get(proxyNum);
			proxyNum++;
		}
	}

	protected void setConnection(String baseURL) throws IOException {

		URL url = new URL(baseURL);

		if (currentProxy != null) {
			log.info("Current proxy " + currentProxy.getProxyAdd());
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					currentProxy.getProxyAdd(), currentProxy.getProxyPort()));
			c = (HttpURLConnection) url.openConnection(proxy);
		} else {
			log.info("No proxy");
			c = (HttpURLConnection) url.openConnection();
		}

		c.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
		c.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
		c.setRequestProperty("Accept-Encoding", "none");
		c.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		c.setRequestProperty("Connection", "keep-alive");

		c.connect();
	}

}
