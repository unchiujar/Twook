package com.nookdevs.twook.services;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.util.Log;

import com.nookdevs.twook.activities.SearchActivity;
import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class SearchService extends MessagesDownloaderService {
    private static final String TAG = SearchService.class.getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
    	
    	// don't really need authentication, but it's better to do it anyway
    	Settings settings = Settings.getSettings(this);
		final Twitter twitter = settings.getConnection();

		try {
		    Query query = new Query(((SearchActivity)getMainActivity()).getSearchTerm());
		   
		    QueryResult result = twitter.search(query);
	
		    return Utilities.tweetsToTweets(result.getTweets());
	
		} catch (TwitterException e) {
		    Log.e(TAG, e.getMessage());
		    return new ArrayList<Tweet>();
		}
    }
}
