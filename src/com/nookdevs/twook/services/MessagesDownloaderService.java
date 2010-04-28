package com.nookdevs.twook.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.nookdevs.twook.activities.TimelineActivity;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.ImageCache;
import com.nookdevs.twook.utilities.MemoryImageCache;
import com.nookdevs.twook.utilities.Utilities;

public abstract class MessagesDownloaderService extends Service {
    private ImageCache cache;
    private static final String TAG = MessagesDownloaderService.class.getName();
    private Timer timer = new Timer();
    private final long INTERVAL = 30000;
    private ArrayList<Tweet> timeline;
    private TimelineActivity mainActivity;

    public void doDownload() {
	try {
	    Log.d(TAG, "Timer step: " + INTERVAL);

	    Log
		    .d(TAG, "Got tweets, trying "
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

    private class DownloadTimer extends TimerTask {
	@Override
	public void run() {
	    doDownload();
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
			    Bitmap icon = Utilities.downloadFile(imageURL);

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

    abstract protected ArrayList<Tweet> getTweets();

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
	Log.d(TAG, "############## Service destroyed");
	doCleanup();
	//
	// if (MAIN_ACTIVITY != null)
	// AppUtils.showToastShort(MAIN_ACTIVITY, "MyService stopped");
    }

    public void doCleanup() {
	try {
	    Log.d(TAG, "Do cleanup called, trying to stop timer...");
	    timer.cancel();
	    mainActivity = null;
	    timeline = null;
	    timer = null;
	    stopSelf();
	    Log.d(TAG, "Cleanup done");
	} catch (Exception excep) {
	    excep.printStackTrace();
	}

    }

    // ipc method ?
    @Override
    public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
    }

}
