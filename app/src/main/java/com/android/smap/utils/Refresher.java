package com.android.smap.utils;

import android.os.Handler;

public class Refresher implements Runnable {

    public interface RefreshListener {

        public void onRefresh();
    }

    private int mInterval;

    private final Handler mHandler;

    private final RefreshListener mListener;

    /**
     * @param refreshInterval
     *            The interval to refresh, in milliseconds.
     * @param listener
     *            The listener that's called when the refresh time is completed.
     */
    public Refresher(int refreshInterval, RefreshListener listener) {
        if (refreshInterval <= 0) {
            throw new IllegalArgumentException();
        }

        if (listener == null) {
            throw new IllegalArgumentException();
        }

        mInterval = refreshInterval;
        mListener = listener;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        mListener.onRefresh();
        start();
    }

    public void start() {
        mHandler.postDelayed(this, mInterval);
    }

    public void stop() {
        mHandler.removeCallbacks(this);
    }

    public void reset() {
        stop();
        start();
    }

    public void setRefreshInterval(int time) {
        if (time <= 0) {
            throw new IllegalArgumentException();
        }

        mInterval = time;
        reset();
    }

    public int getRefreshInterval() {
        return mInterval;
    }
}
