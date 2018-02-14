package com.sudoplz.rnsynchronouslistmanager.Sync;

import android.os.Bundle;


import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedArray;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.bridge.Arguments;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by SudoPlz on 02/11/2017.
 */

public class SyncRootView extends ReactRootView {

    private Map<Integer,Integer> recipeTagToTag;
    private Map<String,String> knownPropNameMap;
    private @Nullable ReactInstanceManager mReactInstanceManager;
    private @Nullable String mJSModuleName;
    private Boolean hasInitialised;
//    private @Nullable Bundle mAppProperties;


    public SyncRootView(final String moduleName, ReactContext context, final ReactNativeHost rcHost) {
        super(context);
        this.setJSEntryPoint(new Runnable() {
            @Override
            public void run() {
                System.out.println("Module "+mJSModuleName+".runApplication would normally run now");
            }
        });
        hasInitialised = false;
        mReactInstanceManager = rcHost.getReactInstanceManager();
        mJSModuleName = moduleName;

        // prepare the known properties name association map
        // we want to rename the properties that have to do with colors,
        // because react will typically strip those of if we don't
        knownPropNameMap = new HashMap<String,String>();
        knownPropNameMap.put("bgColor", "backgroundColor");
        knownPropNameMap.put("bordColor", "borderColor");
        knownPropNameMap.put("txtColor", "color");

        final SyncRootView self = this;
        this.post(new Runnable() {
            // Post in the parent's message queue to make sure the parent
            // lays out its children before you call getHitRect()
            @Override
            public void run() {
                self.startReactApplication(mReactInstanceManager, mJSModuleName);
            }
        });
    }


    @Override
    protected void onAttachedToWindow() {
        final int rootTag = super.getRootViewTag();
        final SyncRootView self = this;
        this.post(new Runnable() {
            // Post in the parent's message queue to make sure the parent
            // lays out its children before you call getHitRect()
            @Override
            public void run() {
                self.runApplication();
            }
        });
        super.onAttachedToWindow();
    }



    private void runApplication() {
        ReactContext reactContext = mReactInstanceManager.getCurrentReactContext();
        final UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);

        final int rootTag = getRootViewTag();

        SyncRegistry syncModule = registryModule();

        Recipe details = syncModule.getRegistry().get(mJSModuleName);

        WritableAdvancedMap binding = new WritableAdvancedMap(details.getRecipeProps());
        ReadableMap inverseBinding = binding.getInverted();
        ReadableArray recipeInstructions = details.getRecipeInstructions();

        recipeTagToTag = new HashMap <Integer, Integer>();
        recipeTagToTag.put(new Integer(1), new Integer(rootTag));

        for (int i = 0; i < recipeInstructions.size(); i++) { // for every instruction
            // instruction example {"args":[125,"RCTText",1,{"allowFontScaling":true,"ellipsizeMode":"tail","accessible":true}],"cmd":"createView"} }
            ReadableMap instruction = recipeInstructions.getMap(i);

            // get it's arguments
            final ReadableArray args = instruction.getArray("args");

            // get the instruction main view tag
            final int tag = this.getRecipeTag(args.getInt(0), rootTag);

            // the command (usually either createView or setChildren)
            String command = instruction != null ? instruction.getString("cmd") : "";



            Bundle initialProps = this.getAppProperties();

            WritableAdvancedMap values;
            if (initialProps != null) {
                values = new WritableAdvancedMap(Arguments.fromBundle(initialProps));
            } else {
                values = new WritableAdvancedMap();
            }

            if (command.equals("createView")) {
                // rewrite the props
                final ReadableMap props = this.bindProps(new WritableAdvancedMap(args.getMap(3)), values, binding, inverseBinding, false);
                reactContext.runOnNativeModulesQueueThread(new Runnable() {
                    @Override
                    public void run() {
                        // and create the child
                        uiManager.createView(tag, args.getString(1), rootTag, props);
                    }
                });
            } else if (command.equals("setChildren")) {
                reactContext.runOnNativeModulesQueueThread(new Runnable() {
                    @Override
                    public void run() {
                        uiManager.setChildren(tag, args.getArray(1));
                    }
                });

            }
            // else if (command.equals("manageChildren")) {
            //     final int tag = this.getRecipeTag(args.getInt(0), rootTag);

            //     reactContext.runOnNativeModulesQueueThread(new Runnable() {
            //         @Override'
            //         public void run() {
            //             uiManager.manageChildren((int) tag, args.getArray(1), args.getArray(2), args.getArray(3), args.getArray(4), args.getArray(5));
            //         }
            //     });
            // }

        } // end of for loop
        if (hasInitialised == false) {
            hasInitialised = true;
        }
    }

    public void updateProps(ReadableMap newProps) {
        if (recipeTagToTag == null) return;

        final int rootTag = getRootViewTag();

        SyncRegistry module = registryModule();
        Recipe details = module.getRegistry().get(mJSModuleName);
//        NSDictionary *details = [module.registry objectForKey:self.moduleName];

        WritableAdvancedMap binding = new WritableAdvancedMap(details.getRecipeProps());
//        NSDictionary *binding = details[@"props"];

        ReadableMap inverseBinding = binding.getInverted();
//        NSDictionary *inverseBinding = [self invertMap:binding];

        WritableAdvancedArray recipe = new WritableAdvancedArray(details.getRecipeInstructions());
//        NSArray *recipe = details[@"recipe"];

        ReactContext reactContext = mReactInstanceManager.getCurrentReactContext();
        final UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);
