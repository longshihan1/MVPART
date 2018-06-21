package com.longshihan.mvpcomponent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class PreferenceUtils {
    private static Context mContext;

    public static final String TAG = PreferenceUtils.class.getSimpleName();

        private PreferenceUtils() {

        }

        public static void init(Context context){
            mContext = context;
        }

        public static void setValue(String key, String value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            sp.edit().putString(key, value).apply();
        }

        public static String getValue(String key, String defValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sp.getString(key, defValue);
        }

        public static void setValue(String key, float value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            sp.edit().putFloat(key, value).apply();
        }

        public static float getValue(String key, float defValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sp.getFloat(key, defValue);
        }

        public static void setValue(String key, double value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            sp.edit().putLong(key, Double.doubleToLongBits(value)).apply();
        }

        public static double getValue(String key, double defValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(defValue)));
        }

        public static void setValue(String name, String key, String value) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            sp.edit().putString(key, value).apply();
        }

        public static String getValue(String name, String key, String defValue) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            return sp.getString(key, defValue);
        }

        public static void setValue(String name, String key, float value) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            sp.edit().putFloat(key, value).apply();
        }

        public static float getValue(String name, String key, float defValue) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            return sp.getFloat(key, defValue);
        }

        public static void setValue(String key, boolean value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            sp.edit().putBoolean(key, value).apply();
        }

        public static boolean getValue(String key, boolean defValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sp.getBoolean(key, defValue);
        }

        public static void setValue(String name, String key, boolean value) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            sp.edit().putBoolean(key, value).apply();
        }

        public static boolean getValue(String name, String key, boolean defValue) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            return sp.getBoolean(key, defValue);
        }

        public static void setValue(String name, String key, int value) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            sp.edit().putInt(key, value).apply();
        }

        public static int getValue(String name, String key, int defValue) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            return sp.getInt(key, defValue);
        }

        public static void setValue(String key, int value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            sp.edit().putInt(key, value).apply();
        }

        public static int getValue(String key, int defValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sp.getInt(key, defValue);
        }

        public static void setValue(String key, long value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            sp.edit().putLong(key, value).apply();
        }

        public static long getValue(String key, long defValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sp.getLong(key, defValue);
        }

        private static boolean isParcelableCreator(Field field) {
            return Modifier.toString(field.getModifiers()).equals("public static final") && "CREATOR".equals(field.getName());
        }

        public static void setObject(Object o) {
            Field[] fields = o.getClass().getDeclaredFields();
            SharedPreferences sp = mContext.getSharedPreferences(o.getClass().getName(), 0);
            SharedPreferences.Editor editor = sp.edit();
            for(int i = 0; i < fields.length; ++i) {
                if(!isParcelableCreator(fields[i])) {
                    Class type = fields[i].getType();
                    String name = fields[i].getName();
                    Object e;
                    if(isSingle(type)) {
                        try {
                            if(type != Character.TYPE && !type.equals(String.class)) {
                                if(!type.equals(Integer.TYPE) && !type.equals(Short.class)) {
                                    if(type.equals(Double.TYPE)) {
                                        editor.putLong(name, Double.doubleToLongBits(fields[i].getDouble(o)));
                                    } else if(type.equals(Float.TYPE)) {
                                        editor.putFloat(name, fields[i].getFloat(o));
                                    } else if(type.equals(Long.TYPE) && !name.equals("serialVersionUID")) {
                                        editor.putLong(name, fields[i].getLong(o));
                                    } else if(type.equals(Boolean.TYPE)) {
                                        editor.putBoolean(name, fields[i].getBoolean(o));
                                    }
                                } else {
                                    editor.putInt(name, fields[i].getInt(o));
                                }
                            } else {
                                e = fields[i].get(o);
                                editor.putString(name, null == e?null:e.toString());
                            }
                        } catch (IllegalAccessException var14) {
//                            LogUtils.e(TAG, var14);
                        } catch (IllegalArgumentException var15) {
//                            LogUtils.e(TAG, var15);
                        }
                    } else if(isObject(type)) {
                        try {
                            e = fields[i].get(o);
                            if(null != e) {
                                setObject( e);
                            } else {
                                try {
                                    setObject(fields[i].getClass().newInstance());
                                } catch (InstantiationException var11) {
                                    ;
                                }
                            }
                        } catch (IllegalArgumentException var12) {
                            ;
                        } catch (IllegalAccessException var13) {
                            ;
                        }
                    } else {
                        try {
                            e = fields[i].get(o);
                            if(null != e) {
                                editor.putString(name, JSONHelper.toJSON(e));
                            }
                        } catch (IllegalAccessException var10) {
                            Logger.e(TAG, var10);
                        }
                    }
                }
            }

            editor.apply();
        }

        public static <T> T getObject( Class<T> clazz) {
            T o = null;

            try {
                o = clazz.newInstance();
            } catch (Exception var15) {
//                LogUtils.e(TAG, var15);
                return o;
            }

            Field[] fields = clazz.getDeclaredFields();
            SharedPreferences sp = mContext.getSharedPreferences(clazz.getName(), 0);

            for(int i = 0; i < fields.length; ++i) {
                if(!isParcelableCreator(fields[i])) {
                    Class type = fields[i].getType();
                    String name = fields[i].getName();
                    String var18;
                    if(isSingle(type)) {
                        try {
                            fields[i].setAccessible(true);
                            if(type != Character.TYPE && !type.equals(String.class)) {
                                if(!type.equals(Integer.TYPE) && !type.equals(Short.class)) {
                                    if(type.equals(Double.TYPE)) {
                                        fields[i].setDouble(o, Double.longBitsToDouble(sp.getLong(name, 0L)));
                                    } else if(type.equals(Float.TYPE)) {
                                        fields[i].setFloat(o, sp.getFloat(name, 0.0F));
                                    } else if(type.equals(Long.TYPE)) {
                                        fields[i].setLong(o, sp.getLong(name, 0L));
                                    } else if(type.equals(Boolean.TYPE)) {
                                        fields[i].setBoolean(o, sp.getBoolean(name, false));
                                    }
                                } else {
                                    fields[i].setInt(o, sp.getInt(name, 0));
                                }
                            } else {
                                var18 = sp.getString(name, (String)null);
                                if(null != var18) {
                                    fields[i].set(o, var18);
                                }
                            }
                        } catch (IllegalAccessException var16) {
//                            LogUtils.e(TAG, var16);
                        } catch (IllegalArgumentException var17) {
//                            LogUtils.e(TAG, var17);
                        }
                    } else if(isObject(type)) {
                        Object tempValue = getObject(fields[i].getType());
                        if(null != tempValue) {
                            fields[i].setAccessible(true);

                            try {
                                fields[i].set(o, tempValue);
                            } catch (IllegalArgumentException var12) {
                                ;
                            } catch (IllegalAccessException var13) {
                                ;
                            }
                        }
                    } else {
                        var18 = sp.getString(name, (String)null);
                        if(!TextUtils.isEmpty(var18)) {
                            Object tempObj = null;
                            if(isArray(type)) {
                                tempObj = JSONHelper.parseArray(var18, type);
                            } else if(isCollection(type)) {
                                tempObj = JSONHelper.parseCollection(var18, type, JSONHelper.getGeneric(fields[i]));
                            } else if(isMap(type)) {
                                ;
                            }

                            if(null != tempObj) {
                                fields[i].setAccessible(true);

                                try {
                                    fields[i].set(o, tempObj);
                                } catch (IllegalAccessException var11) {
//                                    LogUtils.e(TAG, var11);
                                }
                            }
                        }
                    }
                }
            }

            return o;
        }

        private static boolean isObject(Class<?> clazz) {
            return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz) && !isMap(clazz);
        }

        private static boolean isSingle(Class<?> clazz) {
            return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
        }

        public static boolean isBoolean(Class<?> clazz) {
            return clazz != null && (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
        }

        public static boolean isNumber(Class<?> clazz) {
            return clazz != null && (Byte.TYPE.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom(clazz) || Float.TYPE.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz));
        }

        public static boolean isString(Class<?> clazz) {
            return clazz != null && (String.class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz));
        }

        public static boolean isArray(Class<?> clazz) {
            return clazz != null && clazz.isArray();
        }

        public static boolean isCollection(Class<?> clazz) {
            return clazz != null && Collection.class.isAssignableFrom(clazz);
        }

        public static boolean isMap(Class<?> clazz) {
            return clazz != null && Map.class.isAssignableFrom(clazz);
        }

        public static void clearSettings(Context context) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().clear().apply();
        }

        public static void clearSettings(String name) {
            SharedPreferences sp = mContext.getSharedPreferences(name, 0);
            sp.edit().clear().apply();
        }

        public static void clearObject(Class clazz) {
            Field[] fields = clazz.getFields();

            for(int i = 0; i < fields.length; ++i) {
                Class type = fields[i].getType();
                if(isObject(type)) {
                    clearObject( type);
                }
            }

            clearSettings(clazz.getName());
        }


}
