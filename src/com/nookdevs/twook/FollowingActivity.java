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


import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * Activity that displays the latest statuses of the users
 * followed by the authenticated user.  Displays the latest 100
 * users followed.
 *  
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see TimelineActivity
 * 
 */
public class FollowingActivity extends TimelineActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		updateView("Retrieving followed users...");
	}
	@Override
	protected List<Tweet> getTweets() {
		Settings settings = Settings.getSettings();
		//set the title at the top of the eink screen 
		Resources res = getResources();
		NAME = res.getText(R.string.app_name).toString()
		+ res.getText(R.string.title_separator).toString()
		+ res.getText(R.string.followed_timeline).toString();

		Twitter twitter = new TwitterFactory().getInstance(settings.getUsername(),
				settings.getPassword());
	    List<Status> statuses = new ArrayList<Status>();
		try {
			 PagableResponseList<User> following = twitter.getFriendsStatuses();
			 
			 for (User user : following) {
				 statuses.add(user.getStatus());
			}
		    return statusToTweets(statuses);

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void createListeners() {
		btn_followed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateView("Retrieving followed users...");
				Log.d(this.getClass().getName(),"Followed button clicked");
				
			}
		});
		
	}
}