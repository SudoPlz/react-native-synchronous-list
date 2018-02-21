package com.sudoplz.rnsynchronouslistmanager.Sync;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.facebook.react.bridge.AssertionException;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.appregistry.AppRegistry;
import com.facebook.systrace.Systrace;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static com.facebook.systrace.Systrace.TRACE_TAG_REACT_JAVA_BRIDGE;

/**
 * Created by SudoPlz on 02/11/2017.
 */

public class SyncRootView extends ReactRootView {

    protected Map<Integer,Integer> recipeTagToTag;
    protected Map<String,String> knownPropNameMap;
    protected @Nullable ReactInstanceManager mReactInstanceManager;
    protected ReactContext ctx;
    protected @Nullable String mJSModuleName;
    protected Boolean hasInitialised = false;
    protected Boolean isAttached = false;
    protected WritableAdvancedMap initialProps;
//    private @Nullable Bundle mAppProperties;


    public SyncRootView(String moduleName) {
        this(moduleName, SPGlobals.getInstance().getRcContext(), SPGlobals.getInstance().getRcHost(), null);
    }

    public SyncRootView(String moduleName, ReadableMap props) {
        this(moduleName, SPGlobals.getInstance().getRcContext(), SPGlobals.getInstance().getRcHost(), props);
    }

    public SyncRootView(String moduleName, ReactContext context, ReadableMap props) {
        this(moduleName, context, SPGlobals.getInstance().getRcHost(), props);
    }

    public SyncRootView(final String moduleName, ReactContext context, final ReactNativeHost rcHost, ReadableMap props) {
        super(context);

        if (props != null) {
            this.initialProps = new WritableAdvancedMap(props);
        }
        final SyncRootView self = this;
        this.setJSEntryPoint(new Runnable() {
            @Override
            public void run() {
                AssertionError re;
//                if (isAttached  == false) {
//                    self.runApplication();
//                }
                System.out.println("Module "+mJSModuleName+".runApplication would normally run now");
            }
        });
        hasInitialised = false;
        mJSModuleName = moduleName;
        ctx = context;
        mReactInstanceManager = rcHost.getReactInstanceManager();

        // prepare the known properties name association map
        // we want to rename the properties that have to do with colors,
        // because react will typically strip those of if we don't
        knownPropNameMap = new HashMap<String,String>();
        knownPropNameMap.put("bgColor", "backgroundColor");
        knownPropNameMap.put("bordColor", "borderColor");
        knownPropNameMap.put("txtColor", "color");


        Thread cur = Thread.currentThread();
        startReactApplication(mReactInstanceManager, mJSModuleName);
        this.runApplication(); // we want this ton run on the UI Thread

//        this.post(new Runnable() {
//            // Post in the parent's message queue to make sure the parent
//            // lays out its children before you call getHitRect()
//            @Override
//            public void run() {
//                self.startReactApplication(mReactInstanceManager, mJSModuleName);
//            }
//        });
    }


    @Override
    protected void onAttachedToWindow() {
        final SyncRootView self = this;
        if (isAttached  == false) {
            this.post(new Runnable() {
                // Post in the parent's message queue to make sure the parent
                // lays out its children before you call getHitRect()
                @Override
                public void run() {
                    self.runApplication(); // we want this ton run on the UI Thread
                }
            });
        }
        super.onAttachedToWindow();
    }



