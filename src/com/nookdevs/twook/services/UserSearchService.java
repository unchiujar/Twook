package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.activities.UserSearchActivity;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.util.Log;

public class UserSearchService extends MessagesDownloaderService {
    private static final String TAG = UserSearchService.class.getName();

    @Override
    protected List<Tweet> getTweets() {
	String username = Settings.getSettings().getUsername();
	String password = Settings.getSettings().getPassword();
	final Twitter twitter = new TwitterFactory().getInstance(username,
		password);
	try {
	    twitter.verifyCredentials();
	    // FIXME add support for getting the sear
	    final ResponseList<User> users = twitter
		    .searchUsers(((UserSearchActivity) getMainActivity())
			    .getSearchTerm(), 1);
	    List<Tweet> tweets = new ArrayList<Tweet>();
	    // FIXME userToTweets doesn't seem to work
	    for (User user : users) {
		Tweet tweet = new Tweet();
		// FIXME user.getStatus().getText() does an NPE, twitter4j bug ?
		tweet.setMessage("BUGGY twitter4j");
		tweet.setUsername(user.getName());
		tweet.setImageURL(user.getProfileImageURL());
		tweets.add(tweet);
		Log.d(this.getClass().getName(), tweet.getUsername() + " - "
			+ tweet.getMessage());
	    }
	    return tweets;

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return Collections.emptyList();
	}
    }
}
