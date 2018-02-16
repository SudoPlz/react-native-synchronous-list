package com.sudoplz.rnsynchronouslistmanager.Utils;


import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;

import com.facebook.react.bridge.WritableNativeMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by SudoPlz on 07/02/2018.
 */
@DoNotStrip
public class WritableAdvancedArray extends WritableNativeArray {

    public WritableAdvancedArray() {
        super();
    }

    public WritableAdvancedArray(ReadableArray existingArr) {
        super();

        for (int i = 0; i < existingArr.size(); i++ ) {
            Object value = getReadableArrValue(existingArr, i);
            if (value != null) {
                this.pushAnyType(value);
            }
        }
    }

    public Object getReadableArrValue(int itemIndex) {
        return this.getReadableArrValue(this, itemIndex);
    }

    public Object getReadableArrValue(ReadableArray arrToUse, int itemIndex) {
        if (arrToUse == null) {
            return null;
        }
        if (arrToUse == null) {
            arrToUse = this;
        }
        if (arrToUse.size() <= itemIndex) {
            return null;
        }
        Object propValue = null;
        switch (arrToUse.getType(itemIndex)) {
            case Boolean:
                return arrToUse.getBoolean(itemIndex);
            case Number:
                return arrToUse.getDouble(itemIndex);
            case String:
                return arrToUse.getString(itemIndex);
            case Map:
                return arrToUse.getMap(itemIndex);
            case Array:
                return arrToUse.getArray(itemIndex);
            case Null:
            default:
                return propValue;
        }
    }


    public WritableArray convertListToWritableArray(ArrayList aList) {
        if (aList == null) {
            return null;
        }
        WritableNativeArray rArray = new WritableNativeArray();
        for (Object value : aList) {
            Class type = value.getClass();
            if (type == Integer.class) {
                rArray.pushInt((Integer) value);
            } else if (type == String.class) {
                rArray.pushString((String) value);
            } else if (type == Double.class) {
                rArray.pushDouble((Double) value);
            } else if (type == Float.class) {
                rArray.pushDouble(Double.valueOf(value.toString()));
            } else if (type == Boolean.class) {
                rArray.pushBoolean((Boolean) value);
            } else if (type == ArrayList.class) {
                rArray.pushArray(this.convertListToWritableArray((ArrayList) value));
            } else if (type == Map.class) {
                rArray.pushMap(this.convertMapToWritableMap((Map) value));
            }
        }
        return rArray;
    }



    public WritableMap convertMapToWritableMap(Map aMap) {
        if (aMap == null) {
            return null;
        }
        WritableNativeMap rArray = new WritableNativeMap();
        Iterator it = aMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Object value = pair.getValue();
            String key = (String) pair.getKey();
            Class type = value.getClass();

            if (type == Integer.class) {
                rArray.putInt(key, (Integer) value);
            } else if (type == String.class) {
                rArray.putString(key, (String) value);
            } else if (type == Double.class) {
                rArray.putDouble(key, (Double) value);
            } else if (type == Float.class) {
                rArray.putDouble(key, Double.valueOf(value.toString()));
            } else if (type == Boolean.class) {
                rArray.putBoolean(key, (Boolean) value);
            } else if (type == ArrayList.class) {
                rArray.putArray(key, this.convertListToWritableArray((ArrayList) value));
            } else if (type == Map.class) {
                rArray.putMap(key, this.convertMapToWritableMap((Map) value));
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return rArray;
    }

    public WritableAdvancedArray pushAnyType(Object value) {
        if (value == null) {
            return this;
        }
        Class type = value.getClass();
        if (type == Integer.class) {
            this.pushInt((Integer) value);
        } else if (type == String.class) {
            this.pushString((String) value);
        } else if (type == Double.class) {
            this.pushDouble((Double) value);
        } else if (type == Float.class) {
            this.pushDouble(Double.valueOf(value.toString()));
        } else if (type == Boolean.class) {
            this.pushBoolean((Boolean) value);
        } else if (type == ArrayList.class) {
            this.pushArray(this.convertListToWritableArray((ArrayList) value));
        } else if (type == Map.class) {
            this.pushMap(this.convertMapToWritableMap((Map) value));
        }
        return this;
    }


    public static ArrayList<Object> shallowToArrayList(ReadableArray arr) {
        if (arr == null || arr.size() <= 0) {
            return new ArrayList<>();
        }
        ArrayList<Object> arrayList = new ArrayList<>();


        for (int i = 0; i < arr.size(); i++) {
            switch (arr.getType(i)) {
                case Null:
                    arrayList.add(null);
                    break;
                case Boolean:
                    arrayList.add(arr.getBoolean(i));
                    break;
                case Number:
                    arrayList.add(arr.getDouble(i));
                    break;
                case String:
                    arrayList.add(arr.getString(i));
                    break;
                case Map:
                    arrayList.add(arr.getMap(i));
                    break;
                case Array:
                    arrayList.add(arr.getArray(i));
                    break;
                default:
                    throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
            }
        }
        return arrayList;
    }

}
