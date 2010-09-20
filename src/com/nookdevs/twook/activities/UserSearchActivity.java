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

import android.content.Intent;
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
import com.nookdevs.twook.services.UserSearchService;

/**
 * Activity that allows to search for users.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.4
 * @since 0.0.4
 * @see TimelineActivity
 */

public class UserSearchActivity extends TimelineActivity {
	/** Class name used in logging statements */
	private static final String TAG = UserSearchActivity.class.getName();
	private static final String SEARCH_BUTTON_MESSAGE = "Find users button clicked (start search mode)";

	private TextEditListener softKeyListener = new TextEditListener(this);
	private ListListener mlistListener = new ListListener(this);

	private EditText textSearch;

	private String searchTerm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		btn_search_users = (ImageButton) findViewById(R.id.search1);

		// this is needed because we load a different layout
		// so the listeners have to be reinitialized
		createListeners();
		// set the title at the top of the eink screen
		final Resources res = getResources();
		NAME = res.getText(R.string.app_name).toString()
				+ res.getText(R.string.title_separator).toString()
				+ res.getText(R.string.search_users).toString();
		final ListView list = (ListView) findViewById(android.R.id.list);
		list.requestFocus();
		list.setOnKeyListener(mlistListener);
		textSearch = (EditText) findViewById(R.id.search_term);
		textSearch.setOnKeyListener(softKeyListener);
		updateIcon();
	}

	@Override
	protected void createListeners() {
		super.createDefaultListeners();
		btn_search_users.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textSearch.setText(searchTerm);
				service.doDownload();
				Log.d(TAG, SEARCH_BUTTON_MESSAGE);

			}
		});

		btn_search_users.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {
				textSearch.setText(searchTerm);
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
		// retain a reference to our parent for future use
		@SuppressWarnings("unused")
		private UserSearchActivity settings;

		public TextEditListener(UserSearchActivity settings) {
			this.settings = settings;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnKeyListener#onKey(android.view.View, int,
		 * android.view.KeyEvent)
		 */
		public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
			Log.d(TAG, "Received keycode: " + keyCode);
			if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
				if (view instanceof EditText) {
					final EditText editTxt = (EditText) view;
					if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_CLEAR) {
						editTxt.setText("");
					}
					// Clear
					if (keyCode == nookBaseSimpleActivity.SOFT_KEYBOARD_SUBMIT) {
						searchTerm = editTxt.getText().toString();
						service.doDownload();
					}

				}
			}
			return true;
		}
	}

	@Override
	protected void setDownloadService() {
		service = new UserSearchService();
		service.setMainActivity(this);
		intent = new Intent(this, UserSearchService.class);
		startService(intent);
		service.setMainActivity(this);
		service.startDownload();
	}

	public String getSearchTerm() {
		return searchTerm;
	}

}