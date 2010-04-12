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

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
 * Activity that prompts the user to enter a username and password.
 * 
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see Settings
 * 
 */
public class SettingsActivity extends nookBaseSimpleActivity {

	private TextListener credentialsListener = new TextListener(this);
	/** Holds the messages displayed when the entered credentials are invalid. */
	CharSequence[] invalid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NAME = "Twook";

		Resources res = getResources();
		invalid = res.getTextArray(R.array.invalid_credentials);
		setContentView(R.layout.settings);
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.password);
		username.setOnKeyListener(credentialsListener);
		password.setOnKeyListener(credentialsListener);

		Settings settings = Settings.getSettings();
		username.setText(settings.getUsername());
		password.setText(settings.getPassword());
	}

	/**
	 * Helper method used for processing events recieved from the soft keyboard.
	 * 
	 * @param keyCode
	 *            the keyCode received from the soft keyboard
	 * @see nookBaseSimpleActivity
	 */
	private void processCmd(int keyCode) {
		EditText txtUsername = (EditText) findViewById(R.id.username);
		EditText txtPassword = (EditText) findViewById(R.id.password);
		String username = txtUsername.getText().toString();
		String password = txtPassword.getText().toString();
		switch (keyCode) {
		case SOFT_KEYBOARD_SUBMIT: {
			// if submit is pressed check if it is valid,
			// post a message if it is not or
			// set the settings and continue if it is
			// String auth = BasicAuthorization(username, password);
			TextView validation = (TextView) findViewById(R.id.validation);
			Twitter twitter = new TwitterFactory().getInstance(username,
					password);
			try {
				twitter.verifyCredentials();

				Settings settings = Settings.getSettings();

				settings.setUsername(username);
				settings.setPassword(password);

				Log.d(this.getClass().getName(), "Username is: "
						+ settings.getUsername());
				Log.d(this.getClass().getName(), "Password is: "
						+ settings.getPassword());

				finish();

			} catch (TwitterException excep) {
				validation.setText(selectRandomMessage());
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

			}

			break;
		}
		case SOFT_KEYBOARD_CANCEL: {
			Settings settings = Settings.getSettings();
			Log.d(this.getClass().getName(), "Username is: "
					+ settings.getUsername());
			Log.d(this.getClass().getName(), "Password is: "
					+ settings.getPassword());
			finish();
			break;
		}
		}

	}

	class TextListener implements OnKeyListener {
		private SettingsActivity settings;

		public TextListener(SettingsActivity settings) {
			this.settings = settings;
		}

		public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
			Log.d(this.getClass().getName(), "Received keycode: " + keyCode);

			if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
				if (view instanceof EditText) {
					EditText editTxt = (EditText) view;
					// - no idea why - this is what is returned in the emulator
					// when
					// I press these keys
					if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_CLEAR) { // Clear
						// button?
						editTxt.setText("");
					} else {
						settings.processCmd(keyCode);
					}
				}
			}
			return false;
		}
	}

	/**
	 * Helper method for selecting a random error message when invalid
	 * credentials are entered.
	 * 
	 * @return a random message
	 */
	private CharSequence selectRandomMessage() {

		int index = (int) Math.round(Math.random() * invalid.length);
		return invalid[index];
	}

}