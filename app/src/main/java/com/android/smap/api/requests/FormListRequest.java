package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.utils.UriBuilder;

public class FormListRequest extends StringHttpUrlRequest {

    public FormListRequest(HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(generateUrl(), DO_GET, httpListener, httpErrorListener);
    }

    private static String generateUrl() {
        String requestHost = GatewayApp.getPreferenceWrapper().getServerHost();

        if (requestHost == null || requestHost.equals(""))
            requestHost = GatewayApp.getAppConfig().getRequestEndpoint();

        return new UriBuilder()
                .scheme(SCHEME_HTTP)
                .encodedAuthority(requestHost)
                .appendEncodedPath(FORM_LIST).build().toString();
    }

}
