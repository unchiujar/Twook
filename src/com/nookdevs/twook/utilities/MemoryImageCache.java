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
    @SuppressWarnings("unused")
	private static final String TAG = MemoryImageCache.class.getName();
    private static final int MAX_SIZE = 300;
    private static MemoryImageCache instance;
    private Map<String, Bitmap> imageCache;

    public static MemoryImageCache getInstance() {
		if (instance == null) {
		    instance = new MemoryImageCache();
		}
		return instance;
    }

    private MemoryImageCache() {
    	imageCache = Collections.synchronizedMap(new HashMap<String, Bitmap>());
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
		//Log.d(TAG, "Cache size is :" + imageCache.size());
		if (cacheSize > MAX_SIZE) {
			/* Ideally we would kick out the least-recently-used icons,
			 * but we can just naively clear the entire cache for not
			 * that much penalty.
			 */
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
