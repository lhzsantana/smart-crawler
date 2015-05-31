package info.trintaetres.smart_crawler.crawler.examples.papers.beans;

import java.util.Date;

/*
 * This file represents the content crawler by Nutch
 * 
 */
public class RawContent {

	private String title;
	private String content;
	private String URL;
	private Date contentDate;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public Date getContentDate() {
		return contentDate;
	}
	public void setContentDate(Date contentDate) {
		this.contentDate = contentDate;
	}
}
