package luizsantana.smart_crawler.machinelearning;

import java.io.Serializable;

public class LabeledDocument extends Document implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1762887113285213477L;
	private Double label;

	public LabeledDocument(Long id, String text, Double label) {
		super(id, text);
		this.label = label;
	}

	public Double getLabel() {
		return this.label;
	}

	public void setLabel(Double label) {
		this.label = label;
	}
}