    public void runApplication() {


        final int rootTag = getRootViewTag();

        SyncRegistry syncModule = registryModule();

        Recipe curRecipe = syncModule.getRegistry().get(mJSModuleName);

        WritableAdvancedMap binding = curRecipe.getRecipeBindings();
        ReadableMap inverseBinding = curRecipe.getRecipeInverseBindings();
        ArrayList<Instruction> recipeInstructions = curRecipe.getRecipeInstructions();

        recipeTagToTag = new HashMap <Integer, Integer>();
        recipeTagToTag.put(new Integer(1), new Integer(rootTag));
//        recipeTagToTag.put(new Integer(1000001), new Integer(rootTag));

        for (int i = 0; i < recipeInstructions.size(); i++) { // for every instruction
            // instruction example {"args":[125,"RCTText",1,{"allowFontScaling":true,"ellipsizeMode":"tail","accessible":true}],"cmd":"createView"} }
            final Instruction instruction = recipeInstructions.get(i);

            // get it's arguments
//            final ReadableArray args = instruction.getArray("args");



            // the command (usually either createView or setChildren)
            String command = instruction.getInstructionType();

            WritableAdvancedMap newValues;
            if (this.initialProps != null) {
                newValues = new WritableAdvancedMap(this.initialProps);
            } else {
                newValues = new WritableAdvancedMap();
            }

            if (command.equals("createView")) {
                // rewrite the props
                final ReadableMap props = this.bindProps(instruction.getProps(), newValues, binding, inverseBinding, false);

                // get the instruction main view tag
                final int tag = this.getRecipeTag(instruction.getTag(), rootTag);
//                System.out.println("Is on UI thread: "+ctx.isOnUiQueueThread()+ " is on native module thread: "+ctx.isOnNativeModulesQueueThread());
                dispatchInNativeModuleThread(new Runnable() {
                    @Override
                    public void run() {
                        final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);
                        // and create the child
                        uiManager.createView(tag, instruction.getModuleName(), rootTag, props);
//                        try {
//                            uiManager.createView(tag, instruction.getModuleName(), rootTag, props);
//                        } catch (Error e) {
//                            System.out.println("runApplication.createView error: "+e);
//                            ctx.runOnUiQueueThread(this);
//                        }
                    }
                });

            } else if (command.equals("setChildren")) {
                int initRootTag = instruction.getInitialRootTag();
                final int initialRootTag = this.getRecipeTag(initRootTag, rootTag);
                dispatchInNativeModuleThread(new Runnable() {
                    @Override
                    public void run() {
                        final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);

                        // set view relationships
                        uiManager.setChildren(initialRootTag, instruction.getHierarchy());
//                        try {
//                            uiManager.setChildren(initialRootTag, instruction.getHierarchy());
//                        } catch (Error e) {
//                            System.out.println("runApplication.setChildren error: "+e);
//                            ctx.runOnUiQueueThread(this);
//                        }
                    }
                });
            }
            // else if (command.equals("manageChildren")) {
            //     final int tag = this.getRecipeTag(args.getInt(0), rootTag);

            //     dispatchInNativeModuleThread(new Runnable() {
            //         @Override'
            //         public void run() {
            //             uiManager.manageChildren((int) tag, args.getArray(1), args.getArray(2), args.getArray(3), args.getArray(4), args.getArray(5));
            //         }
            //     });
            // }

        } // end of for loop
        if (hasInitialised == false) {
            hasInitialised = true;
//            this.setLayoutParams(new LayoutParams(300, 500));
        }
        isAttached = true;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
