package info.trintaetres.smart_crawler.crawler.examples.papers.beans;


public class Paper extends Content{

	private String author;
	private String sumary;
	private String title;
	
	public final String getAuthor() {
		return author;
	}
	public final void setAuthor(String author) {
		this.author = author;
	}
	public final String getSumary() {
		return sumary;
	}
	public final void setSumary(String sumary) {
		this.sumary = sumary;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
