package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.utils.UriBuilder;

public class FormListHttpUrlRequest extends HttpUrlRequest<String> {

    public FormListHttpUrlRequest(HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(generateUrl(), DO_GET, true, httpListener, httpErrorListener);
    }

    private static String generateUrl() {
        return new UriBuilder()
                .scheme(SCHEME_HTTP)
                .encodedAuthority(
                        GatewayApp.getAppConfig().getRequestEndpoint())
                .appendEncodedPath(FORM_LIST).build().toString();
    }

    @Override
    public String onResultResponse(String result) {
        return result;
    }
}
