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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nookdevs.common.nookBaseActivity;

/**
 * Abstract Activity class containing common functionality between Activity
 * classes that display tweets.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * @see PersonalTimelineActivity
 * @see PublicTimelineActivity
 * @see RepliesTimelineActivity
 * @see RetweetsTimelineActivity
 */
public abstract class TimelineActivity extends nookBaseActivity {
    private final static String TAG = TimelineActivity.class.getName();
    private final static String SEARCH_BUTTON_MESSAGE = "Search button clicked (intent start mode)";
    /** Text listener used for scrolling and selecting in the ListView */
    private ListListener listListen = new ListListener(this);

    /** The list of retrieved tweets. */
    private ArrayList<Tweet> retrievedTweets;
    /** Milliseconds to sleep the thread after getting the tweets */
    private TweetAdapter tweetAdapter;

    // touchscreen buttons
    protected ImageButton btn_tweet;
    protected ImageButton btn_replies_timeline;
    protected ImageButton btn_personal_timeline;
    protected ImageButton btn_public_timeline;
    protected ImageButton btn_settings;
    protected ImageButton btn_retweets_timeline;
    protected ImageButton btn_direct_messages_timeline;
    protected ImageButton btn_followers;
    protected ImageButton btn_followed;
    protected ImageButton btn_home_timeline;
    protected ImageButton btn_search1;
    protected ImageButton btn_search2;
    protected ImageButton btn_search3;
    protected ImageButton btn_search4;
    protected ImageButton btn_favorites;
    protected ImageButton btn_search_users;

    protected Intent intent;
    private Handler guiHandler;
    @Override
    protected void onRestart() {

	super.onRestart();
	Log.i(TAG, "Activity restarted");
	startService(intent);
    }

    @Override
    protected void onStart() {
	super.onStart();
	Log.i(TAG, "Activity started");
    }

    @Override
    protected void onStop() {
	super.onStop();
	Log.i(TAG, "Activity stopped");

	stopService(intent);
    }

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	NAME = "Twook";

	setContentView(R.layout.main);

	createDefaultListeners();
	createListeners();
	retrievedTweets = new ArrayList<Tweet>();
	this.tweetAdapter = new TweetAdapter(this, R.layout.single_tweet,
		retrievedTweets);
	setListAdapter(this.tweetAdapter);

	updateIcon();

