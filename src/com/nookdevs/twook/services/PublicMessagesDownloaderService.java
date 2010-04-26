package com.nookdevs.twook.services;

import java.util.Collections;
import java.util.List;

import com.nookdevs.twook.activities.Tweet;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

public class PublicMessagesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = PublicMessagesDownloaderService.class
	    .getName();

    @Override
    protected List<Tweet> getTweets() {
	Log.d(TAG, "Getting public messages...");
	Twitter twitter = new TwitterFactory().getInstance();
	List<Status> statuses;
	try {
	    statuses = twitter.getPublicTimeline();
	    return statusToTweets(statuses);

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return Collections.emptyList();
	}
    }
}
