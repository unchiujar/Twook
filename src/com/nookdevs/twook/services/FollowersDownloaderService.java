package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.util.Log;

public class FollowersDownloaderService extends MessagesDownloaderService {
    private static final String TAG = FollowersDownloaderService.class
	    .getName();

    @Override
    protected List<Tweet> getTweets() {
	Settings settings = Settings.getSettings();
	// set the title at the top of the eink screen
	Twitter twitter = new TwitterFactory().getInstance(settings
		.getUsername(), settings.getPassword());
	try {
	    PagableResponseList<User> followers = twitter
		    .getFollowersStatuses();
	    return userToTweets(followers);

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return Collections.emptyList();
	}
    }
}
