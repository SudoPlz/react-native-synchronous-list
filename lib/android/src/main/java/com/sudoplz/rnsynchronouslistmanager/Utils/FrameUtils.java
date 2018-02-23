package com.sudoplz.rnsynchronouslistmanager.Utils;

import com.facebook.react.bridge.ReadableMap;

/**
 * Created by idynopia on 23/02/2018.
 */

public class FrameUtils {
    public static int extractItemWidth(ReadableMap props) {
        if (props != null && props.hasKey("width")) {
            return props.getInt("width");
        }
        return 0;
    }

    public static int extractItemHeight(ReadableMap props) {
        if (props != null && props.hasKey("height")) {
            return props.getInt("height");
        }
        return 0;
    }

}
