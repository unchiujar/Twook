package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class RepliesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = RepliesDownloaderService.class.getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
	Settings settings = Settings.getSettings();
	Twitter twitter = new TwitterFactory().getInstance(settings
		.getUsername(), settings.getPassword());
	ArrayList<Status> statuses;
	try {
	    statuses = twitter.getMentions();
	    return Utilities.statusToTweets(statuses);

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return (ArrayList)Collections.emptyList();
	}
    }
}
