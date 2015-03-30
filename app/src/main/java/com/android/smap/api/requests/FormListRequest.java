package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.utils.UriBuilder;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class FormListRequest extends SmapRawRequest {

	public FormListRequest(Listener<String> listener,
			ErrorListener errorListener) {
		super(Method.GET, generateUrl(), listener, errorListener);
	}

	private static String generateUrl() {
		return new UriBuilder()
				.scheme(SCHEME_HTTP)
				.encodedAuthority(
						GatewayApp.getAppConfig().getRequestEndpoint())
				.appendEncodedPath(FORM_LIST).build().toString();
	}
}
