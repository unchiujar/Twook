package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nookdevs.twook.activities.SearchActivity;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class SearchService extends MessagesDownloaderService {
    /*LOG COMMENT  private static final String TAG = SearchService.class.getName();  LOG COMMENT*/

    @Override
    protected ArrayList<Tweet> getTweets() {
	final Twitter twitter = new TwitterFactory().getInstance();
	try {
	    Query query = new Query(((SearchActivity)getMainActivity()).getSearchTerm());
	   
	    QueryResult result = twitter.search(query);

	    return Utilities.tweetsToTweets(result.getTweets());

	} catch (TwitterException e) {
	    /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
	    return (ArrayList)Collections.emptyList();
	}
    }
}
