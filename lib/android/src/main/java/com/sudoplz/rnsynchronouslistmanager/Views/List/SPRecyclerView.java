package com.sudoplz.rnsynchronouslistmanager.Views.List;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedArray;

/**
 * Created by SudoPlz on 05/02/2018.
 */

public class SPRecyclerView extends RecyclerView {
//public class SynchronousRecyclerView extends SyncRootView {

    private final static String TAG = "SynchronousRecyclerView";
//    private final OnScrollDispatchHelper mOnScrollDispatchHelper = new OnScrollDispatchHelper();
//    private final VelocityHelper mVelocityHelper = new VelocityHelper();

    public SPRecyclerView(ReactContext context) {
        this(context, null);
    }

    public SPRecyclerView(ReactContext context, ReadableArray newData) {
        super(context);

        // setting the list adapter (which will be holding the views and binding props to them)
        setAdapter(new SPAdapter(WritableAdvancedArray.shallowToArrayList(newData)));

    }

    public void setData(ReadableArray newData) {
        if (getAdapter() != null) {
            SPAdapter adapter = (SPAdapter) getAdapter();
            adapter.setAdapterData(WritableAdvancedArray.shallowToArrayList(newData));
        }
    }

    public void onReload() {
        // remove self from parent
        ViewGroup parent = (ViewGroup) this.getParent();
        parent.removeView(this);

        // and clear all the views so that we create new ones
        if (getAdapter() != null) {
            SPAdapter adapter = (SPAdapter) getAdapter();
            adapter.clearData();
        }
        this.removeAllViewsInLayout();

    }


    public void prepareRows(final Promise promise) {
        Log.d(TAG, "@@@@@@@@@@@@@@ prepareRows RAN");
    }

}
