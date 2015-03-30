package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.api.models.Gojo;
import com.android.smap.api.models.Status;
import com.android.smap.utils.UriBuilder;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Request to post polymodel data
 * 
 * @author Matt Witherow
 * 
 */
public class GojoRequest extends ApiRequest<Status> {

	private Gojo	mModel;

	public GojoRequest(Gojo model, Listener<Status> listener,
			ErrorListener errorListener) {
		super(Method.POST, generateUrl(), listener, errorListener);
		this.mModel = model;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(mModel);
		return json.getBytes();
	}

	private static String generateUrl() {

		return new UriBuilder()
				.scheme(SCHEME_HTTP)
				.encodedAuthority(
						GatewayApp.getAppConfig().getRequestEndpoint()).build()
				.toString();
	}

	@Override
	protected TypeToken<Status> getGsonTypeToken() {
		return new TypeToken<Status>() {};
	}

}
