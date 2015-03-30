package com.android.smap.controllers;

import android.content.Context;

import com.android.smap.GatewayApp;
import com.android.smap.api.models.Gojo;
import com.android.smap.api.models.Status;
import com.android.smap.api.models.validator.ModelValidator;
import com.android.smap.api.requests.ApiRequest;
import com.android.smap.api.requests.GojoRequest;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * Generic 'Post the GoJo back' controller
 * 
 * @author matt witherow
 * @param <T>
 */
public class GojoController implements Controller, Listener<Status>,
		ErrorListener {

	private static final int			DEFAULT_REFRESH_INTERVAL	= 30000;	// 30
																				// seconds
	protected ControllerListener		mListener;
	protected ControllerErrorListener	mErrorListener;
	private final Context				mContext;
	private Status						mModelIn;
	private Gojo						mModelOut;

	public GojoController(Gojo model, Context context,
			ControllerListener listener, ControllerErrorListener errorListener) {

		if (listener == null || errorListener == null || context == null
				|| model == null) {
			throw new IllegalArgumentException();
		}

		mListener = listener;
		mErrorListener = errorListener;
		mContext = context;
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
			onErrorResponse(new ParseError());
			return;
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		ControllerError controllerError = new ControllerError(error, mContext);
		getControllerErrorListener().onControllerError(controllerError);
	}

	/**
	 * Adds the request to the {@link RequestQueue} and tags the request with
	 * this fragment
	 * 
	 * @param request
	 */
	public void start() {

		ApiRequest<?> request = new GojoRequest(mModelOut, this, this);
		request.setTag(this);
		GatewayApp.getRequestQueue().add(request);

	}

	/**
	 * Cancels all requests in the {@link RequestQueue} tagged with this
	 * controller.
	 */
	public void cancel() {
		GatewayApp.getRequestQueue().cancelAll(this);
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
