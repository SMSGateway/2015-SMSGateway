package com.android.smap.api.requests;

import android.text.TextUtils;
import android.util.Log;

import com.android.smap.GatewayApp;
import com.android.smap.api.ApiConstants;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Generic API request for a GoJo
 * 
 * @author matt witherow
 * @param <T>
 */
public abstract class ApiRequest<T> extends Request<T> implements ApiConstants {

	protected Listener<T>		mListener;
	protected FieldNamingPolicy	mNamingPolicy	= FieldNamingPolicy.IDENTITY;

	public ApiRequest(int method, String url, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		Log.d("ApiRequest", "SENDING VOLLEY REQ  : " + url);
		mListener = listener;
		setRetryPolicy(new DefaultRetryPolicy((int) GatewayApp.getAppConfig()
				.getTimeoutInMillis(), DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data);
			T responseModel = createGsonModel(jsonString);
			return Response.success(responseModel,
					HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
		catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * Generates model using GSON
	 * 
	 * @param jsonString
	 * @return
	 */
	private T createGsonModel(String jsonString) throws JsonSyntaxException,
			IllegalArgumentException {

		// if no json return null
		if (TextUtils.isEmpty(jsonString)) {
			throw new IllegalArgumentException();
		}

		GsonBuilder builder = new GsonBuilder()
				.setFieldNamingPolicy(mNamingPolicy);

		JsonDeserializer<T> deserializer = getCustomDeserializer();
		if (deserializer != null) {
			builder.registerTypeAdapter(getGsonTypeToken().getType(),
					deserializer);
		}

		Gson gson = builder.create();

		// parse the result
		T model = gson.fromJson(jsonString, getGsonTypeToken().getType());
		return model;
	}

	/**
	 * Provide {@link TypeToken} for the GSON deserialization of the Model.
	 * 
	 * @return
	 */
	protected abstract TypeToken<T> getGsonTypeToken();

	/**
	 * Override and provide a custom GSON deserializer if required.
	 * 
	 * @return
	 */
	protected JsonDeserializer<T> getCustomDeserializer() {
		return null;
	}
}
