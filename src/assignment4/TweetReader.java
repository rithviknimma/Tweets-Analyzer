package assignment4;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TweetReader contains method used to return tweets from method
 * Do not change the method header
 */
public class TweetReader {

	private static HttpURLConnection connection; 
	
    /**
     * Find tweets written by a particular user.
     *
     * @param url
     *            url used to query a GET Request from the server
     * @return return list of tweets from the server
     *
     */
    public static List<Tweets> readTweetsFromWeb(String url) throws Exception
    {
        List<Tweets> tweetList = new ArrayList<>();
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        ArrayList<String> responseStrings = new ArrayList<String>();
        try {
        	URL website = new URL(url);
        	connection = (HttpURLConnection) website.openConnection();
        	
        	// request setup
        	connection.setRequestMethod("GET");
        	connection.setConnectTimeout(5000);
        	connection.setReadTimeout(5000);
        	
        	
        	int status = connection.getResponseCode();
        	//System.out.println(status);
        	if(status > 299) {// if connection not successful
        		reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        		while((line = reader.readLine()) != null) {
        			responseContent.append(line);
        			responseStrings.add(line);
        		}
        		reader.close();
        	}
        	else {
        		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        		while((line = reader.readLine()) != null) {
        			responseContent.append(line);
        			responseStrings.add(line);
        		}
        		reader.close();
        	}
        	
        	// parse JSON data
        	parse(tweetList, responseContent.toString());
        	
        }
        catch(MalformedURLException e){
        	e.printStackTrace();
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
        finally {
        	connection.disconnect();
        }
        
        return tweetList;
    }
    
    // parses the JSON data structure and creates tweets if they have valid specs
    public static void parse(List<Tweets> tweetList, String jsonResponse) {
    	JSONArray jsonTweets = new JSONArray(jsonResponse);
    	
    	for(int i = 0; i < jsonTweets.length(); i++) {
    		JSONObject tweet = jsonTweets.getJSONObject(i);
    		int id = tweet.getInt("Id");
    		
    		try {
	    		String Name = tweet.getString("Name");
	    		String Date = tweet.getString("Date");
	    		String Text = tweet.getString("Text");
	    		if(checkifValidTweet(Name, Date, Text)) {
	    			tweetList.add(new Tweets());
		    		tweetList.get(tweetList.size() - 1).setId(id);
		    		tweetList.get(tweetList.size() - 1).setName(Name);
		    		tweetList.get(tweetList.size() - 1).setDate(Date);
		    		tweetList.get(tweetList.size() - 1).setText(Text);
	    		}
    		}
    		catch(JSONException e) {
    			// do nothing, don't add that tweet to tweetList
    		}
    		
    	}
    }
    
    // check if tweet has valid username, date, and text
    // Name: has to be a letter, number, or underscore (not case sensitive)
    // Date: has the format YYYY-MM-YYTHRS:MIN:SECSZ
    // Text: has to be less than or equal to 140 characters
    public static boolean checkifValidTweet(String username, String Date, String Text) {
    	boolean validUsername = true, validDate = false, validText = false;
    	
    	// set validUsername to false is it isn't valid
    	for(int i = 0; i < username.length(); i++) {
    		char userLetter = username.charAt(i);
    		if((userLetter >= 'a' && userLetter <= 'z') || (userLetter >= '0' && userLetter <= '9') || (userLetter >= 'A' && userLetter <= 'Z') || (userLetter == '_')){              
    			// valid character
    		}
    		else
    			validUsername = false;
    	}
    	
    	// set validDate
    	String datePattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z";
    	validDate = Date.matches(datePattern);
    	
    	// set validText
    	if(Text.length() <= 140) {
    		validText = true;
    	}
    	
    	if(validUsername && validDate && validText) {
    		return true;
    	}
    	else
    		return false;
    }
    
    
}
