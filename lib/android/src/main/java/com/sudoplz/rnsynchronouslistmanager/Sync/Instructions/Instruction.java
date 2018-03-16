package com.sudoplz.rnsynchronouslistmanager.Sync.Instructions;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;

/**
 * Created by SudoPlz on 02/11/2017.
 */

public interface Instruction {

    public int getInitialRootTag();
    public int getTag();
    public String getModuleName();
    public WritableAdvancedMap getProps();
    public ReadableArray getHierarchy();
    public String getInstructionType();

}
