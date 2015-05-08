package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.utils.UriBuilder;

public class FormListHttpUrlRequest extends RawHttpUrlRequest<String> {

    public FormListHttpUrlRequest(HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(generateUrl(), DO_GET, true, httpListener, httpErrorListener);
    }

    private static String generateUrl() {
        String requestHost = GatewayApp.getPreferenceWrapper().getServerHost();

        if (requestHost == null)
            requestHost = GatewayApp.getAppConfig().getRequestEndpoint();

        return new UriBuilder()
                .scheme(SCHEME_HTTP)
                .encodedAuthority(requestHost)
                .appendEncodedPath(FORM_LIST).build().toString();
    }

    @Override
    public String onResultResponse(String result) {
        return result;
    }
}
