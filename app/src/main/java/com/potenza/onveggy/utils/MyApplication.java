package com.potenza.onveggy.utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.multidex.MultiDex;

import com.potenza.onveggy.customview.pref.Prefs;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.applinks.AppLinkData;
import com.onesignal.OneSignal;


/**
 * Created by Bhumi Shah on 12/23/2017.
 */

public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new APIS();

        OneSignal.startInit(this)
                //.setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                //.setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(Constant.MyPREFERENCES)
                .setUseDefaultSharedPreference(true)
                .build();

        AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {
                    // Process app link data
                });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
