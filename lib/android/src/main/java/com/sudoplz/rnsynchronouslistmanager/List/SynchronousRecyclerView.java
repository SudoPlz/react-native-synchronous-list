package com.sudoplz.rnsynchronouslistmanager.List;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRootView;

/**
 * Created by SudoPlz on 05/02/2018.
 */

// TODO Extend the RecyclerView instead of SyncRootView once we write an actual RecyclerView implementation
//public class SynchronousRecyclerView extends RecyclerView {
public class SynchronousRecyclerView extends SyncRootView {

    private final static String TAG = "SynchronousRecyclerView";

//    private final OnScrollDispatchHelper mOnScrollDispatchHelper = new OnScrollDispatchHelper();
//    private final VelocityHelper mVelocityHelper = new VelocityHelper();

    public SynchronousRecyclerView(ReactContext context, ReactNativeHost rcHost) {
//        super(context); // recyclerview
        super("MyStaticRowTemplate", context, rcHost);
    }


    public void prepareRows(final Promise promise) {
        Log.d(TAG, "@@@@@@@@@@@@@@ prepareRows RAN");
    }

}
