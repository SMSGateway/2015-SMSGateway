package com.android.smap.controllers;

/**
 * Created by kai on 23/04/2015.
 */
public interface HttpErrorListener {
    public String errorMessage = "Unable to retrieve web page. Please Check the server IP setting.";

    void onErrorResponse(NetworkError networkError);
}
