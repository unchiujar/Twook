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

import com.nookdevs.twook.utilities.Utilities;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 
 * Singleton class holding the password and username.
 * 
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see SettingsActivity
 * 
 */

public class Settings {
	private final static String TAG = Settings.class.getName();

	private static final String consumerKey = "aM70gIvV9eewUUKWc7XtNg";
	private static final String consumerSecret = "ctsZVh9Dvwyv0kXG4RPW26pzKsA3GIjqTihlMYcNdA";

	private SharedPreferences mPrefs;

	public static final String PREFSTRING = "TwookPrefs";

	// OAuth tokens
	private User user;
	private AccessToken access;

	ArrayList<String> searches = new ArrayList<String>();
	int refresh;

	// singleton
	private static Settings settings;

	private Bitmap icon;

	public Bitmap getIcon() {
		return icon;
	}

	public void downloadIcon() {
		if (null != user) {
			this.icon = Utilities.downloadFile(user.getProfileImageURL());
		} else {
			Log.d(TAG, "User not authenticated, skipping icon download");
		}
	}

	private Settings(SharedPreferences prefs) {
		user = null;
		access = null;
		refresh = 5;
		searches.add("nook");
		searches.add("twook");

		this.mPrefs = prefs;

		String token = mPrefs.getString("token", null);
		String tokenSecret = mPrefs.getString("tokenSecret", null);
		if (token != null && tokenSecret != null) {
			// this will also update the user info, including the icon
			setAccessToken(new AccessToken(token, tokenSecret));
			Log.d(TAG, "Loaded " + user + " (" + access + ")");
		}
	};

	public static synchronized Settings getSettings(SharedPreferences prefs) {
		Log.d(TAG, "Retrieving settings");
		if (null == settings) {
			Settings.settings = new Settings(prefs);
		}
		Log.d(TAG, "Done with settings");
		return Settings.settings;
	}

	public static synchronized Settings getSettings(Context ctx) {
		Log.d(TAG, "Getting prefs from context " + ctx);
		if (null == Settings.settings) {
			SharedPreferences prefs = ctx.getSharedPreferences(PREFSTRING,
					Context.MODE_PRIVATE);
			return getSettings(prefs);
		}
		return Settings.settings;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void save() {
		SharedPreferences.Editor edit = mPrefs.edit();

		if (access != null) {
			edit.putInt("userId", this.access.getUserId());
			edit.putString("token", this.access.getToken());
			edit.putString("tokenSecret", this.access.getTokenSecret());
		}

		edit.commit();
		Log.d(TAG, "Saved " + user + " (" + access + ")");
	}

	public void setAccessToken(AccessToken accessToken) {
		this.access = accessToken;

		// fill out any info, like username and user id
		Twitter twitter = getConnection();
		try {
			this.user = twitter.verifyCredentials();
			this.access = twitter.getOAuthAccessToken();
			// Log.d(TAG, "Access after verify: " + this.access);
			// Log.d(TAG, "User after verify: " + user);
			downloadIcon();
		} catch (TwitterException e) {
			Log.e(TAG, "Access credentials did not verify: " + e.getMessage());
			this.access = null;
		}
	}

	public int getUserId() {
		if (null != user) {
			return user.getId();
		} else {
			return -1;
		}
	}

	public String getUserName() {
		if (null != user) {
			return user.getScreenName();
		} else {
			return null;
		}
	}

	public AccessToken getAccessToken() {
		return access;
	}

	public Twitter getConnection() {
		Configuration conf = getConfiguration().build();
		OAuthAuthorization auth = new OAuthAuthorization(conf);
		if (access != null) {
			// already authorized
			auth.setOAuthAccessToken(access);
		}
		Twitter twitter = new TwitterFactory().getInstance(auth);
		return twitter;
	}

	/*
	 * If we want to add additional configuration options, call this method
	 * instead of the one above. For instance, the username and password is set
	 * in the configuration (as best as I can tell), so during initial
	 * authorization, you have to get the config directly before instantiating
	 * the Auth object.
	 */
	public ConfigurationBuilder getConfiguration() {
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		confBuilder.setOAuthConsumerKey(consumerKey);
		confBuilder.setOAuthConsumerSecret(consumerSecret);

		return confBuilder;
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

}
