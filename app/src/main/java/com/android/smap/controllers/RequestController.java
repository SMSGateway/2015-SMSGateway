package com.android.smap.controllers;

import android.content.Context;

/**
 * Created by kai on 24/04/2015.
 */
public abstract class RequestController {

    private final Context mContext;

    protected RequestController(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }
}

