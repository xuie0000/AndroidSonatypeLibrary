package com.xuie0000;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Jie Xu
 * @date 17-1-18
 */
public class PreferenceUtils {
    private static SharedPreferences mSharedPreferences = null;
    private static SharedPreferences.Editor mEditor = null;

    public static void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
    }

    public static void setString(String key, String value) {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public static String getString(String key, String defaultValue) {
        checkNotNull();
        return mSharedPreferences.getString(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        checkNotNull();
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public static void setLong(String key, long value) {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public static long getLong(String key, long defaultValue) {
        checkNotNull();
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public static Boolean getBoolean(String key, boolean defaultValue) {
        checkNotNull();
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void removeKey(String key) {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        mEditor.remove(key);
        mEditor.apply();
    }

    public static void removeAll() {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public static void setPreference(String key, Object object) {
        checkNotNull();
        mEditor = mSharedPreferences.edit();
        if (object instanceof String) {
            mEditor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            mEditor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            mEditor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            mEditor.putLong(key, (Long) object);
        } else {
            mEditor.putString(key, object.toString());
        }
        mEditor.apply();
    }

    private static void checkNotNull() {
        if (mSharedPreferences == null) {
            throw new RuntimeException("SharedPreferences unInit");
        }
    }
}
