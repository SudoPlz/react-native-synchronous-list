package com.sudoplz.rnsynchronouslistmanager;

import com.sudoplz.rnsynchronouslistmanager.List.SynchronousRecyclerView;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRootView;

/**
 * Created by SudoPlz on 05/02/2018.
 */

public class SynchronousListManager extends ViewGroupManager <SynchronousRecyclerView> {
    private SynchronousRecyclerView listView;


    public SynchronousListManager(ReactContext context, ReactNativeHost rcHost) {
        super();
        if (listView == null) {
            listView = new SynchronousRecyclerView(context, rcHost);
        }
    }


    @Override
    public String getName() {
        return "RCTSynchronousList";
    }


    @Override
    public SynchronousRecyclerView createViewInstance(ThemedReactContext context) {
        return listView;
    }

    public SynchronousRecyclerView getListView() {
        return listView;
    }

}

