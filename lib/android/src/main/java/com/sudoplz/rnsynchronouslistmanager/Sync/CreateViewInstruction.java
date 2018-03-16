package com.sudoplz.rnsynchronouslistmanager.Sync;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedArray;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;

/**
 * Created by SudoPlz on 19/02/2018.
 */

public class CreateViewInstruction implements Instruction {
    private String moduleName;
    private int tag;
    private int initialRootTag;
    private WritableAdvancedMap props;

    public CreateViewInstruction (ReadableArray recipeArgs) {
//        "args":[125,"RCTText",1,{"allowFontScaling":true,"ellipsizeMode":"tail","accessible":true}],"
        tag = recipeArgs.getInt(0);
        moduleName = recipeArgs.getString(1);
        initialRootTag = recipeArgs.getInt(2);
        props = new WritableAdvancedMap(recipeArgs.getMap(3));
    }
    @Override
    public int getInitialRootTag() {
        return initialRootTag;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public WritableAdvancedMap getProps() {
        return props;
    }

    @Override
    public ReadableArray getHierarchy() {
        return null;
    }

    @Override
    public String getInstructionType() {
        return "createView";
    }

    @Override
    public String toString() {
        return "createView: ["+tag+", "+moduleName+", "+initialRootTag+", "+props;
    }
}
