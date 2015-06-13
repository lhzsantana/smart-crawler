package info.trintaetres.smart_crawler.words;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextPreparer {

	private static final Logger logger = LoggerFactory
			.getLogger(TextPreparer.class);

	public static String prepare(String text){
		
		//text.trim().replaceAll(" +", " ").replace("\n", "").replace("\r", "");
		try {
			text = removeStopWords(text);
		} catch (Exception e) {
			logger.error("Unable to remove stopwords", e);
		}
		
		return text;
	}
	public static String removeStopWords(String textFile) throws Exception {
		
	    CharArraySet stopWords = PortugueseAnalyzer.getDefaultStopSet();
	    
	    TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_48, new StringReader(textFile.trim()));

	    tokenStream = new StopFilter(Version.LUCENE_48, tokenStream, stopWords);	    
	    
	    StringBuilder sb = new StringBuilder();
	    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
	    tokenStream.reset();
	    
	    Set<String> tokens=new HashSet<String>();
	    
	    while (tokenStream.incrementToken()) {
	        String term = charTermAttribute.toString();
	        tokens.add(term.toLowerCase());
	    }
	    
	    for(String term:tokens){
	    	if(term.length()>5 && term.length()<15) sb.append(term + " ");
	    }
	    
	    return sb.toString();
	}
}
