package com.android.smap.controllers;

import android.content.Context;

import com.android.smap.api.models.Gojo;
import com.android.smap.api.models.Status;
import com.android.smap.api.models.validator.ModelValidator;

/**
 * Generic 'Post the GoJo back' controller
 *
 * @author matt witherow.
 * @author kai qin.
 */
public class GojoController extends RequestController implements Controller,
        HttpListener<Status>,
        HttpErrorListener {

    private static final int DEFAULT_REFRESH_INTERVAL = 30000;// 30 seconds
    protected ControllerListener mListener;
    protected ControllerErrorListener mErrorListener;
    private Status mModelIn;
    private Gojo mModelOut;

    public GojoController(Gojo model, Context context,
                          ControllerListener listener, ControllerErrorListener errorListener) {
        super(context);

        if (listener == null || errorListener == null || context == null
                || model == null) {
            throw new IllegalArgumentException();
        }

        mListener = listener;
        mErrorListener = errorListener;
        mModelIn = null;
        mModelOut = model;
    }

    @Override
    public void onResponse(Status response) {
        validate(response);
        this.mModelIn = response;
        getControllerListener().onControllerResult();
    }

    public void validate(Status response) {
        if (!ModelValidator.isModelValid(response)) {
            onErrorResponse(new NetworkError());
            return;
        }
    }

    @Override
    public void onErrorResponse(NetworkError networkError) {
        getControllerErrorListener().onControllerError(networkError);
    }

    /**
     * Adds the request to the {@link } and tags the request with
     * this fragment
     */
    public void start() {

    }

    /**
     * Cancels all requests in the tagged with this
     * controller.
     */
    public void cancel() {
    }

    public Status getModelIn() {
        return mModelIn;
    }

    public ControllerErrorListener getControllerErrorListener() {
        return mErrorListener;
    }

    @Override
    public ControllerListener getControllerListener() {
        return mListener;
    }

}
