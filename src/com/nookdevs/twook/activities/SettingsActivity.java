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

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.nookdevs.common.nookBaseSimpleActivity;

/**
 * Activity that prompts the user to enter a username and password.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * @see Settings
 */
public class SettingsActivity extends nookBaseSimpleActivity {
	private final static String TAG = SettingsActivity.class.getName();
	private TextListener credentialsListener = new TextListener(this);

	private EditText txtPin;

	private OAuthAuthorization mAuth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the title at the top of the eink screen
		final Resources res = getResources();
		NAME = res.getText(R.string.app_name).toString()
				+ res.getText(R.string.title_separator).toString()
				+ res.getText(R.string.settings_title).toString();

		setContentView(R.layout.settings);
		final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		// get the buttons
		txtPin = (EditText) findViewById(R.id.pin);
		// set listeners
		txtPin.setOnKeyListener(credentialsListener);

		WebView engine = (WebView) findViewById(R.id.web_frame);
		engine.setOnKeyListener(credentialsListener);
		// the submit is done through jscript, so we'd better allow it...
		engine.getSettings().setJavaScriptEnabled(true);

		// set credentials from Settings
		Settings settings = Settings.getSettings(this);

		ConfigurationBuilder confBuilder = settings.getConfiguration();

		// this doesn't seem to be used by the authorization object...
		// confBuilder.setUser(username);
		// confBuilder.setPassword(password);

		Configuration conf = confBuilder.build();
		mAuth = new OAuthAuthorization(conf);
		TextView validation = (TextView) findViewById(R.id.validation);

		try {
			// ask for the request token first, so we can ask the user for
			// authorization
			RequestToken requestToken = mAuth.getOAuthRequestToken();

			Log.d(TAG, "Request token: " + requestToken.toString());
			// this includes the oauth_token parameter and everything
			Log.d(TAG, "Sending user to " + requestToken.getAuthorizationURL());

			engine.loadUrl(requestToken.getAuthorizationURL());
			/*
			 * Intent myIntent = new Intent(Intent.ACTION_VIEW,
			 * Uri.parse(requestToken.getAuthorizationURL()));
			 * startActivity(myIntent);
			 */

			Log.d(TAG, "Will authorize to " + conf.getOAuthAccessTokenURL());
		} catch (TwitterException excep) {
			// TODO: better validation here
			validation.setText("Unknown error: " + excep.getMessage());
		}
		// spiner for selecting refresh times
		// Spinner refresh = (Spinner) findViewById(R.id.refresh_time);
		// ArrayAdapter adapter = ArrayAdapter.createFromResource(
		// this, R.array.refresh_times, android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// refresh.setAdapter(adapter);
	}

	/**
	 * Helper method used for processing events recieved from the soft keyboard.
	 * 
	 * @param keyCode
	 *            the keyCode received from the soft keyboard
	 * @see nookBaseSimpleActivity
	 */
	private void processCmd(int keyCode) {
		final String pin = txtPin.getText().toString();

		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		Settings settings = Settings.getSettings(this);

		switch (keyCode) {
		case nookBaseSimpleActivity.SOFT_KEYBOARD_SUBMIT:
			// if submit is pressed check if it is valid,
			// post a message if it is not or
			// set the settings and continue if it is
			// String auth = BasicAuthorization(username, password);
			TextView validation = (TextView) findViewById(R.id.validation);

			try {
				// we can specify the username and password here, but we'd need
				// to allow xauth
				// for this client, which requires an email to api@twitter.com.
				// Since we know
				// we have a browser available, launch that and let the user
				// authenticate there.
				AccessToken accessToken = mAuth.getOAuthAccessToken(pin);

				Log.d(TAG, "Auth result is: " + accessToken);

				// where do we set username/password?
				// accessToken includes username, userId, and auth tokens
				settings.setAccessToken(accessToken);

				settings.save();

				finish();

			} catch (TwitterException excep) {
				// TODO: resource-ize strings
				if (401 == excep.getStatusCode()) {
					validation.setText("Twitter rejected us: "
							+ excep.getMessage());
				} else {
					// TODO: better validation here
					validation.setText("Unknown error: " + excep.getMessage());
				}
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			} catch (IllegalStateException excep) {
				Log.e(TAG, "Internal error: " + excep.getMessage());
				validation
						.setText("Oops, something bad happened... Check the logs!");
			}

			break;
		case nookBaseSimpleActivity.SOFT_KEYBOARD_CANCEL:
			Log.d(TAG, "Token is: " + settings.getAccessToken());
			finish();
			break;
		case nookBaseSimpleActivity.NOOK_PAGE_DOWN_KEY_LEFT:
		case nookBaseSimpleActivity.NOOK_PAGE_DOWN_KEY_RIGHT:
		case nookBaseSimpleActivity.NOOK_PAGE_UP_KEY_LEFT:
		case nookBaseSimpleActivity.NOOK_PAGE_UP_KEY_RIGHT:
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			break;
		}

	}

	static class TextListener implements OnKeyListener {
		private SettingsActivity settings;

		public TextListener(SettingsActivity settings) {
			this.settings = settings;
		}

		public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
			if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
				Log.d(TAG, "Received keycode: " + keyCode);

				switch (keyCode) {
				case nookBaseSimpleActivity.SOFT_KEYBOARD_CLEAR:
					if (view instanceof EditText) {
						EditText editTxt = (EditText) view;
						editTxt.setText("");
					}
					break;
				case nookBaseSimpleActivity.SOFT_KEYBOARD_SUBMIT:
					if (view instanceof EditText) {
						// we only want to do this fake form submit when the
						// user has
						// entered the pin
						settings.processCmd(keyCode);
					} else if (view instanceof WebView) {
						WebView engine = (WebView) view;
						KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,
								KeyEvent.KEYCODE_DPAD_CENTER);
						engine.onKeyDown(KeyEvent.KEYCODE_DPAD_CENTER, event);
						event = new KeyEvent(KeyEvent.ACTION_UP,
								KeyEvent.KEYCODE_DPAD_CENTER);
						engine.onKeyUp(KeyEvent.KEYCODE_DPAD_CENTER, event);
						engine.dispatchKeyEvent(event);

						// View v = engine.findFocus();
						// v.performClick();
						// Log.d(TAG, "Focused: " + v.toString());

						// Rect r = new Rect();
						// engine.getFocusedRect(r);
						// Log.d(TAG, "FocusedRect: " + r.toString());
						// MotionEvent ev = MotionEvent.obtain(1, 1,
						// MotionEvent.ACTION_UP, r.centerX(), r.centerY(), 0);
						// engine.dispatchTouchEvent(ev);
						// engine.performClick();
					}
					break;
				case nookBaseSimpleActivity.SOFT_KEYBOARD_CANCEL:
				case nookBaseSimpleActivity.NOOK_PAGE_DOWN_KEY_LEFT:
				case nookBaseSimpleActivity.NOOK_PAGE_DOWN_KEY_RIGHT:
				case nookBaseSimpleActivity.NOOK_PAGE_UP_KEY_LEFT:
				case nookBaseSimpleActivity.NOOK_PAGE_UP_KEY_RIGHT:
					// always interpret these as special
					// TODO: translate page turn buttons to soft-key navigation
					// to navigate
					// through web frame using dispatchKeyEvent
					settings.processCmd(keyCode);
					break;
				}
			}
			return false;
		}
	}
}