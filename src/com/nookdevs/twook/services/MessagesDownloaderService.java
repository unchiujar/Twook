package com.nookdevs.twook.services;

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

class IconDownloader implements Runnable {
	private Tweet t;
    private MessagesDownloaderService svc;
	private MemoryImageCache cache = MemoryImageCache.getInstance();
	
	public IconDownloader(Tweet t, MessagesDownloaderService svc) {
		this.t = t;
		this.svc = svc;
	}
	
    @Override
    public void run() {
    	//Log.d(TAG, "Started download thread with username: " + username
		//	+ " and image URL " + imageURL);
		// FIXME why ?
		if (t.getImageURL() != null) {
		    Bitmap icon = Utilities.downloadFile(t.getImageURL());

		    cache.insertImage(t.getUsername(), icon);
		    svc.setUserIcon(t.getUsername(), icon);
		}
    }
}

public abstract class MessagesDownloaderService extends Service {
    private ImageCache cache;
    private static final String TAG = MessagesDownloaderService.class.getName();
    private Timer timer;
    private final long INTERVAL = 30000;
    private ArrayList<Tweet> timeline;
    private TimelineActivity mainActivity;
	private static int activeDownloads = 0;

    public void doDownload() {
	    //Log.d(TAG, "Timer step: " + INTERVAL);

	    Log.d(TAG, "Timeline download...");
	    try {
			timeline = getTweets();
			// get images from net or database
			Log.d(TAG, "Loading images...");
			loadImages();
			// loadImages should return very quickly, as it either queues
			// threads to download the icons or retrieves cached icons,
			// so set these once cached images have already been set.
			Log.d(TAG, "Got timeline, setting to mainActivity...");
			mainActivity.setRetrievedTweets(timeline);
	
			// update view
			// FIXME necessary ?
	    } catch (Exception excep) {
	    	Log.d("Error setting tweets", excep.getMessage());
	    }
	    Log.d(TAG, "Update done");
    }

    private class DownloadTimer extends TimerTask {
		@Override
		public void run() {
		    doDownload();
		}
    }

    public void stopDownload() {
    	Log.d(TAG, "Service paused");
    	if(null != timer) {
	    	timer.cancel();
	    	timer = null;
    	}
    }

    public void startDownload() {
    	/* We may get a resume by itself, or after a start. Don't duplicate
    	 * our service runs by creating multiple timers.
    	 */
    	if(null == timer) {
	    	Log.d(TAG, "Service resumed");
	    	timer = new Timer();
	    	DownloadTimer timerTask = new DownloadTimer();
			timer.scheduleAtFixedRate(timerTask, 0, INTERVAL);
    	}
    }
    
    /* Note that this should ONLY be called from the downloader runnable */
    public void setUserIcon(String username, Bitmap bmp) {
	    for(Tweet t : timeline) {
	    	// update image for all tweets by this user
	    	if(t.getUsername().equals(username)) {
	    		t.setImage(cache.getImage(username));
	    	}
	    }

	    // only send an update to the main activity if there are no
	    // remaining active downloads.
	    MessagesDownloaderService.activeDownloads--;
	    // also make sure activity hasn't disappeared while we were downloading
	    if(0 == activeDownloads && null != mainActivity && 
	    		!mainActivity.isFinishing()) {
	    	mainActivity.setRetrievedTweets(timeline);
	    }
    }
    
    public void loadImages() {
		cache = MemoryImageCache.getInstance();
		for (Tweet tweet : timeline) {
	
		    String username = tweet.getUsername();
		    if (!cache.isCached(username)) {
				Log.d(TAG, "Inserting icon in cache for user " + username);
				// first, mark the image as cached so we don't duplicate any downloads
				cache.insertImage(username, null);

			    MessagesDownloaderService.activeDownloads++;
				Runnable download = new IconDownloader(tweet, this);
		
				Thread thread = new Thread(download);
				thread.start();
		    } else {
		    	//Log.d(TAG, "Image already exists" + " in cache for user "
				//	+ username);
		    	Bitmap icon = cache.getImage(username);
		    	if(null != icon) {
		    		tweet.setImage(icon);
		    	}
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
		    if(null != timer) {
		    	timer.cancel();
		    }
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
