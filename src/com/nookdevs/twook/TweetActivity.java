/***********************************************
This file is part of the Twook project http://github.com/unchiujar/Twook

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

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nookdevs.common.nookBaseSimpleActivity;

/**
 * 
 * Activity that prompts the user to enter a tweet.
 * 
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see Settings
 * 
 */
public class TweetActivity extends nookBaseSimpleActivity {
	private ProgressDialog postProgress = null;
	private final int WAIT_TIME = 200;
	private final int TWEET_LENGTH = 140;
	Thread postTweetThread;
	private Runnable postTweet;
	private TextListener softKeyListener = new TextListener(this);
	int left;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the title at the top of the eink screen
		Resources res = getResources();
		NAME = res.getText(R.string.app_name).toString()
				+ res.getText(R.string.title_separator).toString()
				+ res.getText(R.string.tweet_title).toString();
		setContentView(R.layout.write_tweet);
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		EditText tweet = (EditText) findViewById(R.id.tweet_message);
		tweet.setOnKeyListener(softKeyListener);

		postTweet = new Runnable() {
			@Override
			public void run() {
				updateStatus();
			}
		};

	}

	private void updateStatus() {
		try {
			Settings settings = Settings.getSettings();
			EditText tweetEdit = (EditText) findViewById(R.id.tweet_message);
			String tweet = tweetEdit.getText().toString();

			// The factory instance is re-useable and thread safe.
			Twitter twitter = new TwitterFactory().getInstance(settings
					.getUsername(), settings.getPassword());
			twitter.updateStatus(tweet);
			Thread.sleep(WAIT_TIME);
		} catch (InterruptedException excep) {
			Log.e(this.getClass().getName(), "Thread has been interrupted "
					+ excep.getMessage());
		} catch (TwitterException excep) {
			Log.e(this.getClass().getName(), "Twitter exception"
					+ excep.getMessage());
		}

	}

	private void processCmd(int keyCode) {
		switch (keyCode) {
		case SOFT_KEYBOARD_SUBMIT: {

			EditText tweet = (EditText) findViewById(R.id.tweet_message);

			Log.d(this.getClass().getName(), "Message is : "
					+ tweet.getText().toString());
			if (left >= 0) {
				postTweetThread = new Thread(null, postTweet,
						"MagentoBackground");
				postTweetThread.start();
				postProgress = ProgressDialog.show(TweetActivity.this,
						"Please wait...", "Updating status...", true);

				Log.d(this.getClass().getName(), "Tweet posted");
				finish();
			} else {
				TextView tweetLength = (TextView) findViewById(R.id.tweet_length);
				tweetLength.setText("Remove " + Math.abs(left)
						+ " characters before submitting.");

			}
			break;
		}
		case SOFT_KEYBOARD_CANCEL: {
			finish();
			break;
		}
		}
	}

	class TextListener implements OnKeyListener {
		private TweetActivity settings;

		public TextListener(TweetActivity settings) {
			this.settings = settings;
		}

		public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
			Log.d(this.getClass().getName(), "Received keycode: " + keyCode);
			if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
				if (view instanceof EditText) {
					EditText editTxt = (EditText) view;
					if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_CLEAR) { // Clear
						editTxt.setText("");
					} else {
						// calculate the length of the text entered
						// and display the remaining number of characters
						TextView tweetLength = (TextView) findViewById(R.id.tweet_length);
						left = TWEET_LENGTH
								- editTxt.getText().toString().length();
						tweetLength.setText("Characters remaining " + left);
						settings.processCmd(keyCode);
					}
				}
			}
			return false;
		}
	}
}