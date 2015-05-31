package info.trintaetres.smart_crawler.crawler.examples.papers.beans;

import org.elasticsearch.common.xcontent.XContentBuilder;

/*
 * This file represents the content parsed from the crawler
 * 
 */
public abstract class Content {

	private String url;
	private String text;
	private String name;
	
	//change to an encapsulate class
	private XContentBuilder meta;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public XContentBuilder getMeta() {
		return meta;
	}

	public void setMeta(XContentBuilder meta) {
		this.meta = meta;
	}
	

}
