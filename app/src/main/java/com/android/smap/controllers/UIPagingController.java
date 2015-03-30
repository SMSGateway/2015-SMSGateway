package com.android.smap.controllers;

import android.content.Context;

import com.android.smap.api.ApiConstants;
import com.android.smap.api.requests.ApiRequest;
import com.android.volley.VolleyError;

/**
 * This controller provides the base logic for paging. Intended to be used with
 * VelocAdapter to display an endless list of incoming traffic updates
 * 
 * @param <T>
 *            Clean model type of each page item.
 * @param <V>
 *            Dirty raw model from feed of underlying concrete controller
 * @author matt witherow
 */

public abstract class UIPagingController<T, V> extends UIRequestController<V> {

	public interface PagingStatusIndicator {
		public void noMorePages();

		public void paging(boolean isPaging);
	}

	protected enum ControllerState {
		ERROR,
		FREE,
		BUSY
	}

	/**
	 * From the newly requested page of the dirty model, we want only new items
	 * (no duplicates). Save these new items as clean models to be appended to
	 * the end of the list. We update the dirty model (super.setModel) to be the
	 * current page (we dont append to create a massive dirty model that is
	 * tiresome to sanitze)
	 * 
	 * @param Dirty
	 *            model of concrete controller
	 */
	protected abstract void appendNewPageItems();

	public static final int			PAGE_HEADSTART	= 3;
	private int						mPageSize		= ApiConstants.DEFAULT_PAGE_SIZE;
	private int						mPageNumber		= 1;
	private PagingStatusIndicator	mPagingIndicator;
	private ControllerState			mState			= ControllerState.FREE;
	private boolean					mMorePages		= true;

	public UIPagingController(Context context, ControllerListener listener,
			ControllerErrorListener errorListener,
			PagingStatusIndicator indicator) {
		super(context, listener, errorListener);
		mPagingIndicator = indicator;
		mState = ControllerState.FREE;
	}

	/**
	 * This method provides the page request
	 * 
	 * @param pageSize
	 *            size of each page
	 * @param page
	 *            current page number to fetch
	 * @return
	 */
	protected abstract ApiRequest<?> getPageRequest(int pageSize, int page);

	@Override
	protected ApiRequest<?> getRequest() {
		return getPageRequest(mPageSize, mPageNumber);
	}

	@Override
	public void setModel(V model) {
		super.setModel(model);
		appendNewPageItems();
		mPagingIndicator.paging(setBusy(false));
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		super.onErrorResponse(error);
		mState = ControllerState.ERROR;
	}

	/**
	 * This method is used to check if the controller is not busy and there are
	 * more pages left to get
	 * 
	 * @return
	 */
	public boolean canPage() {
		return (mMorePages && !isBusy());
	}

	/**
	 * This method is used to check if the controller is currently fetching new
	 * data
	 * 
	 * @return
	 */
	public boolean isBusy() {
		return (mState == ControllerState.BUSY);
	}

	private boolean setBusy(boolean busy) {

		mState = busy ? ControllerState.BUSY : ControllerState.FREE;
		return busy;
	}

	/**
	 * Starts a new page request, and notifies the controller's callback when
	 * completed. Tells a paging delegate that it is busy paging.
	 */
	public void performPageRequest() {
		if (canPage()) {
			incrementPageNumber();
			mPagingIndicator.paging(setBusy(true));
			super.start();
		}
	}

	/**
	 * Sets whether or not the controller has reached the last page
	 * 
	 * @param atEnd
	 */
	public void setAtEnd() {
		mMorePages = false;
		mPagingIndicator.paging(setBusy(false));
		if (mPagingIndicator != null) {
			mPagingIndicator.noMorePages();
		}
	}

	public void setPageSize(int size) {
		this.mPageSize = size;
	}

	public int getPageSize() {
		return mPageSize;
	}

	public int getPage() {
		return mPageNumber;
	}

	public void incrementPageNumber() {
		++mPageNumber;
	}

	public void resetPager() {
		mPageNumber = 1;
		mMorePages = true;
		mPagingIndicator.paging(setBusy(false));

	}

	/**
	 * Call this when network requests have been canceled to update the
	 * controller's state
	 */
	public void onStop() {
		setBusy(false);
	}

}
