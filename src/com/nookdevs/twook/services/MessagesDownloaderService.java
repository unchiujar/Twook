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

import com.nookdevs.twook.activities.TimelineActivity;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.ImageCache;
import com.nookdevs.twook.utilities.MemoryImageCache;
import com.nookdevs.twook.utilities.Utilities;

public abstract class MessagesDownloaderService extends Service {
    private ImageCache cache;
    /*LOG COMMENT  private static final String TAG = MessagesDownloaderService.class.getName();  LOG COMMENT*/
    private Timer timer = new Timer();
    private final long INTERVAL = 30000;
    private ArrayList<Tweet> timeline;
    private TimelineActivity mainActivity;

    public void doDownload() {
	try {
	    /*LOG COMMENT  Log.d(TAG, "Timer step: " + INTERVAL);  LOG COMMENT*/

	    /*LOG COMMENT Log.d(TAG, "Got tweets, trying to set them on the activity..."); LOG COMMENT*/
	    try {
		timeline = getTweets();
		mainActivity.setRetrievedTweets(timeline);
		// get images from net or database
		loadImages();

		// update view
		// FIXME necessary ?

	    } catch (Exception excep) {
		/*LOG COMMENT  Log.d("Error setting tweets", excep.getMessage());  LOG COMMENT*/
	    }
	    /*LOG COMMENT  Log.d(TAG, "Update done");  LOG COMMENT*/
	} catch (Exception e) {
	    /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
	}
    }

    private class DownloadTimer extends TimerTask {
	@Override
	public void run() {
	    doDownload();
	}
    }

    public void startDownload() {
	/*LOG COMMENT  Log.d(TAG, "Service started.");  LOG COMMENT*/
	DownloadTimer timerTask = new DownloadTimer();
	timer.scheduleAtFixedRate(timerTask, 0, INTERVAL);

    }

    public void loadImages() {
	cache = MemoryImageCache.getInstance();
	for (final Tweet tweet : timeline) {

	    final String username = tweet.getUsername();
	    if (!cache.isCached(username)) {
		/*LOG COMMENT  Log.d(TAG, "Inserting icon in cache for user " + username);  LOG COMMENT*/
		final URL imageURL = tweet.getImageURL();
		Runnable download = new Runnable() {
		    @Override
		    public void run() {
			/* LOG COMMENT Log.d(TAG, "Started thread with username: " + username + " and image URL " + imageURL); LOG COMMENT*/
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
		/*LOG COMMENT Log.d(TAG, "Image already exists" + " in cache for user "+ username); LOG COMMENT*/
		tweet.setImage(cache.getImage(username));
	    }
	}
    }

    abstract protected ArrayList<Tweet> getTweets();

    public void setMainActivity(TimelineActivity activity) {
	mainActivity = activity;
	/*LOG COMMENT  Log.d(TAG, "Activity set." + mainActivity);  LOG COMMENT*/
    }

    public Activity getMainActivity() {
	return mainActivity;
    }

    @Override
    public void onCreate() {
	super.onCreate();
	/*LOG COMMENT  Log.d(TAG, "Service created");  LOG COMMENT*/

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
	/*LOG COMMENT  Log.d(TAG, "############## Service destroyed");  LOG COMMENT*/
	doCleanup();
	//
	// if (MAIN_ACTIVITY != null)
	// AppUtils.showToastShort(MAIN_ACTIVITY, "MyService stopped");
    }

    public void doCleanup() {
	try {
	    /*LOG COMMENT  Log.d(TAG, "Do cleanup called, trying to stop timer...");  LOG COMMENT*/
	    timer.cancel();
	    mainActivity = null;
	    timeline = null;
	    timer = null;
	    stopSelf();
	    /*LOG COMMENT  Log.d(TAG, "Cleanup done");  LOG COMMENT*/
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
