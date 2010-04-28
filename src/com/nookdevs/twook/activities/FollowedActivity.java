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
import android.view.View;
import android.view.View.OnClickListener;

import com.nookdevs.twook.services.FollowedDownloaderService;

/**
 * 
 * Activity that displays the latest statuses of the users followed by the
 * authenticated user. Displays the latest 100 users followed.
 * 
 * @author=Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see TimelineActivity
 * 
 */
public class FollowedActivity extends TimelineActivity {
    /*LOG COMMENT  private static final String TAG = FollowedActivity.class.getName();  LOG COMMENT*/
    FollowedDownloaderService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// set the title at the top of the eink screen
	Resources res = getResources();
	NAME = res.getText(R.string.app_name).toString()
		+ res.getText(R.string.title_separator).toString()
		+ res.getText(R.string.followed_timeline).toString();
    }

    @Override
    protected void createListeners() {
	btn_followed.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		service.doDownload();
		/*LOG COMMENT  Log.d(TAG, "Followed button clicked");  LOG COMMENT*/

	    }
	});

    }

    @Override
    protected void stopDownloadService() {
	/*LOG COMMENT  Log.d(TAG, "Trying to stop service....");  LOG COMMENT*/
	service.doCleanup();
    }

    @Override
    protected void setDownloadService() {
	service = new FollowedDownloaderService();
	intent = new Intent(this, FollowedDownloaderService.class);
	startService(intent);
	service.setMainActivity(this);
	service.startDownload();

    }
}