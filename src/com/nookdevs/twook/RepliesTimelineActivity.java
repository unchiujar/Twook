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

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		updateView("Retrieving replies timeline");
		//set the title at the top of the eink screen 
		Resources res = getResources();
		NAME = res.getText(R.string.app_name).toString()
				+ res.getText(R.string.title_separator).toString()
				+ res.getText(R.string.replies_timeline).toString();
	}
	@Override
	protected List<Tweet> getTweets() {
		Settings settings = Settings.getSettings();
		Twitter twitter = new TwitterFactory().getInstance(settings.getUsername(),
				settings.getPassword());
	    List<Status> statuses;
		try {
			statuses = twitter.getMentions();
		    return statusToTweets(statuses);

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void createListeners() {
		btn_replies_timeline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateView("Retrieving replies timeline");
				Log.d(this.getClass().getName(),"Replies timeline button clicked");
				
			}
		});
		
	}
}