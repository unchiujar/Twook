package com.nookdevs.twook.services;

import java.util.ArrayList;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
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
		Settings settings = Settings.getSettings(this);
		Twitter twitter = settings.getConnection();

		try {
		    final ResponseList<User> users = twitter
			    .searchUsers(((UserSearchActivity) getMainActivity())
				    .getSearchTerm(), 1);
		    return Utilities.userToTweets(users);
	
		} catch (TwitterException e) {
		    Log.e(TAG, e.getMessage());
		    return new ArrayList<Tweet>();
		}
    }
}
