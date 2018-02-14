package com.sudoplz.rnsynchronouslistmanager;

import android.widget.LinearLayout;
import com.sudoplz.rnsynchronouslistmanager.List.SynchronousRecyclerView;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
/**
 * Created by SudoPlz on 05/02/2018.
 */

public class SynchronousListManager extends ViewGroupManager <SynchronousRecyclerView> {
    public static final String REACT_CLASS = "SynchronousListManager";
    private SynchronousRecyclerView listView;


    public SynchronousListManager(ReactContext context, ReactNativeHost rcHost) {
        super();
        if (listView == null) {
            listView = new SynchronousRecyclerView(context, rcHost);
        }
    }


    @Override
    public String getName() {
        return REACT_CLASS;
    }


    @Override
    public SynchronousRecyclerView createViewInstance(ThemedReactContext context) {
        return listView;
    }

    public SynchronousRecyclerView getCalendarInstance() {
        return listView;
    }

}

