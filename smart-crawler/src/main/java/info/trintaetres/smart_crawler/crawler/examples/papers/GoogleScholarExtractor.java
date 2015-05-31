package info.trintaetres.smart_crawler.crawler.examples.papers;


import info.trintaetres.smart_crawler.crawler.Extractor;
import info.trintaetres.smart_crawler.crawler.examples.papers.beans.Paper;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleScholarExtractor extends Extractor {

	public GoogleScholarExtractor() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static Logger log = Logger.getLogger(GoogleScholarExtractor.class);

	public List<Paper> getPapers(String author) throws IOException {

		String baseURL = "https://scholar.google.com.br/scholar?q="
				+ URLEncoder.encode(author) + "";
		log.info(baseURL);
		// https://scholar.google.com.br/scholar?q=autor%3Aerick+autor%3Aprado&btnG=&hl=pt-BR&as_sdt=0%2C5
		
		File input = new File("/tmp/input.html");
		// Document doc = Jsoup.parse(input, "UTF-8",
		// "http://example.com/");
		
		this.setConnection(baseURL);
		Document doc = Jsoup.parse(c.getInputStream(), "UTF-8", baseURL);

		Elements elements = doc.select("div.gs_r");
		// div with class=gs_r
		List<Paper> papers = new ArrayList<Paper>();
		for (Element element : elements) {

			Paper paper = new Paper();
			paper.setTitle(element.select("h3.gs_rt").text());
			paper.setSumary(element.select("div.gs_rs").text());
			paper.setAuthor(author);
			paper.setUrl(baseURL);

			papers.add(paper);

			log.info(paper.getTitle());
			log.info(paper.getSumary());
		}

		return papers;
	}
}
