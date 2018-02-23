    package com.sudoplz.rnsynchronouslistmanager.Views;

    import android.view.ViewGroup;


    import com.facebook.react.bridge.queue.MessageQueueThreadImpl;
    import com.sudoplz.rnsynchronouslistmanager.Sync.Instruction;
    import com.sudoplz.rnsynchronouslistmanager.Sync.Recipe;
    import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRegistry;
    import com.sudoplz.rnsynchronouslistmanager.Utils.FrameUtils;
    import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;
    import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;
    import com.facebook.react.ReactInstanceManager;
    import com.facebook.react.ReactNativeHost;
    import com.facebook.react.ReactRootView;
    import com.facebook.react.bridge.ReactContext;
    import com.facebook.react.bridge.ReadableMap;
    import com.facebook.react.bridge.ReadableMapKeySetIterator;
    import com.facebook.react.bridge.ReadableType;
    import com.facebook.react.uimanager.UIManagerModule;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;

    import javax.annotation.Nullable;

    /**
     * Created by SudoPlz on 02/11/2017.
     */

    public class SyncRootView extends ReactRootView {

        protected Map<Integer,Integer> recipeTagToTag;
        protected Map<String,String> knownPropNameMap;
        protected @Nullable ReactInstanceManager mReactInstanceManager;
        protected ReactContext ctx;
        protected @Nullable String mJSModuleName;
        protected Boolean hasInit = false;
        protected Boolean isAttached = false;
        protected WritableAdvancedMap initialProps;
        protected MessageQueueThreadImpl nativeModulesThread;
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

            // setting the layout parameters
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            hasInit = false;
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


            nativeModulesThread = (MessageQueueThreadImpl) ctx.getCatalystInstance().getReactQueueConfiguration().getNativeModulesQueueThread();

    //        Thread cur = Thread.currentThread();
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
            recipeTagToTag.put(new Integer(1000001), new Integer(rootTag));

            final int operationCnt = recipeInstructions.size();
            for (int i = 0; i < operationCnt; i++) { // for every instruction
                // instruction example {"args":[125,"RCTText",1,{"allowFontScaling":true,"ellipsizeMode":"tail","accessible":true}],"cmd":"createView"} }
                final Instruction instruction = recipeInstructions.get(i);

                // get it's arguments
    //            final ReadableArray args = instruction.getArray("args");


                // the command (usually either createView or setChildren)
                String command = instruction.getInstructionType();
                final int curOperation = i;
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
                    final int tag = this.translateTagIfNeeded(instruction.getTag());
    //                System.out.println("Is on UI thread: "+ctx.isOnUiQueueThread()+ " is on native module thread: "+ctx.isOnNativeModulesQueueThread());
                    dispatchInAppropriateThread(new Runnable() {
                        @Override
                        public void run() {
                            final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);
                            // and create the child
                            uiManager.createView(tag, instruction.getModuleName(), rootTag, props);

                            // check if that's the last operation
                            if (curOperation == operationCnt - 1 && hasInit == false) {
                                // if it is,
                                hasInit = true;
                                int viewRootTag = getRootViewTag();
//                                System.out.println("@@@@@@@@@@@@@ View initialised for " + viewRootTag);
                            }
                        }
                    });

                } else if (command.equals("setChildren")) {
                    int initRootTag = instruction.getInitialRootTag();
                    final int initialRootTag = this.translateTagIfNeeded(initRootTag);
                    dispatchInAppropriateThread(new Runnable() {
                        @Override
                        public void run() {
                            final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);

                            // set view relationships
                            uiManager.setChildren(initialRootTag, instruction.getHierarchy());

                            // check if that's the last operation
                            if (curOperation == operationCnt - 1 && hasInit == false) {
                                // if it is,
                                hasInit = true;
                                int viewRootTag = getRootViewTag();
//                                System.out.println("@@@@@@@@@@@@@ View initialised for " + viewRootTag);
                            }
                        }
                    });
                }
                // else if (command.equals("manageChildren")) {
                //     final int tag = this.translateTagIfNeeded(args.getInt(0));

                //     dispatchInAppropriateThread(new Runnable() {
                //         @Override'
                //         public void run() {
                //             uiManager.manageChildren((int) tag, args.getArray(1), args.getArray(2), args.getArray(3), args.getArray(4), args.getArray(5));
                //         }
                //     });
                // }

            } // end of for loop
