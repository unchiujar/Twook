package com.nookdevs.twook.services;

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

public class FollowedDownloaderService extends MessagesDownloaderService {
    private static final String TAG = FollowedDownloaderService.class.getName();

    @Override
    protected List<Tweet> getTweets() {
	Settings settings = Settings.getSettings();

	Twitter twitter = new TwitterFactory().getInstance(settings
		.getUsername(), settings.getPassword());
	try {
	    PagableResponseList<User> followed = twitter.getFriendsStatuses();
	    return userToTweets(followed);

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return Collections.emptyList();
	}
    }
}
