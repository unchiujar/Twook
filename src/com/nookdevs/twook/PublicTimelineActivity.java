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


import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
/**
 * 
 * Activity that displays the public timeline. 
 *  
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see TimelineActivity
 * 
 */

public class PublicTimelineActivity extends TimelineActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		updateView("Retrieving public timeline");
	}
	@Override
	protected List<Status> getTweets() {
		Twitter twitter = new Twitter();
		return twitter.getPublicTimeline();
	}
	@Override
	protected void createListeners() {
		btn_public_timeline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateView("Retrieving public timeline");
				Log.d(this.getClass().getName(),"Public timeline button clicked");	
			}
		});
		
	}
}