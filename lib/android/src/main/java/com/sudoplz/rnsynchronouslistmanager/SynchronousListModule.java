package com.sudoplz.rnsynchronouslistmanager;

import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

class SynchronousListModule extends ReactContextBaseJavaModule {
    private RCTACCalendar calendarInstance;

    public SynchronousListModule (ReactApplicationContext reactContext, RCTACCalendarManager calManager) {
        super(reactContext);
        if (calManager != null) {
            calendarInstance = calManager.getCalendarInstance();
        }
    }

    /**
     * @return the name of this module. This will be the name used to {@code require()} this module
     * from javascript.
     */
    @Override
    public String getName() {
        return "SynchronousListManager";
    }

    @ReactMethod
    public void prepareRows(Promise promise) {
        if (calendarInstance  != null) {
            calendarInstance.prepareRows(promise);
        }
    }
}
