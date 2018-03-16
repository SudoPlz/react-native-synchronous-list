package com.sudoplz.rnsynchronouslistmanager;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Views.List.SPRecyclerView;

class SynchronousListModule extends ReactContextBaseJavaModule {
    private SPRecyclerView calendarInstance;

    public SynchronousListModule (ReactApplicationContext reactContext, SynchronousListManager calManager) {
        super(reactContext);
        if (calManager != null) {
            calendarInstance = calManager.getListView();
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

    @ReactMethod
    public void scrollToItem(int item) {
        if (calendarInstance  != null) {
            calendarInstance.rcScrollToItem(item);
        }
    }

    @ReactMethod
    public void prependDataToDataSource(ReadableArray newData) {
        if (calendarInstance  != null) {
            calendarInstance.rcPrependDataToDataSource(newData);
        }
    }

    @ReactMethod
    public void appendDataToDataSource(ReadableArray newData) {
        if (calendarInstance  != null) {
            calendarInstance.rcAppendDataToDataSource(newData);
        }
    }

    @ReactMethod
    public void updateDataAtIndex(int indexToUpdate, ReadableMap newData) {
        if (calendarInstance  != null) {
            calendarInstance.rcUpdateDataAtIndex(indexToUpdate, newData);
        }
    }



}
