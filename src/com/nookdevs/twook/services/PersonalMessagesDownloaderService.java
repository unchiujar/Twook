package com.nookdevs.twook.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nookdevs.twook.activities.Settings;
import com.nookdevs.twook.activities.Tweet;
import com.nookdevs.twook.utilities.Utilities;


public class PersonalMessagesDownloaderService extends MessagesDownloaderService {
    /*LOG COMMENT  private static final String TAG = PersonalMessagesDownloaderService.class.getName();  LOG COMMENT*/
    @Override
    protected ArrayList<Tweet> getTweets() {
        Settings settings = Settings.getSettings();
        Twitter twitter =
                new TwitterFactory().getInstance(settings.getUsername(),
                        settings.getPassword());
        List<Status> statuses;
        try {
            statuses = twitter.getUserTimeline();
            return Utilities.statusToTweets(statuses);

        } catch (TwitterException e) {
            /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
            return (ArrayList)Collections.emptyList();
        }
    }

}
