package com.android.smap.controllers;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.android.smap.GatewayApp;
import com.android.smap.api.requests.HttpUrlRequest;
import com.android.smap.utils.Refresher;
import com.android.smap.utils.Refresher.RefreshListener;

public abstract class RawRequestController<T extends Model> extends RequestController implements
        Controller,
        HttpListener<String>,
        HttpErrorListener,
        RefreshListener {

    private static final int DEFAULT_REFRESH_INTERVAL = 30000;// 30 seconds
    protected ControllerListener mListener;
    protected ControllerErrorListener mErrorListener;
    private int mAutoRefreshInterval = DEFAULT_REFRESH_INTERVAL;
    private Refresher mRefresher;
    private T mModel;

    protected abstract HttpUrlRequest getHttpUrlRequest();

    protected abstract T addResponseToDatabase(String rawXML);

    public RawRequestController(Context context, ControllerListener listener,
                                ControllerErrorListener errorListener) {

        super(context);
        if (listener == null || errorListener == null || context == null) {
            throw new IllegalArgumentException();
        }

        mListener = listener;
        mErrorListener = errorListener;
        mModel = null;
    }

    @Override
    public void onResponse(String response) {

        /** USE ROSA TO SAVE TO DB */

        /** RETURN SAVED OBJECT */
        T model = null;
        ActiveAndroid.beginTransaction();
        try {

            model = addResponseToDatabase(response);
            ActiveAndroid.setTransactionSuccessful();
            setModel(model);

        } finally {
            ActiveAndroid.endTransaction();
            getControllerListener().onControllerResult();

        }
    }

    @Override
    public void onErrorResponse(NetworkError networkError) {
        getControllerErrorListener().onControllerError(networkError);
    }

    @Override
    public void start() {
        getHttpUrlRequest().executeRequest();
    }

    /**
     *
     */
    @Override
    public void cancel() {
        setRefreshEnabled(false);
    }

    /**
     * Set AutoRefresh on for the controller. User overloaded method to specify
     * a custom time interval. If you disable a non-existent refresh handler an
     * item will not be superfluously created
     *
     * @param enabled
     */
    public void setRefreshEnabled(boolean enabled) {
        if (enabled && GatewayApp.getPreferenceWrapper().isAutoRefreshEnabled()) {
            getRefresher().reset();
        } else if (!enabled && mRefresher != null) {
            getRefresher().stop();
        }
    }

    public void setRefreshEnabled(boolean enabled, int interval) {
        mAutoRefreshInterval = interval;
        setRefreshEnabled(enabled);
    }

    private Refresher getRefresher() {
        if (mRefresher == null) {
            mRefresher = new Refresher(mAutoRefreshInterval, this);
        }
        return mRefresher;
    }

    /**
     * Callback from refresh handler. We simply restart our data fetching
     * process
     */
    public void onRefresh() {
        start();
    }

    public void setModel(T response) {
        this.mModel = response;
    }

    public T getModel() {
        return mModel;
    }

    public ControllerErrorListener getControllerErrorListener() {
        return mErrorListener;
    }

    @Override
    public ControllerListener getControllerListener() {
        return mListener;
    }

}
