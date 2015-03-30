package com.android.smap.controllers;

import android.content.Context;

import com.android.smap.GatewayApp;
import com.android.smap.api.models.validator.ModelValidator;
import com.android.smap.api.requests.ApiRequest;
import com.android.smap.utils.Refresher;
import com.android.smap.utils.Refresher.RefreshListener;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * Base controller for fetching data and refreshing it. This is a core class.
 * Drink it up.
 * 
 * @author matt witherow
 * @param <T>
 */
public abstract class UIRequestController<T> implements
		Controller,
		Listener<T>,
		ErrorListener,
		RefreshListener {

	private static final int			DEFAULT_REFRESH_INTERVAL	= 30000;					// 30
																								// seconds
	protected ControllerListener		mListener;
	protected ControllerErrorListener	mErrorListener;
	private final Context				mContext;
	private T							mModel;
	private Refresher					mRefresher;
	private int							mAutoRefreshInterval		= DEFAULT_REFRESH_INTERVAL;

	protected abstract ApiRequest<?> getRequest();

	public UIRequestController(Context context, ControllerListener listener,
			ControllerErrorListener errorListener) {

		if (listener == null || errorListener == null || context == null) {
			throw new IllegalArgumentException();
		}

		mListener = listener;
		mErrorListener = errorListener;
		mContext = context;
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
		ApiRequest<?> request = getRequest();
		request.setTag(this);
		GatewayApp.getRequestQueue().add(request);
	}

	/**
	 * Cancels all requests in the {@link RequestQueue} tagged with this
	 * controller. Also stops the refresh handler if one was running.
	 */
	public void cancel() {
		setRefreshEnabled(false);
		GatewayApp.getRequestQueue().cancelAll(this);
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
