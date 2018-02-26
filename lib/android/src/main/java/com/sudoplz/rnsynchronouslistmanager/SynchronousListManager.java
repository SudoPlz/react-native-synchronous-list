package com.sudoplz.rnsynchronouslistmanager;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.sudoplz.rnsynchronouslistmanager.Views.List.SPRecyclerView;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;

/**
 * Created by SudoPlz on 05/02/2018.
 */

public class SynchronousListManager extends ViewGroupManager <SPRecyclerView> {
    private SPRecyclerView listView;
    private SPGlobals globals;
    private boolean hasInitialised;

    public SynchronousListManager(ReactContext context, ReactNativeHost rcHost) {
        super();
        globals = SPGlobals.init(context, rcHost);
        hasInitialised = false;
    }


    @Override
    public String getName() {
        return "RCTSynchronousList";
    }


    @Override
    public SPRecyclerView createViewInstance(ThemedReactContext context) {
//        return new SPView(context, map);
//        System.out.println("@@@@@@@@@@@@@ Created Synchronous list");
        if (listView == null) {
//            listView = new SPRecyclerView(context, initialData);
            listView = new SPRecyclerView(context);
        } else if (hasInitialised == true) {
            // if the list has not been initialised yet, but SOMEHOW the list view was NOT null
            // that means the app reloaded, so let's remove the listView from it's parent (since it's going to be added on another parent)
            globals.setRcContext(context);
            listView.onReload();

        }
        hasInitialised = true;
//        HorizontalScrollView hr = new HorizontalScrollView(context);
//        hr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1920));
//
//        hr.addView(listView);
//        return hr;
        return listView;
    }

    public SPRecyclerView getListView() {
        return listView;
    }

    @ReactProp(name = "data")
    public void setData(SPRecyclerView view, @Nullable ReadableArray data) {
        listView.setData(data);
    }

    @ReactProp(name = "templateName")
    public void setTemplateName(SPRecyclerView view, @Nullable String templateName) {
        globals.setMainViewTemplateName(templateName);
    }

    @ReactProp(name = "numRenderRows")
    public void setNumRenderRows(SPRecyclerView view, @Nullable int numRenderRows) {
        globals.setNumRenderRows(numRenderRows);
    }

    @ReactProp(name = "loopMode")
    public void setLoopMode(SPRecyclerView view, @Nullable String loopMode) {
        globals.setLoopMode(loopMode);
    }




}

