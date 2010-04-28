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
import android.view.View;
import android.view.View.OnClickListener;

import com.nookdevs.twook.services.RepliesDownloaderService;

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

public class RepliesTimelineActivity extends TimelineActivity {
    private final static String TAG = RepliesTimelineActivity.class.getName();

    RepliesDownloaderService service;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// set the title at the top of the eink screen
	Resources res = getResources();
	NAME = res.getText(R.string.app_name).toString()
		+ res.getText(R.string.title_separator).toString()
		+ res.getText(R.string.replies_timeline).toString();
    }

    @Override
    protected void createListeners() {
	btn_replies_timeline.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		service.doDownload();
		Log.d(TAG,
			"Replies timeline button clicked");

	    }
	});

    }

    @Override
    protected void stopDownloadService() {
	Log.d(TAG, "Trying to stop service....");
	service.doCleanup();
    }

    @Override
    protected void setDownloadService() {
	service = new RepliesDownloaderService();
	intent = new Intent(this, RepliesDownloaderService.class);
	startService(intent);
	service.setMainActivity(this);
	service.startDownload();

    }
}