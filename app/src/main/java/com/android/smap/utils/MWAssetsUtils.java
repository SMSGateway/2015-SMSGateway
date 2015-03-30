package com.android.smap.utils;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;

public class MWAssetsUtils {
    private static final HashMap<String, Typeface> mCachedFonts = new HashMap<String, Typeface>();



    /**
     * Returns typeface font from assets. If cached returns the cached value, otherwise loads from
     * assets and caches it.
     * 
     * @param context
     * @param fontName
     * @return
     */
    public static final Typeface getCachedFont(Context context, String fontName) {
        Typeface tf = null;
        Context appContext = context.getApplicationContext();
        try {
            // check if the font is cached
            if (mCachedFonts.containsKey(fontName) && mCachedFonts.get(fontName) != null) {
                tf = mCachedFonts.get(fontName);
            } else {
                // get the font from assets
                tf = Typeface.createFromAsset(appContext.getAssets(), "fonts/" + fontName);
                // cache the font
                mCachedFonts.put(fontName, tf);
            }
        } catch (Exception e) {
        }
        return tf;
    }
}
