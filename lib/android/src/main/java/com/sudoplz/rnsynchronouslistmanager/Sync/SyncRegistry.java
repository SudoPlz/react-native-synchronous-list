package com.sudoplz.rnsynchronouslistmanager.Sync;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SudoPlz on 02/11/2017.
 */

public class SyncRegistry extends ReactContextBaseJavaModule {

    protected Map<String, Recipe> recipeRegistry;
    protected long lastTag;

    public SyncRegistry(ReactApplicationContext reactContext) {
        super(reactContext);
        recipeRegistry = new HashMap<String, Recipe>();
        lastTag = 1000001;
    }

    @Override
    public String getName() {
        return "RCCSyncRegistry";
    }

    public Map<String, Recipe> getRegistry() {
        return recipeRegistry;
    }

    @ReactMethod
    public void registerRecipe(String registeredName, ReadableMap props, ReadableArray recipe) {
//        Toast.makeText(getReactApplicationContext(), message, duration).show();
//        [self.registry setObject:@{@"props": props, @"recipe": recipe} forKey:registeredName];
        recipeRegistry.put(registeredName, new Recipe(props,recipe));
    }

    public long getLastTag() {
        return lastTag;
    }

    public void setLastTag(long newLastTag) {
        lastTag = newLastTag;
    }
}