//        int heightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
//
//        super.onMeasure(widthSpec, heightSpec);
//
////        int measuredHeight = viewMeasurer.getMeasuredHeight(heightSpec);
//        setMeasuredDimension(500, 300);
//    }

    public void updateProps(ReadableMap newProps) {
//        final SyncRootView self = this;
//        if (isAttached  == false) {
//            this.initialProps = newProps;
////            ctx ctx = mReactInstanceManager.getCurrentReactContext();
//            post(new Runnable() {
//                // Post in the parent's message queue to make sure the parent
//                // lays out its children before you call getHitRect()
//                @Override
//                public void run() {
//                    self.runApplication();
//                    isAttached = true;
//                }
//            });
//        }
        if (recipeTagToTag == null) return;

        final int rootTag = getRootViewTag();

        SyncRegistry syncModule = registryModule();
        Recipe curRecipe = syncModule.getRegistry().get(mJSModuleName);

        WritableAdvancedMap binding = curRecipe.getRecipeBindings();
        ReadableMap inverseBinding = curRecipe.getRecipeInverseBindings();
        ArrayList<Instruction> recipeInstructions = curRecipe.getRecipeInstructions();


//        final ctx ctx = mReactInstanceManager.getCurrentReactContext();
//        final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);
//        RCTUIManager<UIManagerInternals> *uiManager = (RCTUIManager<UIManagerInternals>*)self.bridge.uiManager;

        WritableAdvancedMap newValues = new WritableAdvancedMap(newProps);

        for (int x = 0; x < recipeInstructions.size(); x++) {
            // for (NSDictionary *call in recipe)
            final Instruction instruction = recipeInstructions.get(x);

            // get the instruction main view tag
            final int tag = this.getRecipeTag(instruction.getTag(), rootTag);

            final int initialRootTag = this.getRecipeTag(instruction.getInitialRootTag(), rootTag);

            // the command (usually either createView or setChildren)
            String command = instruction.getInstructionType();

            if (command.equals("createView") ) {
                final ReadableMap props = this.bindProps(instruction.getProps(), newValues, binding, inverseBinding, true);

                if (props == null) continue;

                dispatchInNativeModuleThread(new Runnable() {
                    @Override
                    public void run() {
                        final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);

                        // set view relationships
                        uiManager.updateView(tag, instruction.getModuleName(), props);
//                        try {
//                            uiManager.updateView(tag, instruction.getModuleName(), props);
//                        } catch (Error e) {
//                            System.out.println("updateProps.updateView error: "+e);
//                            ctx.runOnUiQueueThread(this);
//                        }

                    }
                });
                /**
                 * TODO See if we can use synchronouslyUpdateViewOnUIThread above instead of always using updateView
                 if ([prop isEqualToString:@"children"])
                 {
                     dispatch_async(RCTGetUIManagerQueue(), ^{
                         [_uiManager updateView:reactTag viewName:@"RCTRawText" props:@{@"text": rowValue}];
                         [_uiManager batchDidComplete];
                     });
                 } else {
                    [_uiManager synchronouslyUpdateViewOnUIThread:reactTag viewName:viewName props:@{prop: rowValue}];
                 }

                 As seen in https://hackernoon.com/react-native-listview-performance-revisited-recycling-without-the-bridge-c4f62d18c7dd
                 */
            }
//
        }

        dispatchInNativeModuleThread(new Runnable() {
            @Override
            public void run() {
                final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);

                uiManager.onBatchComplete();
//                try {
//                    uiManager.onBatchComplete();
//                } catch (Error e) {
//                    System.out.println("updateProps.onBatchComplete error: "+e);
//                    ctx.runOnUiQueueThread(this);
//                }
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

            if (onlyChanges != true) {
                if (propNameShouldBeReplaced == false) {
                    Object realValue = this.getValueForProp(propValue, propIsAString, values);
                    if (realValue != null) {
                        // i.e propValue is @"__aPropName__"
                        resultMap.putAnyType(propName, realValue);
                        mapSizeApproximation++;
                    }
                } else {
                    resultMap.putAnyType(propName, propValue);
                    mapSizeApproximation++;
                }
            }

            if (propValue != null) {
                if (propIsAString == true) {
                    String valueKey = null;
                    if (inverseBinding.hasKey((String) propValue)) {
                        valueKey = inverseBinding.getString((String) propValue);
                        if (valueKey != null)
                        {
                            String realPropKey = null;

                            if (propNameShouldBeReplaced == true) {
                                realPropKey = knownPropNameMap.get((String) propValue);
                            }

                            if (realPropKey == null) {
                                realPropKey = propName;
                            }

                            Object realValue = this.getValueForProp(propValue, propIsAString, values);
                            if (realValue != null) {
                                // i.e propValue is @"__aPropName__"
                                resultMap.putAnyType(realPropKey, realValue);
                                mapSizeApproximation++;
                            }
                        }
                    }
                }
            }
        }

        if (mapSizeApproximation == 0) return null;
        return resultMap;
    }

    protected Object getValueForProp(Object value, Boolean valueIsAString, WritableAdvancedMap content) {
        if (value == null || content == null) {
            return null;
        }
        if (valueIsAString) {
            String strVal = (String) value;
            if (strVal.toLowerCase().contains("__") != true) {
                // if it's a string that doesn't contain __
                return value;
            }

            // on the other hand if it contains __ remove them
            // i.e strVal is @"__aPropName__"
            strVal = strVal.replaceAll("__","");
            // should now be @"aPropName"



            if (strVal.contains(".") == true) { // if valueKey contains fullstops
                return content.getReadableMapDeepValue(strVal); // get the nested object
            }
            // otherwise it's a plain object (not nested)
            return content.getReadableMapValue(strVal); // get the nested object
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


//    private void runOnNativeModuleThread(Runnable runnable, ReactContext reactContext) {
//        if (ctx.isOnNativeModulesQueueThread()) {
//            runnable.run();
//        } else {
//            ctx.runOnNativeModulesQueueThread(runnable);
//        }
//    }

    public int getRecipeTag(int recipeTag, int rootTag) {
        // usually when the instruction is created (upon the app initialization) the root tag is 1
        if (recipeTag == 1000001 || recipeTag == 1) {
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewGroup parent = (ViewGroup) this.getParent();
        parent.removeView(this);
        isAttached = false;
    }


    @Override
    public int getRootViewTag() {
        int rootTag;
//        if (hasInitialised == true) {
            rootTag = super.getRootViewTag();
//        } else {
//            rootTag = super.getRootViewTag() - 10; // hack
            // NO Idea why the root view tag generated is not the right one (we usually get 41 instead of 31)
            // but the previous one (-10) is the right one
//        }
        return rootTag;
    }

    protected SyncRegistry registryModule() {
        return ctx.getNativeModule(SyncRegistry.class);
    }


    protected void dispatchInNativeModuleThread(Runnable runnable) {
        if (runnable == null) {
            return;
        }
//        final UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);
        ctx.runOnNativeModulesQueueThread(runnable);
    }

    public void setInitialProps(ReadableMap initProps) {
        this.initialProps = new WritableAdvancedMap(initProps);
    }





}
