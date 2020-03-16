package assignment4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Social Network consists of methods that filter users matching a
 * condition.
 *
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {
    /**
     * Get K most followed Users.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @param k
     *            integer of most popular followers to return
     * @return the set of usernames who are most mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getName()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like ethomaz@utexas.edu does NOT
     *         contain a mention of the username.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static List<String> findKMostFollower(List<Tweets> tweets, int k) {
        List<String> mostFollowers = new ArrayList<>();
        List<String> users = new ArrayList<>();
        List<Integer> mentions = new ArrayList<>();
        List<List<String>> followers = new ArrayList<>(); // list of followers for users
        
        // add all users and initialize their mentions
        for(Tweets tweet : tweets) {
        	if(users.contains(tweet.getName().toLowerCase()) == false) {
        		users.add(tweet.getName().toLowerCase());
        		followers.add(new ArrayList<>());
        		mentions.add(0);
        	}
        }
        
        for(Tweets tweet: tweets) {
        	String[] tweetArray = tweet.getText().split(" ");
        	char firstLetter; // to check if first letter is @
        	String username; // save username
        	
        	for(String tweetWord : tweetArray) {
        		if(tweetWord.length() > 0) { // need to have at least one character after @ for it to be a valid username
        			firstLetter = tweetWord.charAt(0);
            		if(firstLetter == '@') {
            			username = tweetWord.substring(1, tweetWord.length()).toLowerCase();
            			
            			// add username to users list if doesn't already exist
            			if(checkIfValidUsername(username) == true && users.contains(username) == false) {
            				users.add(username);
            				followers.add(new ArrayList<>());
                    		mentions.add(0);
            			}
            			
            			if(checkIfValidUsername(username)) {
            				addFollower(tweet.getName().toLowerCase(), username, users, followers);
            			}
            		}
        		}
        	}
        }
        
        fillMostFollowers(followers, mostFollowers, users);
        
        if(mostFollowers.size() < k) {
        	return mostFollowers;
        }
        else 
        	return mostFollowers.subList(0, k);
        
    }
    
    // add follower to username, follower and username both have to be lower case
    public static void addFollower(String follower, String username, List<String> users, List<List<String>> followers) {
    	int index = users.indexOf(username);
    	
    	if(followers.get(index).contains(follower) == false) {
    		followers.get(index).add(follower);
    	}
    }
    
    // fills in the most followers for mostFollowers
    public static void fillMostFollowers(List<List<String>> followers, List<String> mostFollowers, List<String> users) {
    	int maxIndex;
    	List<Integer> numOfMentions = new ArrayList<>();
    	
    	for(int i = 0; i < followers.size(); i++) {
    		numOfMentions.add(followers.get(i).size());
    	}
    	
    	while(users.isEmpty() == false) {
    		maxIndex = numOfMentions.indexOf(Collections.max(numOfMentions));
    		mostFollowers.add(users.get(maxIndex));
    		users.remove(maxIndex);
    		numOfMentions.remove(maxIndex);
    	}
    }
    
    // checks if a username is valid
    public static boolean checkIfValidUsername(String username) {
    	for(int i = 0; i < username.length(); i++) {
    		char userLetter = username.charAt(i);
    		if((userLetter >= 'a' && userLetter <= 'z') || (userLetter >= '0' && userLetter <= '9') || (userLetter >= 'A' && userLetter <= 'Z') || (userLetter == '_')){              
    			// valid character
    		}
    		else
    			return false;
    	}
    	return true;
    }
    
    /**
     * Find all cliques in the social network.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     *
     * @return list of set of all cliques in the graph
     */
    List<Set<String>> findCliques(List<Tweets> tweets) {
        List<Set<String>> result = new ArrayList<Set<String>>();
        List<String> mentions = new ArrayList<>();
        Filter filter = new Filter();
        
        for(int i = 0; i < tweets.size(); i++) {
        	String tweeter = tweets.get(i).getName().toLowerCase();
        	mentions = getMentions(tweets.get(i).getText()); // get all username mentions
        	
        	// add one clique at a time
        	result.add(new HashSet<>());
    		result.get(result.size()-1).add(tweeter);
    		
    		// check if each mention, mentions the tweeter in mention's tweets
        	for(String mention: mentions) {
                List<Tweets> usernameTweets = filter.writtenBy(tweets, mention);
                if(checkIfUsersInClique(tweeter, mention, result) == false && checkIfUsernameMentionsUsers(usernameTweets, result.get(result.size()-1))) {
                	result.get(result.size()-1).add(mention);
                }
        	}
        	
        	// remove clique if only one member is in it
        	if(result.get(result.size()-1).size() == 1) {
        		result.remove(result.size()-1);
        	}
        }
        
        return result;
    }
    
    // checks if two users are in a clique
    public static boolean checkIfUsersInClique(String tweeter, String mention, List<Set<String>> result) {
    	for(Set<String> clique : result) {
    		if(clique.contains(tweeter) && clique.contains(mention)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    // checks if set of users show up in usernametweets
    public static boolean checkIfUsernameMentionsUsers(List<Tweets> usernameTweets, Set<String> users) {
    	String bigTweet = "";
    	
    	// add all tweets by username to one big string 
    	for(int i = 0; i < usernameTweets.size(); i++) {
    		bigTweet += usernameTweets.get(i).getText();
    		bigTweet += " ";
    	}
    	
    	for(String user : users) {
    		if(bigTweet.contains("@"+user) == false) {
    			return false;
    		}
    	}
    	return true;
    }
    
    
    // gets all the mentions in a tweet
    public static List<String> getMentions(String text){
    	List<String> mentions = new ArrayList<>();
    	String[] tweetArray = text.split(" ");
    	char firstLetter; // to check if first letter is @
    	String username; // save username
    	
    	for(String tweetWord: tweetArray) {
    		if(tweetWord.length() > 0) {
    			firstLetter = tweetWord.charAt(0);
        		if(firstLetter == '@') {
        			username = tweetWord.substring(1, tweetWord.length()).toLowerCase();
        			if(checkIfValidUsername(username)) {
        				mentions.add(username);
        			}
        		}
    		}
    	}
    	return mentions;
    }
}



