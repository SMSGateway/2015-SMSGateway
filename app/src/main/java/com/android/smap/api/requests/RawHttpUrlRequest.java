package com.android.smap.api.requests;

import android.os.AsyncTask;
import android.util.Log;

import com.android.smap.GatewayApp;
import com.android.smap.api.ApiConstants;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.controllers.NetworkError;
import com.android.smap.utils.HttpURLDigestAuth;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kai on 7/05/2015.
 */
public abstract class RawHttpUrlRequest<T> extends HttpUrlRequest<T> {

    protected RawHttpUrlRequest(String url, String method, boolean digestAuth, HttpListener<T> httpListener, HttpErrorListener httpErrorListener) {
        super(url, method, digestAuth, httpListener, httpErrorListener);
        setUrlPortFromPrefer();
    }

    public void setUrlPortFromPrefer() {
        String port = GatewayApp.getPreferenceWrapper().getServerPort();

        if (port != null)
            setPort(Integer.valueOf(port));
    }
}
