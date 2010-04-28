package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.activities.UserSearchActivity;
import com.nookdevs.twook.utilities.Utilities;

public class UserSearchService extends MessagesDownloaderService {
    private static final String TAG = UserSearchService.class.getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
	String username = Settings.getSettings().getUsername();
	String password = Settings.getSettings().getPassword();
	final Twitter twitter = new TwitterFactory().getInstance(username,
		password);
	try {
	    twitter.verifyCredentials();
	    final ResponseList<User> users = twitter
		    .searchUsers(((UserSearchActivity) getMainActivity())
			    .getSearchTerm(), 1);
	    return Utilities.userToTweets(users);

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return (ArrayList)Collections.emptyList();
	}
    }
}
