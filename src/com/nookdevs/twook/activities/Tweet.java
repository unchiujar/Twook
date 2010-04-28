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

import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;

/**
 * Very simple wrapper for a tweet. Contains the message and the user icon.
 * 
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * @see TimelineActivity
 */
public class Tweet {
    /*LOG COMMENT private final static String TAG = Tweet.class.getName(); LOG COMMENT*/
    /** The status message. */
    private String message;
    /** The status icon of the user  */
    private Bitmap image;
    /** The name of the user who posted this tweet. */
    private String username;

    private URL imageURL;
    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }
    
    public void setImageURL(String imageURL)  {
        try {
	    this.imageURL = new URL(imageURL);
	} catch (MalformedURLException e) {
	    /*LOG COMMENT  Log.e(TAG, e.getMessage());  LOG COMMENT*/
	}
    }

    /**
     * @return The name of the user who posted this tweet.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the name of the user that posted this tweet
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the image of the user who posted the tweet
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * @param image the image of the user who posted the tweet
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * @return the status message (tweet)
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
