package com.sudoplz.rnsynchronouslistmanager.List;

import android.content.Context;
import android.util.AttributeSet;

import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRootView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;

/**
 * Created by SudoPlz on 06/02/2018.
 */

public class SynchronousRowView extends SyncRootView {

    public SynchronousRowView(ReactContext context, ReactNativeHost rcHost) {
        super("RNSynchronousListRowTemplate", context, rcHost);
    }

}
