package com.android.smap.utils;

import android.net.Uri;
import android.text.TextUtils;

/**
 * The wrapper class for {@link Uri.builder} with null pointer checks
 */
public class UriBuilder {

    /** The instance of {@link Uri.builder} */
    private final Uri.Builder mBuilder = new Uri.Builder();

    /**
     * Appends the given segment to the path.
     * 
     * @param newSegment
     * @return self
     */
    public UriBuilder appendEncodedPath(String newSegment) {
        if (newSegment != null) {
            mBuilder.appendEncodedPath(newSegment);
        }

        return this;
    }

    /**
     * Encodes the key and value and then appends the parameter to the query string.
     * 
     * @param key
     *            which will be encoded
     * @param value
     *            which will be encoded
     * @return self
     */
    public UriBuilder appendQueryParameter(String key, String value) {
        if (key != null && value != null) {
            mBuilder.appendQueryParameter(key, value);
        }

        return this;
    }

    /**
     * Encodes the key and values and then appends the parameter to the query string. Vales will be
     * joined to comma separated string.
     * 
     * @param key
     *            which will be encoded
     * @param values
     *            which will be encoded
     * @return self
     */
    public UriBuilder appendQueryParameter(String key, String[] values) {
        if (key != null && values != null) {
            mBuilder.appendQueryParameter(key, TextUtils.join(",", values));
        }

        return this;
    }

    /**
     * Sets the previously encoded authority.
     * 
     * @param authority
     *            example "google.com"
     * @return self
     */
    public UriBuilder encodedAuthority(String authority) {
        mBuilder.encodedAuthority(authority);
        return this;
    }

    /**
     * Sets the previously encoded fragment.
     * 
     * @param fragment
     * @return self
     */
    public UriBuilder encodedFragment(String fragment) {
        if (fragment != null) {
            mBuilder.encodedFragment(fragment);
        }

        return this;
    }

    /**
     * Sets the scheme.
     * 
     * @param scheme
     *            name or null if this is a relative Uri
     * @return self
     */
    public UriBuilder scheme(String scheme) {
        mBuilder.scheme(scheme);
        return this;
    }

    /**
     * Constructs a Uri with the current attributes.
     * 
     * @return Uri
     * @throws UnsupportedOperationException
     *             if the URI is opaque and the scheme is null
     */
    public Uri build() throws UnsupportedOperationException {
        return mBuilder.build();
    }
}
