package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;


public class PersonalMessagesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = PersonalMessagesDownloaderService.class.getName();
    @Override
    protected ArrayList<Tweet> getTweets() {
        Settings settings = Settings.getSettings(this);
        Twitter twitter = settings.getConnection();

        List<Status> statuses;
        try {
            statuses = twitter.getUserTimeline();
            return Utilities.statusToTweets(statuses);

        } catch (TwitterException e) {
            Log.e(TAG, e.getMessage());
            return new ArrayList<Tweet>();
        }
    }

}
