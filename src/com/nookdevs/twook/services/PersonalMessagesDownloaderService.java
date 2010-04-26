package com.nookdevs.twook.services;

import java.util.Collections;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;


public class PersonalMessagesDownloaderService extends MessagesDownloaderService {
    private static final String TAG = PersonalMessagesDownloaderService.class.getName();
    @Override
    protected List<Tweet> getTweets() {
        Settings settings = Settings.getSettings();
        Twitter twitter =
                new TwitterFactory().getInstance(settings.getUsername(),
                        settings.getPassword());
        List<Status> statuses;
        try {
            statuses = twitter.getUserTimeline();
            return statusToTweets(statuses);

        } catch (TwitterException e) {
            Log.e(TAG, e.getMessage());
            return Collections.emptyList();
        }
    }

}
