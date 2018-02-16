package com.sudoplz.rnsynchronouslistmanager.Utils;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
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
public class WritableAdvancedMap extends WritableNativeMap {

    public WritableAdvancedMap() {
        super();
    }

    public WritableAdvancedMap(ReadableMap existingMap) {
        super();
        ReadableMapKeySetIterator rMapIter = existingMap.keySetIterator();

        while (rMapIter.hasNextKey()) {
            String propKey = rMapIter.nextKey();
            Object value = getReadableMapValue(existingMap, propKey);
            if (value != null) {
                this.putAnyType(propKey, value);
            }
        }
    }

    public Object getReadableMapValue(String propName) {
        return this.getReadableMapValue(this, propName);
    }

    public Object getReadableMapValue(ReadableMap mapToUse, String propName) {
        if (propName == null) {
            return null;
        }
        if (mapToUse == null) {
            mapToUse = this;
        }
        Object propValue = null;
        if (mapToUse.hasKey(propName) == false) {
            return null;
        }
        switch (mapToUse.getType(propName)) {
            case Boolean:
                return mapToUse.getBoolean(propName);
            case Number:
                return mapToUse.getDouble(propName);
            case String:
                return mapToUse.getString(propName);
            case Map:
                return mapToUse.getMap(propName);
            case Array:
                return mapToUse.getArray(propName);
            case Null:
            default:
                return propValue;
        }
    }

    public Object getReadableMapDeepValue(String path) {
        if (path != null) {
            String[] pathParts = path.split("\\.");
            ReadableMap mapToUse = this;
            if (pathParts.length > 0) {
//                boolean foundValue = false;
                Object valueFound = null;

                int partIt = 0;
                while (partIt < pathParts.length) {
                    String curKey = pathParts[partIt];
                    try {
                        ReadableType curType = mapToUse.getType(curKey);
                        if (curType != ReadableType.Map) {
                            mapToUse = mapToUse.getMap(curKey);
                        }
                        valueFound = this.getReadableMapValue(mapToUse, curKey);
                        partIt++;
                    } catch (Exception e) {
                        return null;
                    }
                }
                return valueFound;
            } else if (pathParts.length == 0) {
                return this.getReadableMapValue(mapToUse, pathParts[0]);
            }
        }
        return null;
    }

    public WritableMap putAnyType(String key, Object value) {
        if (value == null || key == null) {
            return this;
        }
        Class type = value.getClass();
        if (type == Integer.class) {
            this.putInt(key, (Integer) value);
        } else if (type == String.class) {
            this.putString(key, (String) value);
        } else if (type == Float.class) {
            this.putDouble(key, Double.valueOf(value.toString()));
        } else if (type == Double.class) {
            this.putDouble(key, (Double) value);
        } else if (type == Boolean.class) {
            this.putBoolean(key, (Boolean) value);
        } else if (type == ArrayList.class) {
            this.putArray(key, convertListToWritableArray((ArrayList) value));
        } else if (type == Map.class) {
            this.putMap(key, convertMapToWritableMap((Map) value));
        }
        return this;
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
                rArray.pushArray(convertListToWritableArray((ArrayList) value));
            } else if (type == Map.class) {
                rArray.pushMap(convertMapToWritableMap((Map) value));
            }
        }
        return rArray;
    }



    public WritableMap convertMapToWritableMap(Map aMap) {
        if (aMap == null) {
            return null;
        }
        WritableNativeMap rMap = new WritableNativeMap();
        Iterator it = aMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Object value = pair.getValue();
            String key = (String) pair.getKey();
            Class type = value.getClass();

            if (type == Integer.class) {
                rMap.putInt(key, (Integer) value);
            } else if (type == String.class) {
                rMap.putString(key, (String) value);
            } else if (type == Float.class) {
                rMap.putDouble(key, Double.valueOf(value.toString()));
            } else if (type == Double.class) {
                rMap.putDouble(key, (Double) value);
            } else if (type == Boolean.class) {
                rMap.putBoolean(key, (Boolean) value);
            } else if (type == ArrayList.class) {
                rMap.putArray(key, convertListToWritableArray((ArrayList) value));
            } else if (type == Map.class) {
                rMap.putMap(key, convertMapToWritableMap((Map) value));
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return rMap;
    }


    /**
     * Converts a ReadableMap's keys to values and it's values to keys
     * Works w/ ReadableMaps that contain String keys and values only
     * @return {ReadableMap}
     */
    public ReadableMap getInverted() {
        WritableNativeMap invertedMap = new WritableNativeMap();
        ReadableMapKeySetIterator rMapIter = this.keySetIterator();

        while (rMapIter.hasNextKey()) {
            String propKey = rMapIter.nextKey();
            String propValue = this.getString(propKey);
            invertedMap.putString(propValue, propKey);
        }

        return invertedMap;
    }
}
