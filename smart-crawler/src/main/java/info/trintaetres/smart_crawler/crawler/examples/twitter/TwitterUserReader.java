package info.trintaetres.smart_crawler.crawler.examples.twitter;

import info.trintaetres.smart_crawler.utils.Utils;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUserReader {

	private static Utils utils = Utils.getInstance();
	
	public void getTweets(String username) throws TwitterException{
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(utils.getTwitterConsumerKey())
				.setOAuthConsumerSecret(utils.getTwitterConsumerSecret())
				.setOAuthAccessToken(utils.getTwitterAccessToken())
				.setOAuthAccessTokenSecret(utils.getTwitterAccessTokenSecret());
		
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		//First param of Paging() is the page number, second is the number per page (this is capped around 200 I think.
		Paging paging = new Paging(1, 100);
		
		List<Status> statuses = twitter.getUserTimeline(username,paging);
		
		for(Status status: statuses){
			System.out.println(status.getText());
		}		
	}

}