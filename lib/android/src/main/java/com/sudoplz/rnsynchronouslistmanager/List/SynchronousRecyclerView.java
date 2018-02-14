package com.sudoplz.rnsynchronouslistmanager.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by SudoPlz on 05/02/2018.
 */

public class SynchronousRecyclerView{

    private final static String TAG = "SynchronousRecyclerView";

//    private final OnScrollDispatchHelper mOnScrollDispatchHelper = new OnScrollDispatchHelper();
//    private final VelocityHelper mVelocityHelper = new VelocityHelper();

    public SynchronousRecyclerView(ReactContext context, ReactNativeHost rcHost) {
        super(context, rcHost);
    }


    public void prepareRows(final Promise promise) {
        Log.d(TAG, "@@@@@@@@@@@@@@ prepareRows RAN");
    }

}
