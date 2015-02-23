package com.webmyne.paylabas.userapp.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Android on 23-02-2015.
 */
public class PrefUtils {
    public static boolean isEnglishSelected(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("isEnglishSelected", false);
    }

    public static void setEnglishSelected(final Context context, final boolean isEnglishSelected) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isEnglishSelected", isEnglishSelected).commit();
    }
}
