package com.sudoplz.rnsynchronouslistmanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.sudoplz.rnsynchronouslistmanager.Views.List.SPRecyclerView;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public SPRecyclerView createViewInstance(ThemedReactContext context) {
//        return new SPView(context, map);
//        System.out.println("@@@@@@@@@@@@@ Created Synchronous list");
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Point screenSize = new Point();
//        wm.getDefaultDisplay().getRealSize(screenSize);
//        int calHeight = 1980;

        if (listView == null) {
//            listView = new SPRecyclerView(context, initialData);
            listView = new SPRecyclerView(context);
            listView.setNestedScrollingEnabled(true);
            listView.setHorizontalScrollBarEnabled(true);

            // setting the layout parameters
//        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//or HORIZONTAL
//            linearLayoutManager.setMe

            // setting the layout manager (which will be responsible for laying out the views)
            listView.setLayoutManager(linearLayoutManager);
        } else if (hasInitialised == true) {
            // if the list has not been initialised yet, but SOMEHOW the list view was NOT null
            // that means the app reloaded, so let's remove the listView from it's parent (since it's going to be added on another parent)
            globals.setRcContext(context);
            listView.onReload();

        }
        hasInitialised = true;
//        NestedScrollView verticalParent = new NestedScrollView(context);
//        verticalParent.setVerticalScrollBarEnabled(true);
////        HorizontalScrollView hr = new HorizontalScrollView(context);
//        verticalParent.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
//        verticalParent.addView(listView);
//
//        hr.addView(listView);
//        return hr;
        return listView;
    }

    public SPRecyclerView getListView() {
        return listView;
    }

    @ReactProp(name = "data")
    public void setData(ViewGroup view, @Nullable ReadableArray data) {
        listView.setData(data);
    }

    @ReactProp(name = "templateName")
    public void setTemplateName(ViewGroup view, @Nullable String templateName) {
        globals.setMainViewTemplateName(templateName);
    }

    @ReactProp(name = "numRenderRows")
    public void setNumRenderRows(ViewGroup view, @Nullable int numRenderRows) {
        globals.setNumRenderRows(numRenderRows);
    }

    @ReactProp(name = "loopMode")
    public void setLoopMode(ViewGroup view, @Nullable String loopMode) {
        globals.setLoopMode(loopMode);
    }




}

