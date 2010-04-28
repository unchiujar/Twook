package com.nookdevs.twook.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.nookdevs.twook.activities.Tweet;

public class Utilities {
    private static final String TAG = Utilities.class.getName();

    /**
     * Utility method used for transforming a List of Tweet from twitter4j into
     * a List of Tweet.
     * 
     * @param messages
     *            a List of classes implementinf ITweet
     * @return a List of Tweet to be used with the TweetAdapter
     */
    public static ArrayList<Tweet> tweetsToTweets(List<twitter4j.Tweet> messages) {
	Log.d(TAG, "Transforming Statuses " + "to Tweets :" + messages.size());
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	Tweet tweet;

	for (twitter4j.Tweet message : messages) {
	    tweet = new Tweet();
	    tweet.setUsername(message.getFromUser());
	    tweet.setMessage(message.getText());
	    tweet.setImageURL(message.getProfileImageUrl());
	    tweet.setImage(null);
	    tweets.add(tweet);
	    Log.v(TAG, "Added : " + tweet.getUsername() + "- "
		    + tweet.getMessage());
	}
	Log.d(TAG, "Tweets to tweets transformation done.");
	return tweets;
    }

    /**
     * Utility method used for transforming a List of Message or Status into a
     * List of Tweet.
     * 
     * @param messages
     *            a List of classes implementinf ITweet
     * @return a List of Tweet to be used with the TweetAdapter
     */
    public static ArrayList<Tweet> statusToTweets(List<Status> messages) {
	Log.d(TAG, "Transforming Statuses " + "to Tweets :" + messages.size());
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	Tweet tweet;
	for (Status message : messages) {
	    tweet = new Tweet();
	    tweet.setUsername(message.getUser().getName());
	    tweet.setMessage(message.getText());
	    tweet.setImageURL(message.getUser().getProfileImageURL());
	    tweets.add(tweet);
	    Log.v(TAG, "Added : " + tweet.getUsername() + "- "
		    + tweet.getMessage());
	}
	Log.d(TAG, "Statuses to tweets transformation done.");
	return tweets;
    }

    /**
     * Utility method used for transforming a List of User into a list of Tweet.
     * 
     * @param users
     * @return a List of Tweet to be used with the TweetAdapter
     */
    public static ArrayList<Tweet> userToTweets(List<User> users) {
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	for (User user : users) {
	    try {
		Tweet tweet = new Tweet();
		if (user.getDescription() != null) {
		    tweet.setMessage(user.getDescription());
		} else {
		    tweet.setMessage("");
		}
		tweet.setImageURL(user.getProfileImageURL());
		tweet.setUsername(user.getName());
		tweets.add(tweet);
		Log.v(TAG, "tweet " + tweet.getMessage());
	    } catch (Exception excep) {
		excep.printStackTrace();
		Log.e(TAG, excep.getMessage());
	    }
	}
	return tweets;
    }

    public static Bitmap downloadFile(URI fileURI) {
	URL imageURL = null;
	try {
	    imageURL = fileURI.toURL();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return downloadFile(imageURL);
    }

    public static Bitmap downloadFile(URL fileURL) {

	try {
	    HttpURLConnection conn = (HttpURLConnection) fileURL
		    .openConnection();
	    conn.setDoInput(true);
	    conn.connect();
	    InputStream is = conn.getInputStream();
	    return BitmapFactory.decodeStream(is);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// FIXME add default image
	return null;
    }

}
