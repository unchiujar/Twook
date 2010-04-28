/**
 * 
 */
package com.nookdevs.twook.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * @author Vasile Jureschi <vasile.jureschi@gmail.com>
 * 
 */
public class MemoryImageCache implements ImageCache {
    /*LOG COMMENT  private static final String TAG = MemoryImageCache.class.getName();  LOG COMMENT*/
    private static final int MAX_SIZE = 300;
    private static MemoryImageCache instance;
    private Map<String, Bitmap> imageCache = Collections
	    .synchronizedMap(new HashMap<String, Bitmap>());

    public static MemoryImageCache getInstance() {
	if (instance == null) {
	    instance = new MemoryImageCache();
	}
	return instance;
    }

    private MemoryImageCache() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nookdevs.twook.ImageCache#getImage(java.lang.String)
     */
    @Override
    public Bitmap getImage(String username) {
	return imageCache.get(username);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nookdevs.twook.ImageCache#insertImage(java.lang.String,
     * android.graphics.Bitmap)
     */
    @Override
    public void insertImage(String username, Bitmap icon) {
	int cacheSize = imageCache.size();
	/*LOG COMMENT  Log.d(TAG, "Cache size is :" + imageCache.size());  LOG COMMENT*/
	if (cacheSize > MAX_SIZE) {
	    imageCache.clear();
	}
	imageCache.put(username, icon);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nookdevs.twook.ImageCache#isCached(java.lang.String)
     */
    @Override
    public boolean isCached(String username) {
	return imageCache.containsKey(username);
    }

}
