package com.android.smap.api.requests;

import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;

public class SurveyDefinitionHttpUrlRequest extends HttpUrlRequest<String> {

    public SurveyDefinitionHttpUrlRequest(String xmlFormKey, HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(xmlFormKey, DO_GET, true, httpListener, httpErrorListener);
    }

    @Override
    public String onResultResponse(String result) {
        return result;
    }
}
