package com.android.smap.api.requests;

import android.os.AsyncTask;
import android.util.Log;

import com.android.smap.api.ApiConstants;
import com.android.smap.api.models.FilePart;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.controllers.NetworkError;
import com.android.smap.utils.HttpURLDigestAuth;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a base class used for doing a http request
 * <p/>
 * Created by kai qin on 7/05/2015.
 */
public abstract class HttpUrlRequest<T> implements ApiConstants {

    private String url;
    private String username;
    private String password;
    private String method;
    private int responseCode;
    private int port;
    private boolean digestAuth;
    private HttpListener<T> httpListener;
    private HttpErrorListener httpErrorListener;
    private HttpURLConnection finalConn;
    private PrintWriter writer;
    private OutputStream outputStream;
    private String charset;
    private static String boundary;
    private static final String LINE_FEED = "\r\n";
    private Map<String, String> requestProperties;
    private Map<String, String> formFields;
    private ArrayList<FilePart> fileParts;


    protected HttpUrlRequest(String url, String method, HttpListener<T> httpListener, HttpErrorListener httpErrorListener) {
        //default port is 80
        port = 80;
        charset = "UTF-8";
        this.url = url;
        this.method = method;
        this.digestAuth = false;
        this.httpListener = httpListener;
        this.httpErrorListener = httpErrorListener;
        // creates a unique boundary based on time stamp
        boundary = "WebKitFormBoundarydEw6oDxwLjIN2afu";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isDigestAuth() {
        return digestAuth;
    }

    public void setDigestAuth(boolean digestAuth) {
        this.digestAuth = digestAuth;
    }

    public HttpListener<T> getHttpListener() {
        return httpListener;
    }

    public HttpErrorListener getHttpErrorListener() {
        return httpErrorListener;
    }

    public static String getBoundary() {
        return boundary;
    }

    public void setFormFields(Map<String, String> formFields) {
        this.formFields = formFields;
    }

    public void setFileParts(ArrayList<FilePart> fileParts) {
        this.fileParts = fileParts;
    }

    public void addRequestProperties(String field, String newValue) {
        if (requestProperties == null) {
            requestProperties = new HashMap<String, String>();
        }
        requestProperties.put(field, newValue);
    }

    public void addFilePart(FilePart filePart) {
        if (fileParts == null) {
            fileParts = new ArrayList<FilePart>();
        }
        fileParts.add(filePart);
    }

    public void addFormField(String name, String value) {
        if (formFields == null) {
            formFields = new HashMap<String, String>();
        }
        formFields.put(name, value);
    }

    public void httpDigestAuthentication(boolean digestAuth, String username, String password) {
        this.digestAuth = digestAuth;
        this.username = username;
        this.password = password;
    }

    public NetworkError parseNetworkError(String errorStr) {
        Log.i("NetworkError:", errorStr);
        NetworkError error = new NetworkError("Error in network connection.", responseCode);
        return error;
    }

    protected void addPortToUrlStr() {
        try {
            URL url = new URL(this.url);
            this.url = url.getProtocol() + "://" + url.getHost();
            String path = url.toString().substring(this.url.length());
            this.url += ":" + String.valueOf(port) + path;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("HttpUrlRequest", "It is failure to add port to Url.");
        }
    }

    public void executeRequest() {

        if (isDigestAuth() && username == null || password == null) {
            NetworkError error = new NetworkError();
            error.setNetworkErrorMessage("Please set UserName and Password.");
            httpErrorListener.onErrorResponse(error);
            return;
        }
        // Add port to Url scheme
        addPortToUrlStr();
        new NetworkConnectionTask().execute(url);
    }

    public abstract T onResultResponse(String result);

    public void doGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Log.i("request url", " " + conn.getURL().toString());

        if (isDigestAuth()) {
            finalConn = HttpURLDigestAuth.tryAuth(conn, username, password);
        } else {
            finalConn = conn;
        }

        finalConn.setReadTimeout(10000 /* milliseconds */);
        finalConn.setConnectTimeout(10000 /* milliseconds */);
        finalConn.setDoInput(true);
    }

    public void doPost(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        Log.i("request url", " " + conn.getURL().toString());
        if (isDigestAuth()) {
            finalConn = HttpURLDigestAuth.tryAuth(conn, username, password);
        } else
            finalConn = conn;

        finalConn.setReadTimeout(10000 /* milliseconds */);
        finalConn.setConnectTimeout(10000 /* milliseconds */);
        finalConn.setDoInput(true);
        finalConn.setDoOutput(true);
        finalConn.setUseCaches(false);
        finalConn.setFollowRedirects(true);

        if (requestProperties != null && !requestProperties.isEmpty()) {
            for (String key : requestProperties.keySet()) {
                finalConn.setRequestProperty(key, requestProperties.get(key));
            }
        }

        outputStream = finalConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

        //Add files stream to post request
        if (fileParts != null && !fileParts.isEmpty()) {
            for (FilePart filePart : fileParts) {
                writeFilePart(filePart.getFieldName(), filePart.getInputStream(), filePart.getFileName());
            }
        }

        //Add form fields to post request
        if (formFields != null && !formFields.isEmpty()) {
            for (String key : formFields.keySet()) {
                writeFormField(key, formFields.get(key));
            }
        }

        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.flush();
        writer.close();
    }

    private class NetworkConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return httpErrorListener.errorMessage;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (result.equals(httpErrorListener.errorMessage)) {
                NetworkError error = new NetworkError();
                error.setNetworkErrorMessage(result);
                httpErrorListener.onErrorResponse(error);

            } else if (responseCode >= 400) {
                httpErrorListener.onErrorResponse(parseNetworkError(result));

            } else {
                httpListener.onResponse(onResultResponse(result));
            }
        }
    }

    private String downloadUrl(String myUrl) throws IOException {
        InputStream in = null;

        try {
            if (method.equals(DO_GET))
                doGet(myUrl);
            else
                doPost(myUrl);

            // Starts the query
            finalConn.connect();
            responseCode = finalConn.getResponseCode();
            Log.d("", "The response is: " + responseCode + " " + finalConn.getURL());

            if (responseCode >= 400)
                in = finalConn.getErrorStream();
            else
                in = finalConn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(in);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public String readIt(InputStream in) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayBuffer baf = new ByteArrayBuffer(50);
        int read = 0;
        int bufSize = 512;
        byte[] buffer = new byte[bufSize];
        while (true) {
            read = bis.read(buffer);
            if (read == -1) {
                break;
            }
            baf.append(buffer, 0, read);
        }
        return new String(baf.toByteArray());
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName   name attribute in <input type="file" name="..." />
     * @param inputStream a File to be uploaded
     * @throws IOException
     */
    public void writeFilePart(String fieldName, InputStream inputStream, String fileName)
            throws IOException {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName
                + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void writeFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }
}
