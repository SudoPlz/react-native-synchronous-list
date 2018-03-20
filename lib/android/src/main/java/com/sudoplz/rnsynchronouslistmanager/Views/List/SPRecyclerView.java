package com.sudoplz.rnsynchronouslistmanager.Views.List;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
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
    private boolean mRequestedLayout = false;

    public SPRecyclerView(ReactContext context) {
        this(context, null);
    }

    public SPRecyclerView(ReactContext context, ReadableArray newData) {
        super(context);
        // setting the list adapter (which will be holding the views and binding props to them)
        setAdapter(new SPAdapter(WritableAdvancedArray.shallowToArrayList(newData)));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println("\n@@@@@@@@@@@@@@@@@ onLayout WOULD run");
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
        // TODO: go through all the SyncRootViews and get a new ref to the thread
    }


    /////////////////////////////////////////////////////////////
    ////////////////////// EXPOSED METHODS //////////////////////
    /////////////////////////////////////////////////////////////

    public void prepareRows(final Promise promise) {
        Log.d(TAG, "@@@@@@@@@@@@@@ prepareRows RAN");
    }
    @UiThread
    public void rcScrollToItem(final int position) {
//        smoothScrollToPosition(position);
        scrollToPosition(position); // TODO <-- why doesn't scrollToPosition work?
    }

    public void rcPrependDataToDataSource(ReadableArray newData) {
        final SPAdapter adapter = (SPAdapter) getAdapter();
        adapter.prependData(WritableAdvancedArray.shallowToArrayList(newData));
    }

    public void rcAppendDataToDataSource(ReadableArray newData) {
        final SPAdapter adapter = (SPAdapter) getAdapter();
        adapter.appendData(WritableAdvancedArray.shallowToArrayList(newData));
    }

    public void rcUpdateDataAtIndex(int indexToUpdate, ReadableMap updatedChild) {
        final SPAdapter adapter = (SPAdapter) getAdapter();
        mRequestedLayout = false;
        adapter.updateDataAtIndex(indexToUpdate, updatedChild);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        // We need to intercept this method because if we don't our children will never update
        // Check https://stackoverflow.com/questions/49371866/recyclerview-wont-update-child-until-i-scroll
        if (!mRequestedLayout) {
            mRequestedLayout = true;
            this.post(new Runnable() {
                @SuppressLint("WrongCall")
                @Override
                public void run() {
                    mRequestedLayout = false;
                    layout(getLeft(), getTop(), getRight(), getBottom());
                    onLayout(false, getLeft(), getTop(), getRight(), getBottom());
                }
            });
        }
    }

}
