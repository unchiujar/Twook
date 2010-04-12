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

import android.graphics.Bitmap;

/**
 * 
 * Very simple wrapper for a tweet. Contains the message and the
 * user icon.  
 *  
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * @version 0.0.2
 * @since 0.0.2
 * 
 * @see TimelineActivity
 * 
 */
public class Tweet {
	private String message;
	private Bitmap image;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
