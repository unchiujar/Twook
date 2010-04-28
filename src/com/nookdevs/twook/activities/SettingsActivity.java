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

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nookdevs.common.nookBaseSimpleActivity;
import com.nookdevs.twook.utilities.Utilities;

/**
 * Activity that prompts the user to enter a username and password.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * @see Settings
 */
public class SettingsActivity extends nookBaseSimpleActivity {
    /*LOG COMMENT private final static String TAG = SettingsActivity.class.getName(); LOG COMMENT*/
    private TextListener credentialsListener = new TextListener(this);
    /** Holds the messages displayed when the entered credentials are invalid. */
    private CharSequence[] invalid;

    private EditText txtUsername;
    private EditText txtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// set the title at the top of the eink screen
	final Resources res = getResources();
	NAME = res.getText(R.string.app_name).toString()
		+ res.getText(R.string.title_separator).toString()
		+ res.getText(R.string.settings_title).toString();
	// get messages for invalid credentials from strings
	invalid = res.getTextArray(R.array.invalid_credentials);
	setContentView(R.layout.settings);
	final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	// get the buttons
	txtUsername = (EditText) findViewById(R.id.username);
	txtPassword = (EditText) findViewById(R.id.password);
	// set listeners
	txtUsername.setOnKeyListener(credentialsListener);
	txtPassword.setOnKeyListener(credentialsListener);
	// set credentials from Settings
	final Settings settings = Settings.getSettings();
	txtUsername.setText(settings.getUsername());
	txtPassword.setText(settings.getPassword());

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
	final String username = txtUsername.getText().toString();
	final String password = txtPassword.getText().toString();
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

		/*LOG COMMENT Log.d(TAG, "Username is: " + settings.getUsername()); LOG COMMENT*/
		/*LOG COMMENT Log.d(TAG, "Password is: " + settings.getPassword()); LOG COMMENT*/
		settings.setIcon(Utilities.downloadFile(twitter.showUser(
			username).getProfileImageURL()));
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
	    /*LOG COMMENT Log.d(TAG, "Username is: " + settings.getUsername()); LOG COMMENT*/
	    /*LOG COMMENT Log.d(TAG, "Password is: " + settings.getPassword()); LOG COMMENT*/
	    finish();
	    break;
	}
	}

    }

    static class TextListener implements OnKeyListener {
	private SettingsActivity settings;

	public TextListener(SettingsActivity settings) {
	    this.settings = settings;
	}

	public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
	    /*LOG COMMENT  Log.d(TAG, "Received keycode: " + keyCode);  LOG COMMENT*/

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