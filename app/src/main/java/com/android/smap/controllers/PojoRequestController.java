package com.android.smap.controllers;

import android.content.Context;

import com.android.smap.GatewayApp;
import com.android.smap.api.models.validator.ModelValidator;
import com.android.smap.api.requests.HttpUrlRequest;
import com.android.smap.utils.Refresher;
import com.android.smap.utils.Refresher.RefreshListener;

/**
 * Base controller for fetching data and refreshing it. This is a core class.
 * Drink it up.
 *
 * @param <T>
 * @author matt witherow
 * @author Kai qin
 */
public abstract class PojoRequestController<T> extends RequestController implements
        Controller,
        HttpListener<T>,
        HttpErrorListener,
        RefreshListener {

    private static final int DEFAULT_REFRESH_INTERVAL = 30000;// 30 seconds
    protected ControllerListener mListener;
    protected ControllerErrorListener mErrorListener;
    private Refresher mRefresher;
    private int mAutoRefreshInterval = DEFAULT_REFRESH_INTERVAL;
    private T mModel;

    protected abstract HttpUrlRequest<?> getHttpUrlRequest();

    public PojoRequestController(Context context, ControllerListener listener,
                                 ControllerErrorListener errorListener) {
        super(context);

        if (listener == null || errorListener == null || context == null) {
            throw new IllegalArgumentException();
        }

        mListener = listener;
        mErrorListener = errorListener;
        mModel = null;
    }

    /**
     * Overwrite onReponse in the concrete controller to specify custom refresh
     * behaviour. Don't call super (you'll trip onControllerResult). validate()
     * is provided as a public util function for subclass use, use it. By
     * default, autorefresh is OFF. Enabling vanilla autorefresh will activate a
     * 30 second refresh.
     */
    @Override
    public void onResponse(T response) {
        validate(response);
        setModel(response);
        getControllerListener().onControllerResult();
    }

    public void validate(T response) {
        if (!ModelValidator.isModelValid(response)) {
            onErrorResponse(new NetworkError(1));
            return;
        }
    }

    @Override
    public void onErrorResponse(NetworkError networkError) {
        getControllerErrorListener().onControllerError(networkError);
    }

    /**
     * Start the request.
     */
    public void start() {
        getHttpUrlRequest().executeRequest();
    }

    /**
     * Cancel the request and Also stops the refresh handler if one was running.
     */
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

    /**
     * Override setModel in concrete controllers to specify how you wish the
     * model to be returned to others. This is your opportunity to santise the
     * model data. If you do not override this, you will accept what is given
     * raw from the feed
     *
     * @param model
     * @return
     */
    public void setModel(T model) {
        this.mModel = model;
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
