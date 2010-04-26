package com.nookdevs.twook.activities;

import android.graphics.Bitmap;

public class Row { 
    private Bitmap icon; 
    private String username; 

    public Row(Bitmap b, String n) { 
        icon = b; 
        username = n; 
    } 
    
    public Bitmap getBitmap() { return icon; } 
    public String getName() { return username; } 
} 