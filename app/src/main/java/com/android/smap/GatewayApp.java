package com.android.smap;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.android.smap.utils.PreferenceWrapper;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * The main {@link Application} class. Singleton that provides access to main
 * utilities and global components.
 *
 * @author matt witherow
 */
public class GatewayApp extends Application {

    private static GatewayApp sInstance;
    private static AppConfig sAppConfig;
    private static List<String> newSurveys;
    private static PreferenceWrapper sPreferenceWrapper;
    private static PhoneStateWrapper sPhoneStateWrapper;
    private static DependencyContainer sDependencyContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = GatewayApp.this;
        sAppConfig = new AppConfig(this);
        newSurveys = new ArrayList<String>();
        sPreferenceWrapper = new PreferenceWrapper(this);
        sPhoneStateWrapper = new PhoneStateWrapper(this);
        sDependencyContainer = new DependencyContainer();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    /**
     * Returns the {@link PreferenceWrapper}
     *
     * @return
     */
    public static PreferenceWrapper getPreferenceWrapper() {
        return sPreferenceWrapper;
    }

    /**
     * Returns an instance of the SMAP Application
     *
     * @return
     */
    public static GatewayApp getInstance() {
        return sInstance;
    }

    /**
     * Returns the Application Configuration (managed by the companion app)
     *
     * @return
     */
    public static AppConfig getAppConfig() {
        return sAppConfig;
    }

    /**
     * Returns a phone state convenience wrapper.
     *
     * @return
     */
    public static PhoneStateWrapper getPhoneStateWrapper() {
        return sPhoneStateWrapper;
    }

    public static DependencyContainer getDependencyContainer() {
        return sDependencyContainer;
    }

    public static List<String> getNewSurveys() {
        return newSurveys;
    }

    public static void setNewSurveys(List<String> newSurveys) {
        GatewayApp.newSurveys = newSurveys;
    }
}