	ListView tweetLists = (ListView) findViewById(android.R.id.list);
	tweetLists.setOnKeyListener(listListen);
	setDownloadService();
    }

    protected abstract void setDownloadService();
    protected abstract void stopDownloadService();


    public void onResume() {
	super.onResume();
	updateIcon();
    }

    /**
     * Updates the icon of the user on the tweet button.
     */
    protected void updateIcon() {
	Settings settings = Settings.getSettings();
	if (settings.getIcon() != null) {
	    ImageView photo = (ImageView) findViewById(R.id.tweet);
	    photo.setImageBitmap(settings.getIcon());
	}
    }

    private Runnable returnRes = new Runnable() {

	@Override
	public void run() {

	    if (retrievedTweets != null && retrievedTweets.size() > 0) {
		// m_adapter.notifyDataSetChanged();
		tweetAdapter.clear();
		for (Tweet tweet : retrievedTweets) {
		    tweetAdapter.add(tweet);
		}
	    }
	    // retriveProgress.dismiss();
	    tweetAdapter.notifyDataSetChanged();
	}
    };

    public void setRetrievedTweets(List<Tweet> tweets) {
	Log.d(TAG, "Setting new list of messages");
	retrievedTweets = (ArrayList<Tweet>) tweets;
	runOnUiThread(returnRes);

    }

    public class TweetAdapter extends ArrayAdapter<Tweet> {
	private ArrayList<Tweet> items;

	public TweetAdapter(Context context, int textViewResourceId,
		ArrayList<Tweet> items) {
	    super(context, textViewResourceId, items);
	    this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = convertView;
	    if (v == null) {
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.single_tweet, null);
	    }
	    Tweet tweet = items.get(position);
	    if (tweet != null) {
		TextView tt = (TextView) v.findViewById(R.id.toptext);
		// TextView bt = (TextView) v.findViewById(R.id.bottomtext);
		ImageView iv = (ImageView) v.findViewById(R.id.icon);
		if (tt != null) {
		    tt
			    .setText(tweet.getUsername() + " - "
				    + tweet.getMessage());
		}
		if (iv != null) {
		    iv.setImageBitmap(tweet.getImage());
		}
	    }
	    return v;
	}
    }

    // =================== Button listeners =======================
    protected void createDefaultListeners() {

	btn_tweet = (ImageButton) findViewById(R.id.tweet);
	btn_replies_timeline = (ImageButton) findViewById(R.id.replies);
	btn_personal_timeline = (ImageButton) findViewById(R.id.personalTimeline);
	btn_public_timeline = (ImageButton) findViewById(R.id.publicTimeline);
	btn_settings = (ImageButton) findViewById(R.id.settings);
	btn_retweets_timeline = (ImageButton) findViewById(R.id.retweets);
	btn_direct_messages_timeline = (ImageButton) findViewById(R.id.direct);
	btn_followers = (ImageButton) findViewById(R.id.followers);
	btn_followed = (ImageButton) findViewById(R.id.following);
	btn_home_timeline = (ImageButton) findViewById(R.id.home);

	btn_search1 = (ImageButton) findViewById(R.id.search1);
	btn_search2 = (ImageButton) findViewById(R.id.search2);
	btn_search3 = (ImageButton) findViewById(R.id.search3);
	btn_search4 = (ImageButton) findViewById(R.id.search4);
	btn_favorites = (ImageButton) findViewById(R.id.favorite);
	btn_search_users = (ImageButton) findViewById(R.id.findpeople);

	btn_tweet.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			TweetActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), "Tweet button clicked");
	    }
	});
	Log.d(this.getClass().getName(), "Tweet button listener set");
	btn_settings.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO show view for adding username, password and refresh time
		Intent settingsIntent = new Intent(v.getContext(),
			SettingsActivity.class);
		startActivityForResult(settingsIntent, 0);
		Log.d(this.getClass().getName(), "Settings button clicked");
	    }
	});
	Log.d(this.getClass().getName(), "Settings button listener set");

	btn_public_timeline.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			PublicTimelineActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(),
			"Public timeline button clicked");

	    }
	});
	Log.d(this.getClass().getName(), "Public timeline button listener set");

	btn_personal_timeline.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent settingsIntent = new Intent(v.getContext(),
			PersonalTimelineActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(),
			"Personal timeline button clicked");

	    }
	});
	Log.d(this.getClass().getName(),
		"Personal timeline button listener set");

	btn_replies_timeline.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent settingsIntent = new Intent(v.getContext(),
			RepliesTimelineActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(),
			"Replies timeline button clicked");

	    }
	});
	Log
		.d(this.getClass().getName(),
			"Replies timeline button listener set");

	btn_retweets_timeline.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		final Intent settingsIntent = new Intent(v.getContext(),
			RetweetsTimelineActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(),
			"Retweets timeline button clicked");

	    }
	});
	Log.d(this.getClass().getName(),
		"Retweets timeline button listener set");

	btn_direct_messages_timeline.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			DirectMessagesTimelineActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(),
			"Direct messages timeline button clicked");

	    }
	});
	Log.d(this.getClass().getName(),
		"Direct messages timeline button listener set");

	btn_followers.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			FollowersActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), "Followers button clicked");

	    }
	});
	Log.d(this.getClass().getName(), "Followers button listener set");

	btn_followed.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			FollowedActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(),
			"Followed users button clicked");

	    }
	});
	Log.d(this.getClass().getName(), "Followed users button listener set");

	btn_home_timeline.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			HomeTimelineActivity.class);
		startActivity(settingsIntent);
		Log
			.d(this.getClass().getName(),
				"Home timeline button clicked");

	    }
	});
	Log.d(this.getClass().getName(), "Home timeline button listener set");

	btn_search1.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			SearchActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	Log.d(this.getClass().getName(), "Search button listener set");

	btn_search2.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			SearchActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	Log.d(this.getClass().getName(), "Search button listener set");

	btn_search3.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			SearchActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	Log.d(this.getClass().getName(), "Search button listener set");

	btn_search4.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			SearchActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), SEARCH_BUTTON_MESSAGE);

	    }
	});
	Log.d(this.getClass().getName(), "Search button listener set");

	btn_favorites.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			FavoriteTimelineActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), "Favorites button clicked.");

	    }
	});
	Log.d(this.getClass().getName(), "Favorite button listener set");

	btn_search_users.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent settingsIntent = new Intent(v.getContext(),
			UserSearchActivity.class);
		startActivity(settingsIntent);
		Log.d(this.getClass().getName(), "User search button clicked.");

	    }
	});
	Log.d(this.getClass().getName(), "User search listener set");

    }

    abstract protected void createListeners();

    protected class ListListener implements OnKeyListener {
	private TimelineActivity settings;

	public ListListener(TimelineActivity settings) {
	    this.settings = settings;
	}

	public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
	    Log.d(this.getClass().getName(), "Received keycode: " + keyCode);
	    if (keyEvent.getAction() == KeyEvent.ACTION_UP
		    && keyCode == NOOK_PAGE_UP_KEY_LEFT) {
		ListView list = (ListView) findViewById(android.R.id.list);
		// scroll the maximum amount if the top isn't reached
		// OR scroll to reach the top otherwise
		list.scrollBy(0,
			list.getScrollY() >= list.getMaxScrollAmount() ? -list
				.getMaxScrollAmount() : -list.getScrollY());
		Log.d(this.getClass().getName(), "Changing scroll, "
			+ "max is :" + list.getMaxScrollAmount()
			+ " current is : " + list.getScrollY());
	    }
	    if (keyEvent.getAction() == KeyEvent.ACTION_UP
		    && keyCode == NOOK_PAGE_DOWN_KEY_LEFT) {
		ListView // list.getSelectedView().setBackgroundColor(30);
		list = (ListView) findViewById(android.R.id.list);
		list.scrollBy(0, list.getMaxScrollAmount());
		Log.d(this.getClass().getName(), "Changing selection ");
	    }

	    if (keyEvent.getAction() == KeyEvent.ACTION_UP
		    && keyCode == NOOK_PAGE_UP_KEY_RIGHT) {
		ListView list = (ListView) findViewById(android.R.id.list);
		list.setSelection(5);
		list.setSelected(true);
		Log.d(this.getClass().getName(), "Selected :"
			+ list.getSelectedItemPosition());

	    }

	    if (keyEvent.getAction() == KeyEvent.ACTION_UP
		    && keyCode == NOOK_PAGE_DOWN_KEY_RIGHT) {
		ListView list = (ListView) findViewById(android.R.id.list);
		list.setSelection(9);
		list.setSelected(true);
		Log.d(this.getClass().getName(), "Selected :"
			+ list.getSelectedItemPosition());
	    }

	    return false;
	}
    }

    @Override
    public void onPause() {
	super.onPause();
	Log.i(TAG, "Activity paused");
	stopDownloadService();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	Log.i(TAG, "Activity destroyed");
	stopDownloadService();

    }

}