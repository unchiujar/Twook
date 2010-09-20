package com.nookdevs.twook.services;

import java.util.ArrayList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class PublicMessagesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = PublicMessagesDownloaderService.class
	    .getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
		Log.d(TAG, "Getting public messages...");
		// don't need authentication here, but we'll use it anyway
		Settings settings = Settings.getSettings(this);
		Twitter twitter = settings.getConnection();
		Log.d(TAG, "Got connection, downloading...");
		ResponseList<Status> statuses;
		try {
		    statuses = twitter.getPublicTimeline();
		    return Utilities.statusToTweets(statuses);
	
		} catch (TwitterException e) {
		    Log.e(TAG, e.getMessage());
		    return new ArrayList<Tweet>();
		}
    }
}
