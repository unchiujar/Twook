package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class FollowedDownloaderService extends MessagesDownloaderService {
    /*LOG COMMENT  private static final String TAG = FollowedDownloaderService.class.getName();  LOG COMMENT*/

    @Override
    protected ArrayList<Tweet> getTweets() {
	Settings settings = Settings.getSettings();

	Twitter twitter = new TwitterFactory().getInstance(settings
		.getUsername(), settings.getPassword());
	try {
	    PagableResponseList<User> followed = twitter.getFriendsStatuses();
	    return Utilities.userToTweets(followed);

	} catch (TwitterException e) {
	    /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
	    return (ArrayList)Collections.emptyList();
	}
    }
}
