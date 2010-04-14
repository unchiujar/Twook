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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.nookdevs.common.nookBaseSimpleActivity;

/**
 * 
 * Activity that displays the replies timeline of the user.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see TimelineActivity
 * 
 */

public class SearchActivity extends TimelineActivity {
	String searchTerm = "nook";
	private TextListener softKeyListener = new TextListener(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		btn_search_test = (ImageButton) findViewById(R.id.search1);
		
		//this is needed because we load a different layout
		//so the listeners have to reinitialized
		createListeners();
		// set the title at the top of the eink screen
		Resources res = getResources();
		NAME = "SEARCH - WORK IN PROGRESS";
		ListView list = (ListView) findViewById(android.R.id.list);
		list.requestFocus();
		EditText tweet = (EditText) findViewById(R.id.search_term);
		tweet.setOnKeyListener(softKeyListener);
	}

	@Override
	protected List<Tweet> getTweets() {
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			Query query = new Query(searchTerm);
			QueryResult result = twitter.search(query);
			List<twitter4j.Tweet> resultTweets = result.getTweets();
			List<Tweet> tweets = new ArrayList<Tweet>();
			for (twitter4j.Tweet tweet : resultTweets) {
				Tweet tweetR = new Tweet();
				tweetR.setUsername(tweet.getFromUser());
				tweetR.setMessage(tweet.getText());
				tweetR
						.setImage(downloadFile(new URL(tweet
								.getProfileImageUrl())));
				tweets.add(tweetR);
			}
			return tweets;

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void createListeners() {
		super.createDefaultListeners();
		btn_search_test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateView("Searching for " + searchTerm);
				Log.d(this.getClass().getName(), "Search button clicked");

			}
		});
		btn_search_test.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				EditText textSearch = (EditText) findViewById(R.id.search_term);
				textSearch.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});
	}

	class TextListener implements OnKeyListener {
		private SearchActivity settings;

		public TextListener(SearchActivity settings) {
			this.settings = settings;
		}

		public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
			Log.d(this.getClass().getName(), "Received keycode: " + keyCode);
			if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
				if (view instanceof EditText) {
					EditText editTxt = (EditText) view;
					if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_CLEAR) {
						editTxt.setText("");
					}

					if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_SUBMIT) { // Clear
						searchTerm = editTxt.getText().toString();
						updateView("Retrieving results for " + searchTerm);
					}

				}
			}
			return true;
		}
	}

}