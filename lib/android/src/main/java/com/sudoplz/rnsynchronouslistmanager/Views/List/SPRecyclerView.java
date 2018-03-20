package com.sudoplz.rnsynchronouslistmanager.Views.List;


import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.views.scroll.OnScrollDispatchHelper;
import com.facebook.react.views.scroll.ScrollEvent;
import com.facebook.react.views.scroll.ScrollEventType;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedArray;

/**
 * Created by SudoPlz on 05/02/2018.
 */

public class SPRecyclerView extends RecyclerView {
//public class SynchronousRecyclerView extends SyncRootView {

    private final static String TAG = "SynchronousRecyclerView";
//    private final VelocityHelper mVelocityHelper = new VelocityHelper();
    private boolean mRequestedLayout = false;
    private int mFirstVisibleIndex, mLastVisibleIndex;

//    private final OnScrollDispatchHelper mOnScrollDispatchHelper = new OnScrollDispatchHelper();

    public SPRecyclerView(ReactContext context) {
        this(context, null);
    }

    public SPRecyclerView(ReactContext context, ReadableArray newData) {
        super(context);
        // setting the list adapter (which will be holding the views and binding props to them)
        setAdapter(new SPAdapter(WritableAdvancedArray.shallowToArrayList(newData)));

//        this.addOnScrollListener(new OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
////                System.out.println("\n@@@@@@@@@@@@@@@@@ dx: "+dx+" dy: "+dy);
//                if (dy > 0) {
//                    // Scrolling up
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ Scrolling down");
//                } else {
//                    // Scrolling down
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ Scrolling up");
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    // Do something
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ FLING");
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    // Do something
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ SCROLL");
//                } else {
//                    // Do something
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ other: "+newState);
//                }
//            }
//        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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


    /////////////////////////////////////////////////////////////
    ////////////////////// EXPOSED METHODS //////////////////////
    /////////////////////////////////////////////////////////////

    public void prepareRows(final Promise promise) {
        Log.d(TAG, "@@@@@@@@@@@@@@ prepareRows RAN");
    }
    @UiThread
    public void rcScrollToItem(int position, Boolean animated) {
        if (animated) {
            smoothScrollToPosition(position);
        } else {
            mRequestedLayout = false;
            scrollToPosition(position);
        }
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

    private ReactContext getReactContext() {
        return (ReactContext) ((ContextThemeWrapper) getContext()).getBaseContext();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        if (mOnScrollDispatchHelper.onScrollChanged(l, t)) {
//            getReactContext().getNativeModule(UIManagerModule.class).getEventDispatcher()
//                    .dispatchEvent(ScrollEvent.obtain(
//                            getId(),
//                            ScrollEventType.SCROLL,
//                            0, /* offsetX = 0, horizontal scrolling only */
//                            computeVerticalScrollOffset(),
//                            mOnScrollDispatchHelper.getXFlingVelocity(),
//                            mOnScrollDispatchHelper.getYFlingVelocity(),
//                            getWidth(),
//                            computeVerticalScrollRange(),
//                            getWidth(),
//                            getHeight()));
//        }

        // try to figure which child is the most visible on screen
        final int firstIndex = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        final int lastIndex = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

        if (firstIndex != mFirstVisibleIndex || lastIndex != mLastVisibleIndex) {
            mFirstVisibleIndex = firstIndex;
            mLastVisibleIndex = lastIndex;

            /**
             * getReactContext().getNativeModule(UIManagerModule.class).getEventDispatcher()
             .dispatchEvent(new VisibleItemsChangeEvent(
             getId(),
             SystemClock.nanoTime(),
             firstIndex,
             lastIndex));
             */

            // if it's not the same
            int mostVisible;
            if (mFirstVisibleIndex != mLastVisibleIndex) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.getLayoutManager();

                // get the visible rect of the first item
                Rect firstPercentageRect = new Rect();
                linearLayoutManager.findViewByPosition(mFirstVisibleIndex).getGlobalVisibleRect(firstPercentageRect);

                // get the visible rect of the last item
                Rect lastPercentageRect = new Rect();
                linearLayoutManager.findViewByPosition(mLastVisibleIndex).getGlobalVisibleRect(lastPercentageRect );

                // if that's a vertical list
                if (linearLayoutManager.getOrientation() == VERTICAL) {
                    if (firstPercentageRect.height() > lastPercentageRect.height()) {
                        mostVisible = mFirstVisibleIndex;
                    } else {
                        mostVisible = mLastVisibleIndex;
                    }
                } else { // else if it's a horizontal list
                    if (firstPercentageRect.width() > lastPercentageRect.width()) {
                        mostVisible = mFirstVisibleIndex;
                    } else {
                        mostVisible = mLastVisibleIndex;
                    }
                }

            } else {
                mostVisible = mFirstVisibleIndex;
            }

            System.out.println("@@@@@@@@@@@@@@@@@ onScrollChanged first visible: "+firstIndex+" last visible: "+lastIndex+" most visible: "+mostVisible);
        }
    }

}
