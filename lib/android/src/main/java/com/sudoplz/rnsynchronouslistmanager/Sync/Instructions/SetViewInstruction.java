package com.sudoplz.rnsynchronouslistmanager.Sync.Instructions;

import com.facebook.react.bridge.ReadableArray;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;

/**
 * Created by SudoPlz on 19/02/2018.
 */

public class SetViewInstruction implements Instruction {
    private int initialRootTag;
    private ReadableArray hierarchyArr;

    public SetViewInstruction(ReadableArray recipeArgs) {
//        "args": [3, [2]],
        initialRootTag = recipeArgs.getInt(0);
        hierarchyArr = recipeArgs.getArray(1);
    }
    @Override
    public int getInitialRootTag() {
        return initialRootTag;
    }

    @Override
    public int getTag() {
        return -1;
    }

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    public WritableAdvancedMap getProps() {
        return null;
    }

    @Override
    public ReadableArray getHierarchy() {
        return hierarchyArr;
    }

    @Override
    public String getInstructionType() {
        return "setChildren";
    }

    @Override
    public String toString() {
        return "setChildren: "+initialRootTag+", "+hierarchyArr;
    }
}
