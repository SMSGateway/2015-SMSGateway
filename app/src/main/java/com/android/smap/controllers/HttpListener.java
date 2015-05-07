package com.android.smap.controllers;

/**
 * Created by kai on 23/04/2015.
 */
public interface HttpListener<T> {
    void onResponse(T response);
}
