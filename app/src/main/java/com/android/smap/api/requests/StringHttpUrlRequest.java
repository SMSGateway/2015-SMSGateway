package com.android.smap.api.requests;

import com.android.smap.GatewayApp;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;

/**
 * Created by kai on 7/05/2015.
 */
public abstract class StringHttpUrlRequest extends HttpUrlRequest<String> {

    protected StringHttpUrlRequest(String url, String method, HttpListener<String> httpListener, HttpErrorListener httpErrorListener) {
        super(url, method, httpListener, httpErrorListener);
        setDigestAuth();
        setUrlPortFromPrefer();
    }

    public void setDigestAuth() {
        String userName = GatewayApp.getPreferenceWrapper().getUserName();
        String password = GatewayApp.getPreferenceWrapper().getPassword();
        httpDigestAuthentication(true, userName, password);
    }

    public void setUrlPortFromPrefer() {
        String port = GatewayApp.getPreferenceWrapper().getServerPort();

        if (port != null)
            setPort(Integer.valueOf(port));
    }

    @Override
    public String onResultResponse(String result) {
        return result;
    }
}
