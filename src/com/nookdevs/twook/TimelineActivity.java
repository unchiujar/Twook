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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
 * 
 * Abstract Activity class containing common functionality between Activity
 * classes that display tweets.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see PersonalTimelineActivity
 * @see PublicTimelineActivity
 * @see RepliesTimelineActivity
 * 
 */
abstract public class TimelineActivity extends nookBaseActivity {

	/** Text listener used for scrolling and selecting in the ListView */
	private TextListener listListen = new TextListener(this);

	/**
	 * Progress dialog that is displayed when the tweets are retrieved. The
	 * message in the progress dialog depends on the implementation of
	 * {@link updateView} in subclasses.
	 */
	private ProgressDialog retriveProgress = null;
	/** The list of retrieved tweets. */
	private ArrayList<Tweet> retrievedTweets = null;
	/** Milliseconds to sleep the thread after getting the tweets */
	private final int SLEEP_TIME = 200;
	private TweetAdapter tweetAdapter;
	private Runnable viewTweets;

	// touchscreen buttons
	protected ImageButton btn_tweet = null;
	protected ImageButton btn_replies_timeline = null;
	protected ImageButton btn_personal_timeline = null;
	protected ImageButton btn_public_timeline = null;
	protected ImageButton btn_settings = null;

	Thread timelineThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NAME = "Twook";

		setContentView(R.layout.main);

		btn_tweet = (ImageButton) findViewById(R.id.tweet);
		btn_replies_timeline = (ImageButton) findViewById(R.id.replies);
		btn_personal_timeline = (ImageButton) findViewById(R.id.personalTimeline);
		btn_public_timeline = (ImageButton) findViewById(R.id.publicTimeline);
		btn_settings = (ImageButton) findViewById(R.id.settings);

		createDefaultListeners();
		createListeners();
		retrievedTweets = new ArrayList<Tweet>();
		this.tweetAdapter = new TweetAdapter(this, R.layout.single_tweet,
				retrievedTweets);
		setListAdapter(this.tweetAdapter);

		viewTweets = new Runnable() {
			@Override
			public void run() {
				getTimeline();
			}
		};
		ListView tweetLists = (ListView) findViewById(android.R.id.list);
		tweetLists.setOnKeyListener(listListen);
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
			retriveProgress.dismiss();
			tweetAdapter.notifyDataSetChanged();
		}
	};

	abstract protected List<Status> getTweets();

	private void getTimeline() {
		try {
			retrievedTweets = new ArrayList<Tweet>();
			List<Status> tweets = getTweets();

			for (Status status : tweets) {
				Tweet message = new Tweet();
				message.setMessage(status.getUser().getName() + " - "
						+ status.getText());
				message.setImage(downloadFile(status.getUser()
						.getProfileImageUrl()));
				retrievedTweets.add(message);

			}

			Thread.sleep(SLEEP_TIME);
			Log.i("ARRAY", "" + retrievedTweets.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private class TweetAdapter extends ArrayAdapter<Tweet> {
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
			Tweet o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				// TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				ImageView iv = (ImageView) v.findViewById(R.id.icon);
				if (tt != null) {
					tt.setText(o.getMessage());
				}
				if (iv != null) {
					iv.setImageBitmap(o.getImage());
				}
			}
			return v;
		}
	}

	Bitmap downloadFile(URI fileURI) {
		URL imageURL = null;
		try {
			imageURL = fileURI.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) imageURL
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();

			return BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// FIXME add default image
		return null;
	}

	// =================== Button listeners =======================
	private void createDefaultListeners() {
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
				Intent settingsIntent = new Intent(v.getContext(),
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
				Intent settingsIntent = new Intent(v.getContext(),
						RepliesTimelineActivity.class);
				startActivity(settingsIntent);
				Log.d(this.getClass().getName(),
						"Replies timeline button clicked");

			}
		});
		Log.d(this.getClass().getName(),
				"Personal timeline button listener set");

	}

	abstract protected void createListeners();

	protected void updateView(String message) {
		timelineThread = new Thread(null, viewTweets, "MagentoBackground");
		timelineThread.start();
		retriveProgress = ProgressDialog.show(TimelineActivity.this,
				"Please wait...", message, true);

	}

	class TextListener implements OnKeyListener {
		private TimelineActivity settings;

		public TextListener(TimelineActivity settings) {
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
}