package assignment4;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 *
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     *
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweets> writtenBy(List<Tweets> tweets, String username) {
        List<Tweets> filteredList = new ArrayList<Tweets>();
        
        for(Tweets tweet : tweets) {
        	if(tweet.getName().toLowerCase().equals(username.toLowerCase())) {
        		filteredList.add(tweet);
        	}
        }
        
        return filteredList;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     *
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweets> inTimespan(List<Tweets> tweets, Timespan timespan) throws InvalidTimespanException{
        List<Tweets> filteredList = new ArrayList<Tweets>();
        Instant startInstant = timespan.getStart();
    	Instant endInstant = timespan.getEnd();
        Instant tweetInstant;
        
        if(startInstant.compareTo(endInstant) > 0) { // start time is greater than end time
        	throw new InvalidTimespanException("End Date before Start Date");
        }
        
        for(Tweets tweet: tweets) {
        	try {
        		tweetInstant = Instant.parse(tweet.getDate());
        		int startValue = tweetInstant.compareTo(startInstant);
        		int endValue = tweetInstant.compareTo(endInstant);
        	
        		if(startValue >= 0 && endValue <= 0) { // if tweet instant falls within timespan
        			filteredList.add(tweet);
        		}
        	}
        	catch(DateTimeParseException e) { 
        		// don't add it to filtered list
        	}
        }
        
        
        return filteredList;
    }

    /**
     * Find tweets that contain certain words.
     *
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets.
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when
     *         represented as a sequence of nonempty words bounded by space characters
     *         and the ends of the string) includes *at least one* of the words
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweets> containing(List<Tweets> tweets, List<String> words) {
        List<Tweets> filteredList = new ArrayList<Tweets>();
        
        for(int i = 0; i < tweets.size(); i++) {
        	String tweetText = tweets.get(i).getText().toLowerCase(); // get tweet text and make it lowercase
        	for(String word : words) {
        		if(tweetText.contains(word.toLowerCase())) { // if tweet contains any of the words, add that tweet to the filterst list     
        			if(filteredList.contains(tweets.get(i)) != true) {
        				filteredList.add(tweets.get(i));
        			}
        		}
        	}
        }
        
        return filteredList;
    }
}



