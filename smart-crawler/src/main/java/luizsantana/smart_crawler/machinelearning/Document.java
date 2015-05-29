package luizsantana.smart_crawler.machinelearning;

import java.io.Serializable;

public class Document implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3930973785776531533L;
	private Long id;
	private String text;

	public Document(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
