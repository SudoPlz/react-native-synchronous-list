package com.sudoplz.rnsynchronouslistmanager;


import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRegistry;
import com.sudoplz.rnsynchronouslistmanager.Utils.*;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

public class SynchronousListPackage implements ReactPackage {
    private SynchronousListManager listManager;
    private ReactNativeHost rcHost;

    public SynchronousListPackage(ReactNativeHost host) {
        rcHost = host;
    }
    /**
     * @param reactContext react application context that can be used to create modules
     * @return list of native modules to register with the newly created catalyst instance
     */
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        if (listManager == null) {
            listManager = new SynchronousListManager(reactContext, rcHost);
            // we've passed the host, no need to keep a reference of it any more
            rcHost = null;
        }
        return Arrays.<NativeModule>asList(
                new SyncRegistry(reactContext),
                new SynchronousListModule(reactContext, listManager)
        );
    }



    /**
     * @param reactContext
     * @return a list of view managers that should be registered with {@link UIManagerModule}
     */
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        if (listManager == null) {
            listManager = new SynchronousListManager(reactContext, rcHost);
        }
        return Arrays.<ViewManager>asList(
                listManager
        );
    }
}
