package com.android.smap.api.requests;

import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;

public class SurveyDefinitionRequest extends StringHttpUrlRequest {

    public SurveyDefinitionRequest(String xmlFormKey, HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(xmlFormKey, DO_GET, httpListener, httpErrorListener);
    }

    @Override
    public String onResultResponse(String result) {
        return result;
    }
}
