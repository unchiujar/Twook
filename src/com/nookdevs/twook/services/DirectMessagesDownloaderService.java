package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class DirectMessagesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = DirectMessagesDownloaderService.class
	    .getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	Settings settings = Settings.getSettings();
	// The factory instance is re-useable and thread safe.
	final Twitter receiver = new TwitterFactory().getInstance(settings
		.getUsername(), settings.getPassword());
	ArrayList<DirectMessage> messages;
	try {
	    messages = receiver.getDirectMessages();
	    for (DirectMessage message : messages) {
		final Tweet tweet = new Tweet();
		tweet.setMessage(message.getText());
		tweet.setUsername(message.getSender().getName());
		tweet.setImage(Utilities.downloadFile(message.getSender()
			.getProfileImageURL()));
		tweets.add(tweet);
	    }
	    return tweets;

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return (ArrayList)Collections.emptyList();
	}
    }
}
