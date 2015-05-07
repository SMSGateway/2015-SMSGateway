package com.android.smap.api.requests;

import android.text.TextUtils;

import com.android.smap.GatewayApp;
import com.android.smap.api.models.Status;
import com.android.smap.controllers.HttpErrorListener;
import com.android.smap.controllers.HttpListener;
import com.android.smap.utils.UriBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * This class is altered to use httpURL
 *
 * @author Kai Qin
 * @param <T>
 */

/**
 * Vanilla sample request, get status of server
 *
 * @author Matt Witherow
 */
public class StatusHttpUrlRequest extends HttpUrlRequest<Status> {

    protected FieldNamingPolicy mNamingPolicy = FieldNamingPolicy.IDENTITY;

    public StatusHttpUrlRequest(String url, String method, boolean digestAuth, HttpListener<Status> httpListener, HttpErrorListener httpErrorListener) {
        super(generateUrl(), DO_GET, true, httpListener, httpErrorListener);
    }

    private static String generateUrl() {

        return new UriBuilder()
                .scheme(SCHEME_HTTP)
                .encodedAuthority(
                        GatewayApp.getAppConfig().getRequestEndpoint())
                .appendEncodedPath(API_TOKEN).build().toString();
    }

    @Override
    public Status onResultResponse(String result) {
        Status responseModel = createGsonModel(result);
        return responseModel;
    }

    /**
     * Generates model using GSON
     *
     * @param jsonString
     * @return
     */
    private Status createGsonModel(String jsonString) throws JsonSyntaxException,
            IllegalArgumentException {

        // if no json return null
        if (TextUtils.isEmpty(jsonString)) {
            throw new IllegalArgumentException();
        }

        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingPolicy(mNamingPolicy);

        JsonDeserializer<Status> deserializer = getCustomDeserializer();
        if (deserializer != null) {
            builder.registerTypeAdapter(getGsonTypeToken().getType(),
                    deserializer);
        }

        Gson gson = builder.create();

        // parse the result
        Status model = gson.fromJson(jsonString, getGsonTypeToken().getType());
        return model;
    }

    /**
     * Override and provide a custom GSON deserializer if required.
     *
     * @return
     */
    protected JsonDeserializer<Status> getCustomDeserializer() {
        return null;
    }

    /**
     * Provide {@link com.google.gson.reflect.TypeToken} for the GSON deserialization of the Model.
     *
     * @return
     */
    protected TypeToken<Status> getGsonTypeToken() {
        return new TypeToken<Status>() {
        };
    }
}
