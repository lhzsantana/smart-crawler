package luizsantana.smart_crawler.words;

public class TextPreparer {

	public static String prepare(String text){
		
		text.trim().replaceAll(" +", " ").replace("\n", "").replace("\r", "");
		
		return text;
	}
}
