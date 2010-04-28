package com.nookdevs.twook.utilities;

import android.graphics.Bitmap;

public interface ImageCache {

    /**
     * Gets the user icon.
     * @param username
     * @return the user icon
     */
    public Bitmap getImage(String username);
    /**
     * Inserts the user icon in the cache if the username
     * does not exist. 
     * 
     * @param username
     * @param icon
     */
    public void insertImage(String username, Bitmap icon);
    
    /**
     * Checks if the image linked to the username is cached. 
     * @param username
     * @return true if the image is cached, false otherwise
     */
    public boolean isCached(String username);
}
