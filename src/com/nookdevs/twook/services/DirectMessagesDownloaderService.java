package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class DirectMessagesDownloaderService extends MessagesDownloaderService {
    /*LOG COMMENT private static final String TAG = DirectMessagesDownloaderService.class.getName(); LOG COMMENT*/

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
	    /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
	    return (ArrayList)Collections.emptyList();
	}
    }
}
