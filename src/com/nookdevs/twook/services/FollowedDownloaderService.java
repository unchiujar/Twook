package com.nookdevs.twook.services;

import java.util.ArrayList;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;

public class FollowedDownloaderService extends MessagesDownloaderService {
    private static final String TAG = FollowedDownloaderService.class.getName();

    @Override
    protected ArrayList<Tweet> getTweets() {
		Settings settings = Settings.getSettings(this);
		Twitter twitter = settings.getConnection();
	
		try {
		    PagableResponseList<User> followed = twitter.getFriendsStatuses();
		    return Utilities.userToTweets(followed);
	
		} catch (TwitterException e) {
		    Log.e(TAG, e.getMessage());
		    return new ArrayList<Tweet>();
		}
    }
}
