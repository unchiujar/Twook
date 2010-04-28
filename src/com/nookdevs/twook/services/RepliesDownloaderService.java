package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class RepliesDownloaderService extends MessagesDownloaderService {
    /*LOG COMMENT  private static final String TAG = RepliesDownloaderService.class.getName();  LOG COMMENT*/

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
	    /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
	    return (ArrayList)Collections.emptyList();
	}
    }
}
