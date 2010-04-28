package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class PublicMessagesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = PublicMessagesDownloaderService.class
	    .getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
	Log.d(TAG, "Getting public messages...");
	Twitter twitter = new TwitterFactory().getInstance();
	ArrayList<Status> statuses;
	try {
	    statuses = twitter.getPublicTimeline();
	    return Utilities.statusToTweets(statuses);

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return (ArrayList)Collections.emptyList();
	}
    }
}