//        RCTUIManager<UIManagerInternals> *uiManager = (RCTUIManager<UIManagerInternals>*)self.bridge.uiManager;

        WritableAdvancedMap newValues = new WritableAdvancedMap(newProps);

        for (int x = 0; x < recipe.size(); x++) {
            // for (NSDictionary *call in recipe)
            ReadableMap instruction = (ReadableMap) recipe.getReadableArrValue(x);
            String command = instruction != null ? instruction.getString("cmd") : "";
            if (command.equals("createView") ) {
                final ReadableArray args = instruction.getArray("args");
                final int tag = this.getRecipeTag(args.getInt(0), rootTag);
                final ReadableMap props = this.bindProps(new WritableAdvancedMap(args.getMap(3)), newValues, binding, inverseBinding, true);

                if (props == null) continue;

                reactContext.runOnNativeModulesQueueThread(new Runnable() {
                    @Override
                    public void run() {
                        uiManager.setChildren(tag, args.getArray(1));
                    }
                });


            }
//
        }

        reactContext.runOnNativeModulesQueueThread(new Runnable() {
            @Override
            public void run() {
                uiManager.onBatchComplete();
            }
        });
    }




    public ReadableMap bindProps(WritableAdvancedMap props, WritableAdvancedMap values, ReadableMap binding, ReadableMap inverseBinding, Boolean onlyChanges) {

        if (props == null) return null;
//        HashMap<String,Object> res = new HashMap<String,Object> ();
        WritableAdvancedMap resultMap = new WritableAdvancedMap();
        int mapSizeApproximation = 0;
        ReadableMapKeySetIterator propsIter = props.keySetIterator();

        while (propsIter.hasNextKey()) {
            // for every key in that component

            // get the key
            String propName = propsIter.nextKey();

            // get the value
            Object propValue = props.getReadableMapValue(propName);

            // find out if it's a String
            boolean propIsAString = propValue != null && props.getType(propName) == ReadableType.String;

            // figure wether we should replace the key or not (we do that for color keys otherwise rn messes them up)
            boolean propNameShouldBeReplaced = propIsAString == true &&  propValue != null && knownPropNameMap.containsKey((String) propValue);

            if (onlyChanges != true && propNameShouldBeReplaced == false) {
                Object realValue = this.getValueForProp(propName, propValue, propIsAString, values);
                if (realValue != null) {
                    // i.e propValue is @"__aPropName__"
                    resultMap.putAnyType(propName, propValue);
                    mapSizeApproximation++;
                }
            }

            if (propValue != null && propIsAString == true) {

                String valueKey = null;
                if (inverseBinding.hasKey((String) propValue)) {
                    valueKey = inverseBinding.getString((String) propValue);
                }

                if (valueKey != null)
                {
                    String realPropKey = null;

                    if (propNameShouldBeReplaced == true) {
                        realPropKey = knownPropNameMap.get((String) propValue);
                    }

                    if (realPropKey == null) {
                        realPropKey = propName;
                    }

                    Object realValue = this.getValueForProp(realPropKey, propValue, propIsAString, values);
                    if (realValue != null) {
                        // i.e propValue is @"__aPropName__"
                        resultMap.putAnyType(propName, propValue);
                        mapSizeApproximation++;
                    }
                }
            }
        }

        if (mapSizeApproximation == 0) return null;
        return resultMap;
    }

    protected Object getValueForProp(String key,Object value, Boolean valueIsAString, WritableAdvancedMap content) {
        if (key == null || value == null || content == null) {
            return null;
        }
        if (valueIsAString) {
            if (((String) value).toLowerCase().contains("__") != true) {
                return value;
            }
            // if it's a string that doesn't contain __
            // i.e propValue is @"__aPropName__"
            if (((String) value).contains(".") == true) { // if valueKey contains fullstops
                return content.getReadableMapDeepValue(key); // get the nested object
            }
            // otherwise it's a plain object (not nested)
            return content.getReadableMapValue(key); // get the nested object
        } else if (valueIsAString == false) {
            // if it's not a string
            return value;
        }
        return null;
    }


//    public long allocateTag() {
//        SyncRegistry registry = registryModule();
//        long result = registry.getLastTag();
//        result++;
//        if (result % 10 == 1) {
//            result++;
//        }
//        registry.setLastTag(result);
//        return result;
//    }

    public int getRecipeTag(int recipeTag, int rootTag) {
        // usually when the instruction is created (upon the app initialization) the root tag is 1
        if (recipeTag == 1) {
            // we want to replace that with the actual root tag
            return rootTag;
        }
        return recipeTag;
//        if (recipeTagToTag == null) {
//            recipeTagToTag = new HashMap<Integer, Integer>();
//        }
//
//        if (recipeTagToTag.get(recipeTag) != null) {
//            return recipeTagToTag.get(recipeTag).intValue();
//        }
//        int result = (int) this.allocateTag();
//        recipeTagToTag.put(new Integer(recipeTag), result);
//        return result;
    }


//    @Override
//    public void onViewAdded(View child) {
//        System.out.println("@@@@@@@@@@@@@@@@@ onViewAdded ");
//        super.onViewAdded(child);
//    }

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    public int getRootViewTag() {
        int rootTag;
        if (hasInitialised == true) {
            rootTag = super.getRootViewTag();
        } else {
            rootTag = super.getRootViewTag() - 10; // hack
            // NO Idea why the root view tag generated is not the right one (we usually get 41 instead of 31)
            // but the previous one (-10) is the right one
        }
        return rootTag;
    }

    private SyncRegistry registryModule() {
        return mReactInstanceManager
                .getCurrentReactContext()
                .getNativeModule(SyncRegistry.class);
    }










}
