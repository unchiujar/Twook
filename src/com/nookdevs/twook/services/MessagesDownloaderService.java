package com.nookdevs.twook.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.Status;
import twitter4j.User;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.nookdevs.twook.activities.ImageCache;
import com.nookdevs.twook.activities.MemoryImageCache;
import com.nookdevs.twook.activities.TimelineActivity;
import com.nookdevs.twook.activities.Tweet;

public abstract class MessagesDownloaderService extends Service {
    private ImageCache cache;
    private static final String TAG = MessagesDownloaderService.class.getName();
    private Timer timer = new Timer();
    private final long INTERVAL = 180000;
    private List<Tweet> timeline;
    private TimelineActivity mainActivity;

    private class DownloadTimer extends TimerTask {
	@Override
	public void run() {
	    try {
		Log.d(TAG, "Timer step: " + INTERVAL);

		Log.d(TAG, "Got, tweets trying "
			+ "to set them on the activity...");
		try {
		    timeline = getTweets();
		    mainActivity.setRetrievedTweets(timeline);
		    // get images from net or database
		    loadImages();

		    // update view
		    // FIXME necessary ?

		} catch (Exception excep) {
		    Log.d("Error setting tweets", excep.getMessage());
		}
		Log.d(TAG, "Update done");
	    } catch (Exception e) {
		Log.e(TAG, e.getMessage());
	    }
	}
    }

    public void startDownload() {
	Log.d(TAG, "Service started.");
	DownloadTimer timerTask = new DownloadTimer();
	timer.scheduleAtFixedRate(timerTask, 0, INTERVAL);

    }

    public void loadImages() {
	cache = MemoryImageCache.getInstance();
	for (final Tweet tweet : timeline) {

	    final String username = tweet.getUsername();
	    if (!cache.isCached(username)) {
		Log.d(TAG, "Inserting icon in cache for user " + username);
		final URL imageURL = tweet.getImageURL();
		Runnable download = new Runnable() {
		    @Override
		    public void run() {
			Log.d(TAG, "Started thread with username: " + username
				+ " and image URL " + imageURL);
			// FIXME why ?
			if (imageURL != null) {
			    Bitmap icon = downloadFile(imageURL);

			    cache.insertImage(username, icon);
			    tweet.setImage(cache.getImage(username));

			    // FIXME 1 call for every icon
			    mainActivity.setRetrievedTweets(timeline);
			}

		    }
		};

		Thread thread = new Thread(download);
		thread.start();
	    } else {
		Log.d(TAG, "Image already exists" + " in cache for user "
			+ username);
		tweet.setImage(cache.getImage(username));
	    }
	}
    }

    /**
     * Utility method used for transforming a List of Message or Status into a
     * List of Tweet.
     * 
     * @param messages
     *            a List of classes implementinf ITweet
     * @return a List of Tweet to be used with the TweetAdapter
     */
    protected List<Tweet> statusToTweets(List<Status> messages) {
	Log.d(this.getClass().getName(), "Transforming Statuses "
		+ "to Tweets :" + messages.size());
	List<Tweet> tweets = new ArrayList<Tweet>();
	Tweet tweet;

	for (Status message : messages) {
	    tweet = new Tweet();
	    tweet.setUsername(message.getUser().getName());
	    tweet.setMessage(message.getText());
	    tweet.setImageURL(message.getUser().getProfileImageURL());
	    tweet.setImage(null);
	    tweets.add(tweet);
	    Log.v(this.getClass().getName(), "Added : " + tweet.getUsername()
		    + "- " + tweet.getMessage());
	}
	Log.d(TAG, "Tweets to status transformation done.");
	return tweets;
    }

    /**
     * Utility method used for transforming a List of User into a list of Tweet.
     * 
     * @param users
     * @return a List of Tweet to be used with the TweetAdapter
     */
    protected List<Tweet> userToTweets(List<User> users) {
	List<Tweet> tweets = new ArrayList<Tweet>();
	for (User user : users) {
	    try {
		Tweet tweet = new Tweet();
		tweet.setMessage(user.getDescription());
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

    abstract protected List<Tweet> getTweets();

    public void setMainActivity(TimelineActivity activity) {
	mainActivity = activity;
	Log.d(TAG, "Activity set." + mainActivity);
    }

    public Activity getMainActivity() {
	return mainActivity;
    }

    @Override
    public void onCreate() {
	super.onCreate();
	Log.d(TAG, "Service created");

	cache = MemoryImageCache.getInstance();

	// // get tweets without images
	// timeline = getTweets();
	// MAIN_ACTIVITY.setRetrievedTweets(timeline);
	// // get images from net or database
	// loadImages();
	// // update view
	// // FIXME necessary ?
	//
	// MAIN_ACTIVITY.setRetrievedTweets(timeline);

	// init the service here
	// _startService();
	//
	// if (MAIN_ACTIVITY != null)
	// AppUtils.showToastShort(MAIN_ACTIVITY, "MyService started");

    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	Log.d(TAG, "Service stopped");
	stopSelf();
	//
	// if (MAIN_ACTIVITY != null)
	// AppUtils.showToastShort(MAIN_ACTIVITY, "MyService stopped");
    }

    // ipc method ?
    @Override
    public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
    }

}
