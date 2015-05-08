package com.android.smap.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.smap.GatewayApp;

/**
 * This class is altered to provide the store for server.
 *
 * @author Kai Qin
 */

/**
 * Preference wrapper for convenience methods dealing with android's shared
 * preferences.
 *
 * @author Matt Witherow
 */
public class PreferenceWrapper {

    // AlarmListener
    private static final String UPDATE_INTERVAL = PreferenceWrapper.class
            .getCanonicalName()
            + "update_interval";

    public static final String DEFAULT_INTERVAL = String.valueOf(30 * 1000);

    private static final String USER_NAME = PreferenceWrapper.class
            .getCanonicalName()
            + "user";

    private static final String PASSWORD = PreferenceWrapper.class
            .getCanonicalName()
            + "password";

    private static final String SERVER_HOST = PreferenceWrapper.class
            .getCanonicalName()
            + "serverHost";

    private static final String SERVER_PORT = PreferenceWrapper.class
            .getCanonicalName()
            + "serverPort";

    private GatewayApp mApp;
    private SharedPreferences mPrefs;

    public PreferenceWrapper(GatewayApp gatewayApp) {
        mApp = gatewayApp;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mApp);

    }

    public boolean isAutoRefreshEnabled() {
        return true;
    }

    public long getUpdateInterval() {
        return Long.parseLong(mPrefs.getString(UPDATE_INTERVAL,
                DEFAULT_INTERVAL));
    }

    public void setUserName(String username) {
        Editor edit = mPrefs.edit();
        edit.putString(USER_NAME, username);
        edit.commit();
    }

    public void setPassword(String username) {
        Editor edit = mPrefs.edit();
        edit.putString(PASSWORD, username);
        edit.commit();
    }

    public String getUserName() {
        return mPrefs.getString(USER_NAME, null);
    }

    public String getPassword() {
        return mPrefs.getString(PASSWORD, null);
    }

    public void setServerHost(String ServerHost) {
        Editor edit = mPrefs.edit();
        edit.putString(SERVER_HOST, ServerHost);
        edit.commit();
    }

    public void setServerPort(String ServerPort) {
        Editor edit = mPrefs.edit();
        edit.putString(SERVER_PORT, ServerPort);
        edit.commit();
    }

    public String getServerHost() {
        return mPrefs.getString(SERVER_HOST, null);
    }

    public String getServerPort() {
        return mPrefs.getString(SERVER_PORT, null);
    }

    public String getRequestEndpoint() {
        String endpoint = null;
        if (!TextUtils.isEmpty(getServerHost())) {
            endpoint = getServerHost();
        }
        if (!TextUtils.isEmpty(getServerPort())) {
            endpoint = endpoint + ":" + getServerPort();
        }
        return endpoint;
    }
}
