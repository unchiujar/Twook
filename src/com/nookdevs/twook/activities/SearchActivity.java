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

package com.nookdevs.twook.activities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
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
 * Activity that displays various searches.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * @see TimelineActivity
 */

public class SearchActivity extends TimelineActivity {
    /** Class name used in logging statements */
    private static final String TAG = SearchActivity.class.getName();
    private static final String SEARCH_BUTTON_MESSAGE = "Search button clicked (start search mode)";
    private int searchNumber = 0;
    private String currentSearch = "nook";

    private String search1 = "nook";
    private String search2 = "twook";
    private String search3 = "android";
    private String search4 = "android";


    private TextEditListener softKeyListener = new TextEditListener(this);
    private ListListener mlistListener = new ListListener(this);

    private EditText textSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.search);
	btn_search1 = (ImageButton) findViewById(R.id.search1);

	// this is needed because we load a different layout
	// so the listeners have to be reinitialized
	createListeners();
	// set the title at the top of the eink screen
	final Resources res = getResources();
	NAME = res.getText(R.string.app_name).toString()
		+ res.getText(R.string.title_separator).toString()
		+ res.getText(R.string.search).toString();
	final ListView list = (ListView) findViewById(android.R.id.list);
	list.requestFocus();
	list.setOnKeyListener(mlistListener);
	textSearch = (EditText) findViewById(R.id.search_term);
	textSearch.setOnKeyListener(softKeyListener);
	updateIcon();
//	updateView(MESSAGE + currentSearch);
    }

//    @Override
    protected List<Tweet> getTweets() {
	final Twitter twitter = new TwitterFactory().getInstance();
	try {
	    final Query query = new Query(currentSearch);
	    final QueryResult result = twitter.search(query);
	    final List<twitter4j.Tweet> resultTweets = result.getTweets();
	    final List<Tweet> tweets = new ArrayList<Tweet>();
	    for (twitter4j.Tweet tweet : resultTweets) {
		final Tweet tweetR = new Tweet();
		tweetR.setUsername(tweet.getFromUser());
		tweetR.setMessage(tweet.getText());
		tweetR.setImageURL(new URL(tweet.getProfileImageUrl()));
		tweets.add(tweetR);
	    }
	    return tweets;

	} catch (TwitterException e) {
	    Log.e(TAG, e.getMessage());
	    return Collections.emptyList();
	} catch (MalformedURLException e) {
	    Log.e(TAG, e.getMessage());
	    return Collections.emptyList();
	}
    }

    @Override
    protected void createListeners() {
	super.createDefaultListeners();
	btn_search1.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		textSearch.setText(search1);
		currentSearch = search1;
//		updateView(MESSAGE + search1);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	btn_search1.setOnLongClickListener(new OnLongClickListener() {

	    @Override
	    public boolean onLongClick(View view) {
		searchNumber = 1;
		textSearch.setText(search1);
		textSearch.requestFocus();
		final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		return true;
	    }
	});
	btn_search2.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		textSearch.setText(search2);
		currentSearch = search2;
//		updateView(MESSAGE + search2);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	btn_search2.setOnLongClickListener(new OnLongClickListener() {

	    @Override
	    public boolean onLongClick(View view) {
		searchNumber = 2;
		textSearch.setText(search2);
		textSearch.requestFocus();
		final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		return true;
	    }
	});
	btn_search3.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		textSearch.setText(search3);
		currentSearch = search3;
//		updateView(MESSAGE + search3);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	btn_search3.setOnLongClickListener(new OnLongClickListener() {

	    @Override
	    public boolean onLongClick(View view) {
		searchNumber = 3;
		textSearch.setText(search3);
		textSearch.requestFocus();
		final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		return true;
	    }
	});

	btn_search4.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		textSearch.setText(search4);
		currentSearch = search4;
//		updateView(MESSAGE + search4);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	btn_search4.setOnLongClickListener(new OnLongClickListener() {

	    @Override
	    public boolean onLongClick(View view) {
		searchNumber = 4;
		textSearch.setText(search4);
		textSearch.requestFocus();
		final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		return true;
	    }
	});

    }

    /**
     * @author Vasile Jureschi <vasile.jureschi@gmail.com>
     * 
     */
    class TextEditListener implements OnKeyListener {
	private SearchActivity settings;

	public TextEditListener(SearchActivity settings) {
	    this.settings = settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int,
	 * android.view.KeyEvent)
	 */
	public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
	    Log.d(this.getClass().getName(), "Received keycode: " + keyCode);
	    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
		if (view instanceof EditText) {
		    final EditText editTxt = (EditText) view;
		    if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_CLEAR) {
			editTxt.setText("");
		    }
		    // Clear
		    if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_SUBMIT) {
			currentSearch = editTxt.getText().toString();
//			updateView(MESSAGE + currentSearch);
			switch (searchNumber) {
			case 1:search1 = currentSearch;
			    break;
			case 2:search2 = currentSearch;
			    break;
			case 3:search3 = currentSearch;
			    break;
			case 4:search4 = currentSearch;
			    break;
			}
		    }

		}
	    }
	    return true;
	}
    }

    @Override protected void stopDownloadService() { stopService(intent);}   @Override
    protected void setDownloadService() {
	// TODO Auto-generated method stub
	
    }

}