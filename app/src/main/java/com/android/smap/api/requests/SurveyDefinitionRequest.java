package com.android.smap.api.requests;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class SurveyDefinitionRequest extends SmapRawRequest {

	public SurveyDefinitionRequest(Listener<String> listener,
			ErrorListener errorListener, String xmlFormKey) {
		super(Method.GET, xmlFormKey, listener, errorListener);
	}

}
