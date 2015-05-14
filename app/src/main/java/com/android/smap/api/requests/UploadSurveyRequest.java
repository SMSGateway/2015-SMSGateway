package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.utils.UriBuilder;

/**
 * This class is used for upload surveys
 * <p/>
 * Created by kai on 11/05/2015.
 */
public class UploadSurveyRequest extends StringHttpUrlRequest {
    public UploadSurveyRequest(HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(generateUrl(), DO_POST, httpListener, httpErrorListener);
        setPostRequestProperties();
    }

    private static String generateUrl() {
        String requestHost = GatewayApp.getPreferenceWrapper().getServerHost();

        if (requestHost == null || requestHost.equals(""))
            requestHost = GatewayApp.getAppConfig().getRequestEndpoint();

        return new UriBuilder()
                .scheme(SCHEME_HTTP)
                .encodedAuthority(requestHost)
                .appendEncodedPath(SUBMISSION_XML).build().toString();
    }

    /**
     * This initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     */
    private void setPostRequestProperties() {
        addRequestProperties("X­Requested­With", "XMLHttpRequest");
        addRequestProperties("Content-Type", "multipart/form-data; boundary=" + getBoundary());
    }
}
