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

/**
 * This the package containing the main classes for the Twook
 * application. All the Activity classes displaying a list of tweets
 * inherit from TimelineActivity. To implement a custom timeline three
 * requirements must be met 
 *        
 *        <ul>
 *        <li>{@link TimelineActivity#getTweets()} must
 *        be implemented</li>
 *        <li> set the NAME  in the onCreate method for the title to be set
 *        </li>
 *        <li>call {@link TimelineActivity#updateView(String)} to display the
 *        tweets</li>
 *        <li>define a button in TimelineActivity and redefine it in the custom
 *        timeline class</li>
 *        </ul>
 *        This is an example of custom timeline class which displays the tweets
 *        of the user.
 * 
 *        <pre>
 * public class PersonalTimelineActivity extends TimelineActivity {
 * 
 *     &#064;Override
 *     public void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         //set the title at the top of the eink screen 
 *         NAME = "Personal Timeline";
 *         updateView(&quot;Let's see what you said...&quot;);
 *     }
 * 
 *     &#064;Override
 *     protected List&lt;Tweet&gt; getTweets() {
 *         Settings settings = Settings.getSettings();
 *         Twitter twitter =
 *                 new TwitterFactory().getInstance(settings.getUsername(),
 *                         settings.getPassword());
 *         List&lt;Status&gt; statuses;
 *         try {
 *             statuses = twitter.getUserTimeline();
 *             return statusToTweets(statuses);
 * 
 *         } catch (TwitterException e) {
 *             e.printStackTrace();
 *             return Collections.EMPTY_LIST;
 *         }
 *     }
 * 
 *     &#064;Override
 *     protected void createListeners() {
 *         btn_personal_timeline.setOnClickListener(new OnClickListener() {
 * 
 *             &#064;Override
 *             public void onClick(View v) {
 *                 updateView(&quot;Let's see what you said...&quot;);
 *                 Log.d(this.getClass().getName(),
 *                         &quot;Personal timeline button clicked&quot;);
 * 
 *             }
 *         });
 * 
 *     }
 * }
 * </pre>
 * 
 *        In the TimelineActivity we need to add:
 * 
 *        <pre>
 * abstract public class TimelineActivity extends nookBaseActivity {
 *      ...
 *     protected ImageButton btn_personal_timeline =
 *      (ImageButton) findViewById(R.id.personalTimeline);
 *      ...
 *     protected void createDefaultListeners() {
 *         .....
 *         btn_personal_timeline =
 *                 (ImageButton) findViewById(R.id.personalTimeline);
 *                         btn_personal_timeline.setOnClickListener(
 *                         new OnClickListener() {
 * 
 *        &#064;Override
 *        public void onClick(View v) {
 *                 Intent settingsIntent =
 *                         new Intent(v.getContext(),
 *                                 PersonalTimelineActivity.class);
 *                 startActivity(settingsIntent);
 *                 Log.d(this.getClass().getName(),
 *                         &quot;Personal timeline button clicked&quot;);
 * 
 *             }
 *         });
 *         Log.d(this.getClass().getName(),
 *                 &quot;Personal timeline button listener set&quot;);
 *         .....
 *     
 *     }
 * }
 * </pre>
 * 
 * 
 * To use the above we need to define a button
 *  with the name "personal_timeline" somwhere in the layout XML.
 */
package com.nookdevs.twook.activities;

