package com.android.smap.controllers;

import android.content.Context;
import android.util.Log;

import com.android.smap.api.models.SmapException;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Controller Errors can be passed to an Error View to be dealt with. You can
 * display the appropriate error to the user.
 * 
 * @author Matt Witherow
 * 
 */
public class ControllerError {

	private String	mTitle			= "";
	private String	mDescription	= "";
	private int		mErrorCode;

	public ControllerError(int errorCode) {
		mTitle = null;
		mDescription = null;
		mErrorCode = errorCode;
	}

	public ControllerError(int errorCode, String title, String description) {
		mTitle = title;
		mDescription = description;
		mErrorCode = errorCode;
	}

	public ControllerError(VolleyError error, Context context) {

		if (error instanceof ServerError) {
			try {
				parseAsSMAPException(error);
				return;
			}
			catch (JsonSyntaxException e) {
				Log.e(ControllerError.class.getCanonicalName(),
						"Malformed Smap Data Exception, default to Generic Error");
				mTitle = "error";// context.getResources().getString(R.string.error_generic);
				mErrorCode = error.networkResponse == null ? 0
						: error.networkResponse.statusCode;
			}

		} else {
			parseAsVolleyException(error, context);
		}

	}

	private void parseAsSMAPException(VolleyError error) {

		Gson gson = new GsonBuilder().create();

		SmapException me = gson.fromJson(
				new String(error.networkResponse.data), SmapException.class);

		mErrorCode = error.networkResponse.statusCode;
		mDescription = me.techMessage;

	}

	private void parseAsVolleyException(VolleyError error, Context context) {

		int msgId = 0;
		// TODO specify error messages

		/*
		 * if (error instanceof NoConnectionError) { msgId =
		 * R.string.error_no_connection; } else if (error instanceof
		 * NetworkError) { msgId = R.string.error_network; } else if (error
		 * instanceof TimeoutError) { msgId = R.string.error_timeout; } else if
		 * (error instanceof ParseError) { msgId = R.string.error_parse; } else
		 * if (error instanceof AuthFailureError) { msgId =
		 * R.string.error_auth_failed; } else { msgId = R.string.error_generic;
		 * }
		 */

		mTitle = "error";// context.getResources().getString(msgId);
		mErrorCode = error.networkResponse == null ? 0
				: error.networkResponse.statusCode;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDescription() {
		return mDescription;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

}
