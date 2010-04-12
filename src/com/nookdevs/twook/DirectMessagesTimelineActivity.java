/***********************************************
This file is part of the Twook project (**linky**).

    Twook is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Twook is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Twook.  If not, see <http://www.gnu.org/licenses/>.

 **********************************************/

package com.nookdevs.twook;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * Activity that displays the retweets of the user's messages.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see TimelineActivity
 * 
 */
public class DirectMessagesTimelineActivity extends TimelineActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateView("Retrieving direct messages timeline");
	}

	@Override
	protected List<Tweet> getTweets() {
		List<Tweet> tweets = new ArrayList<Tweet>();
		Settings settings = Settings.getSettings();
		// The factory instance is re-useable and thread safe.
		Twitter receiver = new TwitterFactory().getInstance(settings
				.getUsername(), settings.getPassword());
		List<DirectMessage> messages;
		try {
			messages = receiver.getDirectMessages();
			for (DirectMessage message : messages) {
				Tweet tweet = new Tweet();
				tweet.setMessage(message.getText());
				tweet.setUsername(message.getSender().getName());
				tweet.setImage(downloadFile(message.getSender()
						.getProfileImageURL()));

			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweets;
	}

	@Override
	protected void createListeners() {
		btn_direct_messages_timeline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateView("Retrieving direct messages timeline");
				Log.d(this.getClass().getName(),
						"Direct messages timeline button clicked");

			}
		});

	}
}