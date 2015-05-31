package info.trintaetres.smart_crawler.crawler.examples.papers;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import info.trintaetres.smart_crawler.crawler.Extractor;
import info.trintaetres.smart_crawler.crawler.examples.papers.beans.Paper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CiteSeerXExtractor extends Extractor{

	public CiteSeerXExtractor() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Paper> getPapers(String author) throws Exception {

		List<Paper> papers = new ArrayList<Paper>();

		String baseURL = "http://citeseerx.ist.psu.edu/search?q=author"
				+ URLEncoder.encode(":" + author);
		String paginationURL = "";

		int page = 0;
		boolean finished = false;

		do {

			log.info(baseURL + paginationURL);

			this.setConnection(baseURL + paginationURL);
			
			Document doc = Jsoup.parse(c.getInputStream(), "UTF-8", baseURL);

			if (doc.text().toLowerCase().contains("query limit exceeded")) {
				
				log.info("Query limit exception");
				
			} else {

				Elements elements = doc.select("div.result");
				// div with class=gs_r
				for (Element element : elements) {

					Paper paper = new Paper();
					paper.setTitle(element.select("h3").text());
					paper.setSumary(element.select("div.snippet").text());
					paper.setAuthor(author);
					paper.setUrl(baseURL);
					

					XContentBuilder meta = jsonBuilder().startObject();
					
					meta.field("co-authors").value("");
					meta.field("tags").value("");
					
					paper.setMeta(meta);

					papers.add(paper);

					//log.info(paper.getTitle());
					//log.info(paper.getSumary());
				}

				if (elements.size() == 0)
					finished = true;

				page = 10 + page;
				paginationURL = "&t=doc&sort=rlv&start=" + page;

			}

		} while (!finished);

		return papers;
	}
}
