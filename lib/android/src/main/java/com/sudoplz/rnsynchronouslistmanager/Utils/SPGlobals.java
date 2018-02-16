package com.sudoplz.rnsynchronouslistmanager.Utils;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;

/**
 * Created by idynopia on 15/02/2018.
 */

public class SPGlobals {
    private static SPGlobals ourInstance;
    private ReactContext rcContext;
    private ReactNativeHost rcHost;
    private String mainViewTemplateName = "MyTemplate";
    private int numRenderRows;
    private String loopMode;

    public static SPGlobals getInstance() {
        return ourInstance;
    }

    public static SPGlobals init(ReactContext ctx, ReactNativeHost host) {
        if (ourInstance == null) {
            ourInstance = new SPGlobals(ctx, host);
        } else {
            ourInstance.setRcContext(ctx);
            ourInstance.setRcHost(host);
        }
        return ourInstance;
    }

    private SPGlobals(ReactContext ctx, ReactNativeHost host) {
        rcContext = ctx;
        rcHost = host;
    }


    // setters and getters


    public String getMainViewTemplateName() {
        return mainViewTemplateName;
    }

    public void setMainViewTemplateName(String mainViewTemplateName) {
        this.mainViewTemplateName = mainViewTemplateName;
    }

    public ReactContext getRcContext() {
        return rcContext;
    }

    public void setRcContext(ReactContext rcContext) {
        this.rcContext = rcContext;
    }

    public ReactNativeHost getRcHost() {
        return rcHost;
    }

    public void setRcHost(ReactNativeHost rcHost) {
        this.rcHost = rcHost;
    }

    public int getNumRenderRows() {
        return numRenderRows;
    }

    public void setNumRenderRows(int numRenderRows) {
        this.numRenderRows = numRenderRows;
    }

    public String getLoopMode() {
        return loopMode;
    }

    public void setLoopMode(String loopMode) {
        this.loopMode = loopMode;
    }
}
