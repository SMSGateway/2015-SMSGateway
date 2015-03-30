package com.android.smap.api.requests;

import java.util.HashMap;
import java.util.Map;

import android.util.Base64;
import android.util.Log;

import com.android.smap.GatewayApp;
import com.android.smap.api.ApiConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;

public abstract class SmapRawRequest extends Request<String> implements
		ApiConstants {

	protected Listener<String>	mListener;
	protected FieldNamingPolicy	mNamingPolicy	= FieldNamingPolicy.IDENTITY;

	public SmapRawRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		Log.d("ApiRequest", "SENDING VOLLEY REQ  : " + url);
		mListener = listener;
		setRetryPolicy(new DefaultRetryPolicy((int) GatewayApp.getAppConfig()
				.getTimeoutInMillis(), DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {

		Map<String, String> headers = new HashMap<String, String>();
		String usr = GatewayApp.getPreferenceWrapper().getUserName();
		String psw = GatewayApp.getPreferenceWrapper().getPassword();
		String creds = String.format("%s:%s", usr, psw);
		String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
		headers.put("Authorization", auth);
		return headers;
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {

		try {
			String xmlStringDump = new String(response.data);
			return Response.success(xmlStringDump,
					HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

}
