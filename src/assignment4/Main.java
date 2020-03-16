package assignment4;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Main {
    final static String URLEndpoint = "http://kevinstwitterclient2.azurewebsites.net/api/products";

    /**
     * We will not use your Main class to test your code
     * Feel free to modify this as much as you want
     * Here is an example of how you might test the code from main
     * for Problem 1 and Problem 2
     */
    public static void main(String[] args) throws Exception {

        // Problem 1: Returning Tweets from Server
        TweetReader reader = new TweetReader();
        List<Tweets> tweetsList = reader.readTweetsFromWeb(URLEndpoint);
        System.out.println(tweetsList);

        // Problem 2:
        // Filter Tweets by Username
        Filter filter = new Filter();
        List<Tweets> filteredUser = filter.writtenBy(tweetsList,"ulysse");
        System.out.println(filteredUser);

        // Filter by Timespan
        Instant testStart = Instant.parse("2017-11-11T00:00:00Z");
        Instant testEnd = Instant.parse("2017-11-13T12:00:00Z");
        Timespan timespan = new Timespan(testStart,testEnd);
        List<Tweets> filteredTimeSpan = filter.inTimespan(tweetsList,timespan);
        System.out.println(filteredTimeSpan);

        //Filter by words containinng
        List<Tweets> filteredWords = filter.containing(tweetsList,Arrays.asList( "good"));
        System.out.println(filteredWords);
        
        // Most followed
        System.out.println(SocialNetwork.findKMostFollower(tweetsList, 100));
        SocialNetwork fb = new SocialNetwork();
        
        // Cliques
        System.out.println(fb.findCliques(tweetsList));
    }
}