//            System.out.println("@@@@@@@@@@@@@ View attached for "+getRootViewTag());
            isAttached = true;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int viewRootTag = getRootViewTag();
//            System.out.println("@@@@@@@@@@@@@ Views measured for " + viewRootTag);
            int widthSpec = MeasureSpec.makeMeasureSpec(FrameUtils.extractItemWidth(this.initialProps), MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(FrameUtils.extractItemHeight(this.initialProps), MeasureSpec.EXACTLY);

            super.onMeasure(widthSpec, heightSpec);
        }

        public void updateProps(ReadableMap newProps) {
            if (recipeTagToTag == null) return;

            final int rootTag = getRootViewTag();

            SyncRegistry syncModule = registryModule();
            Recipe curRecipe = syncModule.getRegistry().get(mJSModuleName);

            WritableAdvancedMap binding = curRecipe.getRecipeBindings();
            ReadableMap inverseBinding = curRecipe.getRecipeInverseBindings();
            ArrayList<Instruction> recipeInstructions = curRecipe.getRecipeInstructions();

            WritableAdvancedMap newValues = new WritableAdvancedMap(newProps);

            for (int x = 0; x < recipeInstructions.size(); x++) {
                final Instruction instruction = recipeInstructions.get(x);

                // get the instruction main view tag
                final int tag = this.translateTagIfNeeded(instruction.getTag());

                final int initialRootTag = this.translateTagIfNeeded(instruction.getInitialRootTag());

                // the command (usually either createView or setChildren)
                String command = instruction.getInstructionType();

                if (command.equals("createView") ) {
                    final ReadableMap props = this.bindProps(instruction.getProps(), newValues, binding, inverseBinding, true);

                    if (props == null) continue;

                    dispatchInAppropriateThread(new Runnable() {
                        @Override
                        public void run() {
                            final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);

                            // set view relationships
                            uiManager.updateView(tag, instruction.getModuleName(), props);
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

            dispatchInAppropriateThread(new Runnable() {
                @Override
                public void run() {
                    final UIManagerModule uiManager = ctx.getNativeModule(UIManagerModule.class);

                    uiManager.onBatchComplete();
                }
            });
        }




        public ReadableMap bindProps(WritableAdvancedMap props, WritableAdvancedMap values, ReadableMap binding, ReadableMap inverseBinding, Boolean onlyChanges) {

            if (props == null) return null;

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


        public int translateTagIfNeeded(int recipeTag) {
            if (recipeTagToTag.get(recipeTag) != null) {
                return recipeTagToTag.get(recipeTag).intValue();
            }
    //        int result = (int) this.allocateTag();
    //        recipeTagToTag.put(new Integer(recipeTag), result);
            return recipeTag;
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


        protected SyncRegistry registryModule() {
            return ctx.getNativeModule(SyncRegistry.class);
        }


        protected void dispatchInAppropriateThread(Runnable runnable) {
            if (runnable == null) {
                return;
            }

            if (nativeModulesThread.getLooper().getThread().isAlive()) {
                ctx.runOnNativeModulesQueueThread(runnable);
            } else {
                this.post(runnable);
            }
    //        final UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);
        }

        public void setInitialProps(ReadableMap initProps) {
//            System.out.println("@@@@@@@@@@@@@ View props received for " + getRootViewTag());
            this.initialProps = new WritableAdvancedMap(initProps);
        }


        public Boolean hasInitialised() {
            return hasInit;
        }


    }
